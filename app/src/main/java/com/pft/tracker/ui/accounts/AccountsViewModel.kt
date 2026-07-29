package com.pft.tracker.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.AccountWithBalance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth

data class AccountsUiState(
    val accounts: List<AccountWithBalance> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

class AccountsViewModel(private val container: AppContainer) : ViewModel() {
    private val month = YearMonth.now()

    val uiState: StateFlow<AccountsUiState> = combine(
        container.accountBalanceUseCase.observeAllWithBalance(),
        container.monthlyAggregationUseCase.observeTransactionsForEffectiveMonth(month),
        container.accountRepository.observeAll()
    ) { accounts, transactions, allAccounts ->
        val accountMap = allAccounts.associateBy { it.id }
        val income = transactions.sumOf { tx ->
            if (tx.transactionType == "INCOME" || (tx.transactionType == "TRANSFER" && tx.sourceAccountId != null && accountMap[tx.sourceAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
        }
        val expense = transactions.sumOf { tx ->
            if (tx.transactionType == "EXPENSE" || (tx.transactionType == "TRANSFER" && tx.destinationAccountId != null && accountMap[tx.destinationAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
        }
        AccountsUiState(accounts, income, expense)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountsUiState())

    fun delete(account: com.pft.tracker.data.local.entity.AccountEntity) {
        viewModelScope.launch {
            container.accountRepository.delete(account)
        }
    }
}
