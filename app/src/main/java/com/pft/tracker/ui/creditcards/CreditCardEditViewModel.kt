package com.pft.tracker.ui.creditcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CreditCardEditUiState(
    val id: Long = 0,
    val name: String = "",
    val last4: String = "",
    val creditLimitText: String = "0",
    val groupId: Long? = null,
    val billingFrequencyMonths: Int = 1,
    val statementDayText: String = "1",
    val paymentDueDayText: String = "1",
    val startMonth: Int? = null,
    val isActive: Boolean = true,
    val groups: List<CreditLimitGroupEntity> = emptyList(),
    val error: String? = null,
    val saved: Boolean = false
)

class CreditCardEditViewModel(private val container: AppContainer, cardId: Long) : ViewModel() {
    private val _uiState = MutableStateFlow(CreditCardEditUiState(id = cardId))
    val uiState: StateFlow<CreditCardEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val groups = container.creditLimitGroupRepository.observeAll().first()
            _uiState.value = _uiState.value.copy(groups = groups)
            if (cardId != 0L) {
                container.creditCardRepository.getById(cardId)?.let { card ->
                    _uiState.value = _uiState.value.copy(
                        id = card.id,
                        name = card.name,
                        last4 = card.cardNumberLast4,
                        creditLimitText = card.creditLimit.toString(),
                        groupId = card.creditLimitGroupId,
                        billingFrequencyMonths = card.billingFrequencyMonths,
                        statementDayText = card.statementDay.toString(),
                        paymentDueDayText = card.paymentDueDay.toString(),
                        startMonth = card.startMonth,
                        isActive = card.isActive
                    )
                }
            }
        }
    }

    fun setName(v: String) = update { it.copy(name = v) }
    fun setLast4(v: String) = update { it.copy(last4 = v.filter { c -> c.isDigit() }.take(4)) }
    fun setCreditLimitText(v: String) = update { it.copy(creditLimitText = v.filter { c -> c.isDigit() || c == '.' }) }
    fun setGroupId(v: Long?) = update { it.copy(groupId = v) }
    fun setBillingFrequencyMonths(v: Int) = update { it.copy(billingFrequencyMonths = v) }
    fun setStatementDayText(v: String) = update { it.copy(statementDayText = v.filter { it.isDigit() }) }
    fun setPaymentDueDayText(v: String) = update { it.copy(paymentDueDayText = v.filter { it.isDigit() }) }
    fun setStartMonth(v: Int?) = update { it.copy(startMonth = v) }
    fun setActive(v: Boolean) = update { it.copy(isActive = v) }

    private fun update(block: (CreditCardEditUiState) -> CreditCardEditUiState) {
        _uiState.value = block(_uiState.value).copy(error = null)
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.value = state.copy(error = "กรุณากรอกชื่อบัตร")
            return
        }
        val limit = state.creditLimitText.toDoubleOrNull() ?: 0.0
        val statementDay = state.statementDayText.toIntOrNull()?.coerceIn(1, 31) ?: 1
        val paymentDueDay = state.paymentDueDayText.toIntOrNull()?.coerceIn(1, 31) ?: 1

        viewModelScope.launch {
            val entity = CreditCardEntity(
                id = state.id,
                name = state.name,
                issuer = "", // Simplified, or could remove from DB later
                cardNumberLast4 = state.last4,
                creditLimit = limit,
                creditLimitGroupId = state.groupId,
                billingFrequencyMonths = state.billingFrequencyMonths,
                statementDay = statementDay,
                paymentDueDay = paymentDueDay,
                startMonth = if (state.billingFrequencyMonths > 1) state.startMonth else null,
                isActive = state.isActive
            )
            if (state.id == 0L) {
                container.creditCardRepository.insert(entity)
            } else {
                container.creditCardRepository.update(entity)
            }
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }
}
