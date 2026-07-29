package com.pft.tracker.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.model.CategoryType
import com.pft.tracker.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class AddEditTransactionUiState(
    val isEditing: Boolean = false,
    val type: TransactionType = TransactionType.EXPENSE,
    val date: LocalDate = LocalDate.now(),
    val title: String = "",
    val categoryId: Long? = null,
    val amountText: String = "",
    val sourceAccountId: Long? = null,
    val sourceCreditCardId: Long? = null,
    val destinationAccountId: Long? = null,
    val destinationCreditCardId: Long? = null,
    val note: String = "",
    val receiptPath: String? = null,
    val accounts: List<AccountEntity> = emptyList(),
    val cards: List<CreditCardEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val error: String? = null,
    val saved: Boolean = false,
    val savedEffectiveMonth: String? = null, // e.g. "สิงหาคม 2569"
    val deleted: Boolean = false,
    val isReadOnly: Boolean = false
)

class AddEditTransactionViewModel(
    private val container: AppContainer,
    private val transactionId: Long,
    initialType: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddEditTransactionUiState(type = TransactionType.valueOf(initialType))
    )
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val accounts = container.accountRepository.observeActive().first()
            val cards = container.creditCardRepository.observeActive().first()
            val categories = container.categoryRepository.observeAll().first()
            _uiState.value = _uiState.value.copy(accounts = accounts, cards = cards, categories = categories)

            if (transactionId != 0L) {
                val existing = container.transactionRepository.getById(transactionId)
                if (existing != null) {
                    _uiState.value = _uiState.value.copy(
                        isEditing = true,
                        isReadOnly = true,
                        type = TransactionType.valueOf(existing.transactionType),
                        date = Instant.ofEpochMilli(existing.transactionDate).atZone(ZoneId.systemDefault()).toLocalDate(),
                        title = existing.title,
                        categoryId = existing.categoryId,
                        amountText = existing.amount.toString(),
                        sourceAccountId = existing.sourceAccountId,
                        sourceCreditCardId = existing.sourceCreditCardId,
                        destinationAccountId = existing.destinationAccountId,
                        destinationCreditCardId = existing.destinationCreditCardId,
                        note = existing.note ?: "",
                        receiptPath = existing.receiptPath
                    )
                }
            }
        }
    }

    fun setDate(date: LocalDate) = update { it.copy(date = date) }
    fun setTitle(title: String) = update { it.copy(title = title) }
    fun setCategory(id: Long?) = update { it.copy(categoryId = id) }
    fun setAmountText(text: String) = update { it.copy(amountText = text.filter { c -> c.isDigit() || c == '.' }) }
    fun setNote(note: String) = update { it.copy(note = note) }
    fun setReceiptPath(path: String?) = update { it.copy(receiptPath = path) }
    fun setReadOnly(v: Boolean) = update { it.copy(isReadOnly = v) }

    fun setSource(accountId: Long?, cardId: Long?) = update { it.copy(sourceAccountId = accountId, sourceCreditCardId = cardId) }
    fun setDestination(accountId: Long?, cardId: Long?) = update { it.copy(destinationAccountId = accountId, destinationCreditCardId = cardId) }

    private fun update(block: (AddEditTransactionUiState) -> AddEditTransactionUiState) {
        _uiState.value = block(_uiState.value).copy(error = null)
    }

    fun save() {
        val state = _uiState.value
        val amount = state.amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.value = state.copy(error = "กรุณากรอกจำนวนเงินให้ถูกต้อง")
            return
        }
        if (state.title.isBlank()) {
            _uiState.value = state.copy(error = "กรุณากรอกชื่อรายการ")
            return
        }

        val entity = TransactionEntity(
            id = if (state.isEditing) transactionId else 0,
            transactionDate = state.date.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            transactionType = state.type.name,
            title = state.title,
            categoryId = if (state.type == TransactionType.EXPENSE || state.type == TransactionType.INCOME || state.type == TransactionType.TRANSFER) state.categoryId else null,
            amount = amount,
            sourceAccountId = state.sourceAccountId,
            sourceCreditCardId = state.sourceCreditCardId,
            destinationAccountId = state.destinationAccountId,
            destinationCreditCardId = state.destinationCreditCardId,
            note = state.note.ifBlank { null },
            receiptPath = state.receiptPath
        )

        viewModelScope.launch {
            val card = if (state.sourceCreditCardId != null) {
                container.creditCardRepository.getById(state.sourceCreditCardId)
            } else if (state.destinationCreditCardId != null) {
                container.creditCardRepository.getById(state.destinationCreditCardId)
            } else null
            
            val billingCycle = com.pft.tracker.domain.BillingCycleUseCase()
            val effectiveDate = if (card != null) billingCycle.statementCutDate(card, state.date) else state.date
            val effectiveMonthStr = if (effectiveDate.month != state.date.month) {
                com.pft.tracker.ui.common.monthLabel(effectiveDate.year, effectiveDate.monthValue)
            } else null

            val result = if (state.isEditing) {
                container.transactionUseCase.update(entity)
            } else {
                container.transactionUseCase.add(entity)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(saved = true, savedEffectiveMonth = effectiveMonthStr)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e.message ?: "บันทึกไม่สำเร็จ")
            }
        }
    }

    fun delete() {
        if (!_uiState.value.isEditing) return
        viewModelScope.launch {
            val existing = container.transactionRepository.getById(transactionId) ?: return@launch
            container.transactionUseCase.delete(existing).onSuccess {
                _uiState.value = _uiState.value.copy(deleted = true)
            }
        }
    }
}
