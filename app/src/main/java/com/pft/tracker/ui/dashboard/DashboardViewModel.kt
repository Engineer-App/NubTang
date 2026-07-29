package com.pft.tracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.dao.ExpenseBySourceRow
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.CategoryBudgetStatus
import com.pft.tracker.domain.model.MonthRange
import com.pft.tracker.domain.model.customRange
import com.pft.tracker.domain.model.toEpochRange
import com.pft.tracker.ui.common.ChartPalette
import com.pft.tracker.ui.common.PieSlice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

enum class DashboardPeriod { THIS_MONTH, LAST_MONTH, THIS_YEAR, CUSTOM }

data class DashboardUiState(
    val userName: String = "ผู้ใช้งาน",
    val period: DashboardPeriod = DashboardPeriod.THIS_MONTH,
    val periodLabel: String = "",
    val totalExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val cashTotal: Double = 0.0,
    val bankTotal: Double = 0.0,
    val creditCardDebt: Double = 0.0,
    val netBalance: Double = 0.0,
    val budgetStatuses: List<CategoryBudgetStatus> = emptyList(),
    val expensePieSlices: List<PieSlice> = emptyList(),
    val previousExpense: Double = 0.0,
    val previousIncome: Double = 0.0,
    val showBudgetDetails: Boolean = false,
    val showSourceDetails: Boolean = false
)

private data class PeriodState(val period: DashboardPeriod, val range: MonthRange, val previousRange: MonthRange, val label: String)
private data class CurrentData(
    val expense: Double,
    val income: Double,
    val budgets: List<CategoryBudgetStatus>,
    val bySource: List<ExpenseBySourceRow>
)
private data class PrevData(val expense: Double, val income: Double)

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(private val container: AppContainer) : ViewModel() {

    private val transactionRepository = container.transactionRepository
    private val budgetUseCase = container.budgetUseCase
    private val accountBalanceUseCase = container.accountBalanceUseCase
    private val creditCardBalanceUseCase = container.creditCardBalanceUseCase
    private val accountRepository = container.accountRepository
    private val creditCardRepository = container.creditCardRepository

    private val periodOnly = MutableStateFlow(DashboardPeriod.THIS_MONTH)
    private val customFrom = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val customTo = MutableStateFlow(LocalDate.now())

    private val periodStateFlow: kotlinx.coroutines.flow.Flow<PeriodState> =
        combine(periodOnly, customFrom, customTo) { period, from, to ->
            val range = computeRange(period, from, to)
            val previousRange = computePreviousRange(period, range)
            PeriodState(period, range, previousRange, periodLabel(period, from, to))
        }

    private val currentDataFlow = periodStateFlow.flatMapLatest { ps ->
        val aggregation = container.monthlyAggregationUseCase
        val txsFlow: kotlinx.coroutines.flow.Flow<List<TransactionEntity>> = if (ps.period == DashboardPeriod.THIS_MONTH) {
            aggregation.observeTransactionsForEffectiveMonth(YearMonth.from(ps.range.startAsLocalDate()))
        } else if (ps.period == DashboardPeriod.LAST_MONTH) {
            aggregation.observeTransactionsForEffectiveMonth(YearMonth.from(ps.range.startAsLocalDate()))
        } else {
            transactionRepository.observeFiltered(ps.range.start, ps.range.end, null, null, null, null)
        }

        val budgetsFlow = budgetUseCase.observeBudgetStatusForTransactions(txsFlow, accountRepository.observeAll())
        
        combine(txsFlow, budgetsFlow, namesFlow) { transactions, budgets, names ->
            val (accountMap, _) = names
            val expense = transactions.sumOf { tx ->
                when {
                    tx.transactionType == "EXPENSE" -> tx.amount
                    tx.transactionType == "TRANSFER" && tx.destinationAccountId != null && accountMap[tx.destinationAccountId]?.isOwnedBySelf == false -> tx.amount
                    else -> 0.0
                }
            }
            val income = transactions.sumOf { tx ->
                when {
                    tx.transactionType == "INCOME" -> tx.amount
                    tx.transactionType == "TRANSFER" && tx.sourceAccountId != null && accountMap[tx.sourceAccountId]?.isOwnedBySelf == false -> tx.amount
                    else -> 0.0
                }
            }
            val bySource = buildExpenseBySource(transactions)
            CurrentData(expense, income, budgets, bySource)
        }
    }

    private fun MonthRange.startAsLocalDate() = java.time.Instant.ofEpochMilli(start).atZone(java.time.ZoneId.systemDefault()).toLocalDate()

    private fun buildExpenseBySource(txs: List<TransactionEntity>): List<ExpenseBySourceRow> {
        return txs.filter { it.transactionType == "EXPENSE" }
            .groupBy { it.sourceAccountId to it.sourceCreditCardId }
            .map { (key, list) -> ExpenseBySourceRow(key.first, key.second, list.sumOf { it.amount }) }
    }

    private val prevDataFlow = periodStateFlow.flatMapLatest { ps ->
        combine(
            transactionRepository.observeTotalExpense(ps.previousRange.start, ps.previousRange.end),
            transactionRepository.observeTotalIncome(ps.previousRange.start, ps.previousRange.end)
        ) { expense, income -> PrevData(expense, income) }
    }

    private val staticTotalsFlow = combine(
        accountBalanceUseCase.observeCashTotal(),
        accountBalanceUseCase.observeBankTotal(),
        creditCardBalanceUseCase.observeActiveTotalUsed()
    ) { cash, bank, cc -> Triple(cash, bank, cc) }

    private val namesFlow = combine(accountRepository.observeAll(), creditCardRepository.observeAll()) { accounts, cards ->
        accounts.associateBy { it.id } to cards.associateBy { it.id }
    }

    private val showBudgetDetails = MutableStateFlow(false)
    private val showSourceDetails = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        periodStateFlow, 
        currentDataFlow, 
        prevDataFlow, 
        staticTotalsFlow, 
        namesFlow, 
        container.userPreferences.userNameFlow, 
        showBudgetDetails, 
        showSourceDetails
    ) { arr ->
        val ps = arr[0] as PeriodState
        val cur = arr[1] as CurrentData
        val prev = arr[2] as PrevData
        val totals = arr[3] as Triple<Double, Double, Double>
        val names = arr[4] as Pair<Map<Long, AccountEntity>, Map<Long, CreditCardEntity>>
        val userName = arr[5] as String
        val showBudget = arr[6] as Boolean
        val showSource = arr[7] as Boolean
        
        val (cash, bank, ccDebt) = totals
        val (accountMap, cardMap) = names
        DashboardUiState(
            userName = userName,
            period = ps.period,
            periodLabel = ps.label,
            totalExpense = cur.expense,
            totalIncome = cur.income,
            cashTotal = cash,
            bankTotal = bank,
            creditCardDebt = ccDebt,
            netBalance = cash + bank - ccDebt,
            budgetStatuses = cur.budgets,
            expensePieSlices = buildPieSlices(cur.bySource, accountMap, cardMap),
            previousExpense = prev.expense,
            previousIncome = prev.income,
            showBudgetDetails = showBudget,
            showSourceDetails = showSource
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    fun setUserName(name: String) {
        viewModelScope.launch { container.userPreferences.setUserName(name) }
    }

    fun toggleBudgetDetails() { showBudgetDetails.value = !showBudgetDetails.value }
    fun toggleSourceDetails() { showSourceDetails.value = !showSourceDetails.value }

    fun selectPeriod(period: DashboardPeriod) {
        periodOnly.value = period
    }

    fun selectCustomRange(from: LocalDate, to: LocalDate) {
        customFrom.value = from
        customTo.value = to
        periodOnly.value = DashboardPeriod.CUSTOM
    }

    private fun buildPieSlices(
        bySource: List<ExpenseBySourceRow>,
        accountMap: Map<Long, AccountEntity>,
        cardMap: Map<Long, CreditCardEntity>
    ): List<PieSlice> {
        return bySource
            .filter { it.total > 0 }
            .sortedByDescending { it.total }
            .mapIndexed { index, row ->
                val label = row.accountId?.let { accountMap[it]?.name }
                    ?: row.cardId?.let { cardMap[it]?.name }
                    ?: "อื่นๆ"
                PieSlice(label, row.total, ChartPalette[index % ChartPalette.size])
            }
    }

    private fun computeRange(period: DashboardPeriod, from: LocalDate, to: LocalDate): MonthRange = when (period) {
        DashboardPeriod.THIS_MONTH -> YearMonth.now().toEpochRange()
        DashboardPeriod.LAST_MONTH -> YearMonth.now().minusMonths(1).toEpochRange()
        DashboardPeriod.THIS_YEAR -> customRange(LocalDate.now().withDayOfYear(1), LocalDate.now().withMonth(12).withDayOfMonth(31))
        DashboardPeriod.CUSTOM -> customRange(from, to)
    }

    private fun computePreviousRange(period: DashboardPeriod, range: MonthRange): MonthRange = when (period) {
        DashboardPeriod.THIS_MONTH -> YearMonth.now().minusMonths(1).toEpochRange()
        DashboardPeriod.LAST_MONTH -> YearMonth.now().minusMonths(2).toEpochRange()
        DashboardPeriod.THIS_YEAR -> {
            val lastYear = LocalDate.now().minusYears(1)
            customRange(lastYear.withDayOfYear(1), lastYear.withMonth(12).withDayOfMonth(31))
        }
        DashboardPeriod.CUSTOM -> {
            val days = (range.end - range.start) / (24L * 60 * 60 * 1000)
            val prevTo = java.time.Instant.ofEpochMilli(range.start).atZone(java.time.ZoneId.systemDefault()).toLocalDate().minusDays(1)
            val prevFrom = prevTo.minusDays(days)
            customRange(prevFrom, prevTo)
        }
    }

    private fun periodLabel(period: DashboardPeriod, from: LocalDate, to: LocalDate): String = when (period) {
        DashboardPeriod.THIS_MONTH -> "เดือนนี้"
        DashboardPeriod.LAST_MONTH -> "เดือนก่อน"
        DashboardPeriod.THIS_YEAR -> "ปีนี้"
        DashboardPeriod.CUSTOM -> "${com.pft.tracker.ui.common.formatDate(from)} - ${com.pft.tracker.ui.common.formatDate(to)}"
    }
}
