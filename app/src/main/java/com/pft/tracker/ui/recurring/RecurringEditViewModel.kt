package com.pft.tracker.ui.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.model.CategoryType
import com.pft.tracker.domain.model.RecurringFrequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class RecurringEditUiState(
    val id: Long = 0,
    val name: String = "",
    val amountText: String = "",
    val categoryId: Long? = null,
    val sourceAccountId: Long? = null,
    val sourceCreditCardId: Long? = null,
    val startDate: LocalDate = LocalDate.now(),
    val frequency: RecurringFrequency = RecurringFrequency.MONTHLY,
    val hasFixedInstallments: Boolean = true,
    val totalInstallmentsText: String = "1",
    val note: String = "",
    val accounts: List<AccountEntity> = emptyList(),
    val cards: List<CreditCardEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val error: String? = null,
    val saved: Boolean = false,
    val nextRunDate: Long? = null,
    val installmentsGenerated: Int = 0,
    val isActive: Boolean = true
)

class RecurringEditViewModel(private val container: AppContainer, planId: Long) : ViewModel() {
    private val _uiState = MutableStateFlow(RecurringEditUiState(id = planId))
    val uiState: StateFlow<RecurringEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val accounts = container.accountRepository.observeActive().first()
            val cards = container.creditCardRepository.observeActive().first()
            val categories = container.categoryRepository.observeByType(CategoryType.EXPENSE.name).first()
            _uiState.value = _uiState.value.copy(accounts = accounts, cards = cards, categories = categories)

            if (planId != 0L) {
                container.recurringTransactionRepository.getById(planId)?.let { plan ->
                    _uiState.value = _uiState.value.copy(
                        name = plan.name,
                        amountText = plan.amount.toString(),
                        categoryId = plan.categoryId,
                        sourceAccountId = plan.sourceAccountId,
                        sourceCreditCardId = plan.sourceCreditCardId,
                        startDate = java.time.Instant.ofEpochMilli(plan.startDate).atZone(ZoneId.systemDefault()).toLocalDate(),
                        frequency = RecurringFrequency.valueOf(plan.frequency),
                        hasFixedInstallments = plan.totalInstallments != null,
                        totalInstallmentsText = (plan.totalInstallments ?: 1).toString(),
                        note = plan.note ?: "",
                        nextRunDate = plan.nextRunDate,
                        installmentsGenerated = plan.installmentsGenerated,
                        isActive = plan.isActive
                    )
                }
            }
        }
    }

    fun setName(v: String) = update { it.copy(name = v) }
    fun setAmountText(v: String) = update { it.copy(amountText = v.filter { c -> c.isDigit() || c == '.' }) }
    fun setCategory(v: Long?) = update { it.copy(categoryId = v) }
    fun setSource(accountId: Long?, cardId: Long?) = update { it.copy(sourceAccountId = accountId, sourceCreditCardId = cardId) }
    fun setStartDate(v: LocalDate) = update { it.copy(startDate = v) }
    fun setFrequency(v: RecurringFrequency) = update { it.copy(frequency = v) }
    fun setHasFixedInstallments(v: Boolean) = update { it.copy(hasFixedInstallments = v) }
    fun setTotalInstallmentsText(v: String) = update { it.copy(totalInstallmentsText = v.filter { c -> c.isDigit() }) }
    fun setNote(v: String) = update { it.copy(note = v) }

    private fun update(block: (RecurringEditUiState) -> RecurringEditUiState) {
        _uiState.value = block(_uiState.value).copy(error = null)
    }

    fun save() {
        val state = _uiState.value
        val amount = state.amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.value = state.copy(error = "กรุณากรอกจำนวนเงินให้ถูกต้อง")
            return
        }
        if (state.name.isBlank()) {
            _uiState.value = state.copy(error = "กรุณากรอกชื่อแผน")
            return
        }
        if (state.sourceAccountId == null && state.sourceCreditCardId == null) {
            _uiState.value = state.copy(error = "กรุณาเลือกบัญชีหรือบัตรที่ใช้จ่าย")
            return
        }

        val startEpoch = state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        var nextRun = state.nextRunDate ?: startEpoch
        
        if (state.id == 0L && state.frequency == RecurringFrequency.LAST_DAY_OF_MONTH) {
            val lastDay = state.startDate.withDayOfMonth(state.startDate.lengthOfMonth())
            nextRun = lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        val totalInstallments = if (state.hasFixedInstallments) state.totalInstallmentsText.toIntOrNull() ?: 1 else null

        viewModelScope.launch {
            val entity = RecurringTransactionEntity(
                id = state.id,
                name = state.name,
                amount = amount,
                categoryId = state.categoryId,
                sourceAccountId = state.sourceAccountId,
                sourceCreditCardId = state.sourceCreditCardId,
                startDate = startEpoch,
                frequency = state.frequency.name,
                totalInstallments = totalInstallments,
                installmentsGenerated = state.installmentsGenerated,
                nextRunDate = nextRun,
                note = state.note.ifBlank { null },
                isActive = state.isActive
            )
            if (state.id == 0L) {
                container.recurringTransactionRepository.insert(entity)
            } else {
                container.recurringTransactionRepository.update(entity)
            }
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }
}
