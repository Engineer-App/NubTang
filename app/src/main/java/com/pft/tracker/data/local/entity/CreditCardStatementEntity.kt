package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "credit_card_statements")
data class CreditCardStatementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creditCardId: Long,
    val periodStart: Long,
    val periodEnd: Long,
    val statementDate: Long,
    val dueDate: Long,
    val statementAmount: Double,
    val paidAmount: Double = 0.0,
    val status: String // NOT_YET_BILLED / BILLED / PARTIALLY_PAID / PAID / OVERDUE
)
