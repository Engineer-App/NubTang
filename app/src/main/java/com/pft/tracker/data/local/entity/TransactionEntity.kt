package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionDate: Long, // epoch millis
    val transactionType: String, // EXPENSE / INCOME / TRANSFER / CASH_WITHDRAWAL / CREDIT_CARD_PAYMENT
    val title: String,
    val categoryId: Long? = null,
    val amount: Double,
    val sourceAccountId: Long? = null,
    val sourceCreditCardId: Long? = null,
    val destinationAccountId: Long? = null,
    val destinationCreditCardId: Long? = null,
    val note: String? = null,
    val receiptPath: String? = null,
    val isRecurringGenerated: Boolean = false,
    val recurringPlanId: Long? = null,
    val creditCardStatementId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
