package com.pft.tracker.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AccountEditUiState(
    val id: Long = 0,
    val name: String = "",
    val accountType: String = "CASH",
    val bankName: String = "",
    val last4: String = "",
    val openingBalanceText: String = "0",
    val isActive: Boolean = true,
    val isOwnedBySelf: Boolean = true,
    val error: String? = null,
    val saved: Boolean = false
)

class AccountEditViewModel(private val container: AppContainer, accountId: Long) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountEditUiState(id = accountId))
    val uiState: StateFlow<AccountEditUiState> = _uiState.asStateFlow()

    init {
        if (accountId != 0L) {
            viewModelScope.launch {
                container.accountRepository.getById(accountId)?.let { account ->
                    _uiState.value = AccountEditUiState(
                        id = account.id,
                        name = account.name,
                        accountType = account.accountType,
                        bankName = account.bankName ?: "",
                        last4 = account.accountNumberLast4 ?: "",
                        openingBalanceText = account.openingBalance.toString(),
                        isActive = account.isActive,
                        isOwnedBySelf = account.isOwnedBySelf
                    )
                }
            }
        }
    }

    fun setName(v: String) = update { it.copy(name = v) }
    fun setAccountType(v: String) = update { it.copy(accountType = v) }
    fun setBankName(v: String) = update { it.copy(bankName = v) }
    fun setLast4(v: String) = update { it.copy(last4 = v.filter { c -> c.isDigit() }.take(4)) }
    fun setOpeningBalanceText(v: String) = update { it.copy(openingBalanceText = v.filter { c -> c.isDigit() || c == '.' }) }
    fun setActive(v: Boolean) = update { it.copy(isActive = v) }
    fun setIsOwnedBySelf(v: Boolean) = update { it.copy(isOwnedBySelf = v) }

    private fun update(block: (AccountEditUiState) -> AccountEditUiState) {
        _uiState.value = block(_uiState.value).copy(error = null)
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.value = state.copy(error = "กรุณากรอกชื่อบัญชี")
            return
        }
        val openingBalance = state.openingBalanceText.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            val entity = AccountEntity(
                id = state.id,
                name = state.name,
                accountType = state.accountType,
                bankName = state.bankName.ifBlank { null },
                accountNumberLast4 = state.last4.ifBlank { null },
                openingBalance = openingBalance,
                isActive = state.isActive,
                isOwnedBySelf = state.isOwnedBySelf
            )
            if (state.id == 0L) {
                container.accountRepository.insert(entity)
            } else {
                container.accountRepository.update(entity)
            }
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }
}
