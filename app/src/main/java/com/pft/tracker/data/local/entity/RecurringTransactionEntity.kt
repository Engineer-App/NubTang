package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    val categoryId: Long? = null,
    val sourceAccountId: Long? = null,
    val sourceCreditCardId: Long? = null,
    val startDate: Long,
    val frequency: String, // MONTHLY / WEEKLY / YEARLY / ONE_TIME
    val totalInstallments: Int? = null,
    val installmentsGenerated: Int = 0,
    val nextRunDate: Long,
    val endDate: Long? = null,
    val note: String? = null,
    val isActive: Boolean = true
)
