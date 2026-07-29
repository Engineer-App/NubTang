package com.pft.tracker.ui.creditcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth

data class CreditCardRow(
    val card: CreditCardEntity,
    val currentUsed: Double,
    val availableLimit: Double,
    val effectiveLimit: Double,
    val groupMateNames: List<String>
)


data class CreditCardsUiState(
    val cards: List<CreditCardRow> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

class CreditCardsViewModel(private val container: AppContainer) : ViewModel() {
    private val month = YearMonth.now()

    val uiState: StateFlow<CreditCardsUiState> = combine(
        container.creditCardRepository.observeAll(),
        container.creditCardRepository.observeAllUsed(),
        container.creditLimitGroupRepository.observeAll(),
        container.monthlyAggregationUseCase.observeTransactionsForEffectiveMonth(month),
        container.accountRepository.observeAll()
    ) { cards, usedRows, groups, transactions, allAccounts ->
        val accountMap = allAccounts.associateBy { it.id }
        val income = transactions.sumOf { tx ->
            if (tx.transactionType == "INCOME" || (tx.transactionType == "TRANSFER" && tx.sourceAccountId != null && accountMap[tx.sourceAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
        }
        val expense = transactions.sumOf { tx ->
            if (tx.transactionType == "EXPENSE" || (tx.transactionType == "TRANSFER" && tx.destinationAccountId != null && accountMap[tx.destinationAccountId]?.isOwnedBySelf == false)) tx.amount else 0.0
        }

        val usedMap = usedRows.associateBy({ it.id }, { it.currentUsed })
        val groupMap = groups.associateBy { it.id }
        val groupUsedTotals = cards
            .filter { it.creditLimitGroupId != null }
            .groupBy { it.creditLimitGroupId }
            .mapValues { (_, cardsInGroup) -> cardsInGroup.sumOf { usedMap[it.id] ?: 0.0 } }

        val rows = cards.map { card ->
            val used = usedMap[card.id] ?: 0.0
            val groupId = card.creditLimitGroupId
            val effectiveLimit: Double
            val available: Double
            if (groupId == null) {
                effectiveLimit = card.creditLimit
                available = card.creditLimit - used
            } else {
                val group = groupMap[groupId]
                effectiveLimit = group?.sharedLimit ?: card.creditLimit
                available = effectiveLimit - (groupUsedTotals[groupId] ?: used)
            }
            val mates = if (groupId != null) {
                cards.filter { it.creditLimitGroupId == groupId && it.id != card.id }.map { it.name }
            } else emptyList()

            CreditCardRow(card, used, available, effectiveLimit, mates)
        }
        
        CreditCardsUiState(rows, income, expense)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreditCardsUiState())

    fun delete(card: com.pft.tracker.data.local.entity.CreditCardEntity) {
        viewModelScope.launch {
            container.creditCardRepository.delete(card)
        }
    }
}
