package com.pft.tracker.backup

import com.pft.tracker.di.AppContainer
import com.pft.tracker.domain.BillingCycleUseCase
import com.pft.tracker.ui.common.formatDate

/**
 * Exports all transactions to CSV (doc §5.8 "ส่งออก CSV/Excel"). A CSV opens
 * cleanly in Excel; a true binary .xlsx would need a heavy extra library
 * (Apache POI) that isn't worth pulling in for one export button in v1.
 */
class CsvExporter(private val container: AppContainer) {

    suspend fun buildCsv(): String {
        val transactions = container.transactionRepository.getAllOnce()
        val accounts = container.accountRepository.getAllOnce().associateBy { it.id }
        val cards = container.creditCardRepository.getAllOnce().associateBy { it.id }
        val categories = container.categoryRepository.getAllOnce().associateBy { it.id }
        val billingCycleUseCase = BillingCycleUseCase()

        val sb = StringBuilder()
        sb.append("วันที่,ประเภท,รายการ,หมวดหมู่,จำนวนเงิน,ต้นทาง,ปลายทาง,หมายเหตุ\n")

        transactions.forEach { tx ->
            val date = formatDate(billingCycleUseCase.epochToLocalDate(tx.transactionDate))
            val source = tx.sourceAccountId?.let { accounts[it]?.name } ?: tx.sourceCreditCardId?.let { cards[it]?.name } ?: ""
            val destination = tx.destinationAccountId?.let { accounts[it]?.name } ?: tx.destinationCreditCardId?.let { cards[it]?.name } ?: ""
            val category = tx.categoryId?.let { categories[it]?.name } ?: ""
            sb.append(
                listOf(
                    date, tx.transactionType, tx.title, category,
                    tx.amount.toString(), source, destination, tx.note ?: ""
                ).joinToString(",") { csvEscape(it) }
            )
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun csvEscape(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}
