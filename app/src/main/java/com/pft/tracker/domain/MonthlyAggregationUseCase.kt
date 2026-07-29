package com.pft.tracker.domain

import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.data.repository.CreditCardRepository
import com.pft.tracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

/**
 * Implements the "Effective Month" logic.
 * Transactions on a credit card are assigned to the month of their billing cycle cutoff.
 * Other transactions are assigned to the month they occurred.
 */
class MonthlyAggregationUseCase(
    private val transactionRepository: TransactionRepository,
    private val creditCardRepository: CreditCardRepository,
    private val billingCycleUseCase: BillingCycleUseCase = BillingCycleUseCase()
) {
    private val zone = ZoneId.systemDefault()

    fun observeTransactionsForEffectiveMonth(month: YearMonth): Flow<List<TransactionEntity>> {
        // To be safe and catch all card cycles, we fetch from 2 months before to 1 month after
        val start = month.minusMonths(1).atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = month.plusMonths(1).atEndOfMonth().atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()

        return combine(
            transactionRepository.getAllFilteredFlow(start, end),
            creditCardRepository.observeAll()
        ) { transactions, cards ->
            val cardMap = cards.associateBy { it.id }
            transactions.filter { tx ->
                getEffectiveMonth(tx, cardMap) == month
            }
        }
    }

    private fun getEffectiveMonth(tx: TransactionEntity, cardMap: Map<Long, CreditCardEntity>): YearMonth {
        val date = billingCycleUseCase.epochToLocalDate(tx.transactionDate)
        
        // Only card expenses use billing cycle for month assignment
        val cardId = tx.sourceCreditCardId ?: tx.destinationCreditCardId
        if (cardId != null) {
            val card = cardMap[cardId]
            if (card != null) {
                val cutDate = billingCycleUseCase.statementCutDate(card, date)
                return YearMonth.from(cutDate)
            }
        }
        
        return YearMonth.from(date)
    }
}

// Add extension to TransactionRepository if needed, or just use existing observation
// Assuming TransactionRepository already has a way to get a wide range
private fun TransactionRepository.getAllFilteredFlow(start: Long, end: Long): Flow<List<TransactionEntity>> =
    observeFiltered(start, end, null, null, null, null)
