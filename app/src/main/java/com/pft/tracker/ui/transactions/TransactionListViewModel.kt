package com.pft.tracker.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.model.toEpochRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

data class TransactionRow(
    val entity: TransactionEntity,
    val dateLabel: String,
    val sourceLabel: String,
    val categoryLabel: String?,
    val amountDisplay: String,
    val isPositive: Boolean
)

data class TransactionFilters(
    val type: String? = null,
    val categoryId: Long? = null,
    val accountId: Long? = null,
    val cardId: Long? = null
)

data class TransactionListUiState(
    val month: YearMonth = YearMonth.now(),
    val filters: TransactionFilters = TransactionFilters(),
    val rows: List<TransactionRow> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModel(private val container: AppContainer) : ViewModel() {

    private val monthFlow = MutableStateFlow(YearMonth.now())
    private val filtersFlow = MutableStateFlow(TransactionFilters())
    private val billingCycleUseCase = com.pft.tracker.domain.BillingCycleUseCase()

    private val namesFlow = combine(
        container.accountRepository.observeAll(),
        container.creditCardRepository.observeAll(),
        container.categoryRepository.observeAll()
    ) { accounts, cards, categories ->
        Triple(accounts.associateBy { it.id }, cards.associateBy { it.id }, categories.associateBy { it.id })
    }

    val uiState: StateFlow<TransactionListUiState> = combine(monthFlow, filtersFlow) { month, filters -> month to filters }
        .flatMapLatest { (month, filters) ->
            combine(
                container.monthlyAggregationUseCase.observeTransactionsForEffectiveMonth(month),
                namesFlow
            ) { transactions, names ->
                val (accountMap, cardMap, categoryMap) = names
                
                val filtered = transactions.filter { tx ->
                    (filters.type == null || tx.transactionType == filters.type) &&
                    (filters.categoryId == null || tx.categoryId == filters.categoryId) &&
                    (filters.accountId == null || tx.sourceAccountId == filters.accountId || tx.destinationAccountId == filters.accountId) &&
                    (filters.cardId == null || tx.sourceCreditCardId == filters.cardId || tx.destinationCreditCardId == filters.cardId)
                }.sortedByDescending { it.transactionDate }

                TransactionListUiState(
                    month = month,
                    filters = filters,
                    rows = filtered.map { tx ->
                        TransactionRow(
                            entity = tx,
                            dateLabel = com.pft.tracker.ui.common.formatDate(billingCycleUseCase.epochToLocalDate(tx.transactionDate)),
                            sourceLabel = sourceLabel(tx, accountMap, cardMap),
                            categoryLabel = tx.categoryId?.let { categoryMap[it]?.name },
                            amountDisplay = com.pft.tracker.ui.common.formatBaht(tx.amount),
                            isPositive = tx.transactionType == "INCOME" || (tx.transactionType == "TRANSFER" && tx.sourceAccountId != null && accountMap[tx.sourceAccountId]?.isOwnedBySelf == false)
                        )
                    },
                    totalIncome = transactions.sumOf { tx ->
                        if (tx.transactionType == "INCOME" || (tx.transactionType == "TRANSFER" && tx.sourceAccountId != null && accountMap[tx.sourceAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
                    },
                    totalExpense = transactions.sumOf { tx ->
                        if (tx.transactionType == "EXPENSE" || (tx.transactionType == "TRANSFER" && tx.destinationAccountId != null && accountMap[tx.destinationAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
                    }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionListUiState())

    private fun sourceLabel(
        tx: TransactionEntity,
        accountMap: Map<Long, com.pft.tracker.data.local.entity.AccountEntity>,
        cardMap: Map<Long, com.pft.tracker.data.local.entity.CreditCardEntity>
    ): String {
        val source = tx.sourceAccountId?.let { accountMap[it]?.name } ?: tx.sourceCreditCardId?.let { cardMap[it]?.name }
        val dest = tx.destinationAccountId?.let { accountMap[it]?.name } ?: tx.destinationCreditCardId?.let { cardMap[it]?.name }
        return when {
            source != null && dest != null -> "$source → $dest"
            source != null -> source
            dest != null -> dest
            else -> "-"
        }
    }

    fun setMonth(month: YearMonth) {
        monthFlow.value = month
    }

    fun setFilters(filters: TransactionFilters) {
        filtersFlow.value = filters
    }

    fun delete(row: TransactionRow) {
        viewModelScope.launch {
            container.transactionUseCase.delete(row.entity)
        }
    }
}
