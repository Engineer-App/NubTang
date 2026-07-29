package com.pft.tracker.ui.creditcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.StatementSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

data class CreditCardDetailUiState(
    val card: CreditCardEntity? = null,
    val currentUsed: Double = 0.0,
    val availableLimit: Double = 0.0,
    val statements: List<StatementSummary> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
class CreditCardDetailViewModel(container: AppContainer, cardId: Long) : ViewModel() {

    private val cardFlow = container.creditCardRepository.observeById(cardId)
    private val transactionsFlow = container.transactionRepository.observeByCard(cardId)

    private val availableLimitFlow = cardFlow.flatMapLatest { card ->
        if (card == null) flowOf(0.0) else container.creditCardBalanceUseCase.observeAvailableLimit(card)
    }

    val uiState: StateFlow<CreditCardDetailUiState> = combine(
        cardFlow, transactionsFlow, availableLimitFlow
    ) { card, transactions, availableLimit ->
        if (card == null) {
            CreditCardDetailUiState()
        } else {
            val expenses = transactions.filter { it.transactionType == "EXPENSE" && it.sourceCreditCardId == cardId }
            val payments = transactions.filter { it.transactionType == "CREDIT_CARD_PAYMENT" && it.destinationCreditCardId == cardId }
            val statements = container.billingCycleUseCase.computeStatements(card, expenses, payments)
            val used = expenses.sumOf { it.amount } - payments.sumOf { it.amount }
            CreditCardDetailUiState(card, used, availableLimit, statements)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreditCardDetailUiState())
}
