package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val accountType: String, // "CASH" | "BANK"
    val bankName: String? = null,
    val accountNumberLast4: String? = null,
    val openingBalance: Double,
    val isActive: Boolean = true,
    val isOwnedBySelf: Boolean = true,
    val creditLimitGroupId: Long? = null, // unused for accounts, kept for schema parity, always null
    val createdAt: Long = System.currentTimeMillis()
)
