package com.pft.tracker.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AccountDetailUiState(
    val account: AccountEntity? = null,
    val balance: Double = 0.0,
    val transactions: List<TransactionEntity> = emptyList()
)

class AccountDetailViewModel(container: AppContainer, accountId: Long) : ViewModel() {
    val uiState: StateFlow<AccountDetailUiState> = combine(
        container.accountRepository.observeById(accountId),
        container.accountBalanceUseCase.observeBalance(accountId),
        container.transactionRepository.observeByAccount(accountId)
    ) { account, balance, transactions ->
        AccountDetailUiState(account, balance, transactions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountDetailUiState())
}
