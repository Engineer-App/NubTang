package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val issuer: String,
    val cardNumberLast4: String,
    val creditLimit: Double,
    val creditLimitGroupId: Long? = null,
    val billingFrequencyMonths: Int = 1,
    val statementDay: Int,
    val paymentDueDay: Int,
    val startMonth: Int? = null,
    val isActive: Boolean = true
)
