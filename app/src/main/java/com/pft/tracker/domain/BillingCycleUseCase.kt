package com.pft.tracker.domain

import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.domain.model.StatementStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.min

/**
 * Implements doc §4.3: which billing cycle a card transaction belongs to.
 * Computed on-the-fly from statementDay/billingFrequencyMonths/startMonth —
 * deliberately not persisted, so editing a card's cutoff day never requires
 * migrating historical transactions (per the doc's own recommendation).
 */
class BillingCycleUseCase(private val zone: ZoneId = ZoneId.systemDefault()) {

    fun statementCutDate(card: CreditCardEntity, transactionDate: LocalDate): LocalDate {
        val cutoffDay = card.statementDay
        val thisMonthCutoff = transactionDate.withDayOfMonth(min(cutoffDay, transactionDate.lengthOfMonth()))
        val cutDate = if (transactionDate.dayOfMonth <= cutoffDay) {
            thisMonthCutoff
        } else {
            thisMonthCutoff.plusMonths(card.billingFrequencyMonths.toLong())
        }
        return alignToAnchor(card, cutDate)
    }

    private fun alignToAnchor(card: CreditCardEntity, cutDate: LocalDate): LocalDate {
        val freq = card.billingFrequencyMonths
        val anchor = card.startMonth
        if (freq <= 1 || anchor == null) return cutDate
        val offset = ((cutDate.monthValue - anchor) % freq + freq) % freq
        if (offset == 0) return cutDate
        val shifted = cutDate.plusMonths((freq - offset).toLong())
        val day = min(card.statementDay, shifted.lengthOfMonth())
        return shifted.withDayOfMonth(day)
    }

    fun periodStart(card: CreditCardEntity, cutDate: LocalDate): LocalDate =
        cutDate.minusMonths(card.billingFrequencyMonths.toLong()).plusDays(1)

    fun dueDate(card: CreditCardEntity, cutDate: LocalDate): LocalDate {
        val dueMonth = cutDate.plusMonths(1)
        val day = min(card.paymentDueDay, dueMonth.lengthOfMonth())
        return dueMonth.withDayOfMonth(day)
    }

    fun epochToLocalDate(epochMillis: Long): LocalDate =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()

    fun localDateToEpoch(date: LocalDate): Long =
        date.atStartOfDay(zone).toInstant().toEpochMilli()

    /**
     * Groups a card's EXPENSE transactions by billing cycle and derives a
     * paid/status summary from CREDIT_CARD_PAYMENT transactions against it.
     */
    fun computeStatements(
        card: CreditCardEntity,
        expenseTransactions: List<TransactionEntity>,
        paymentTransactions: List<TransactionEntity>,
        today: LocalDate = LocalDate.now(zone)
    ): List<StatementSummary> {
        val byCutDate = expenseTransactions.groupBy { statementCutDate(card, epochToLocalDate(it.transactionDate)) }

        return byCutDate.keys.sortedDescending().map { cutDate ->
            val due = dueDate(card, cutDate)
            val statementAmount = byCutDate[cutDate].orEmpty().sumOf { it.amount }
            // A payment counts against this statement if made after its cutoff
            // and on/before its due date.
            val paid = paymentTransactions.filter {
                val payDate = epochToLocalDate(it.transactionDate)
                payDate.isAfter(cutDate) && !payDate.isAfter(due)
            }.sumOf { it.amount }

            val status = when {
                !today.isAfter(cutDate) -> StatementStatus.NOT_YET_BILLED
                statementAmount > 0 && paid >= statementAmount -> StatementStatus.PAID
                paid > 0 -> if (today.isAfter(due)) StatementStatus.OVERDUE else StatementStatus.PARTIALLY_PAID
                today.isAfter(due) -> StatementStatus.OVERDUE
                else -> StatementStatus.BILLED
            }

            StatementSummary(
                cutDate = cutDate,
                periodStart = periodStart(card, cutDate),
                periodEnd = cutDate,
                dueDate = due,
                statementAmount = statementAmount,
                paidAmount = paid,
                status = status
            )
        }
    }
}

data class StatementSummary(
    val cutDate: LocalDate,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val dueDate: LocalDate,
    val statementAmount: Double,
    val paidAmount: Double,
    val status: StatementStatus
)
