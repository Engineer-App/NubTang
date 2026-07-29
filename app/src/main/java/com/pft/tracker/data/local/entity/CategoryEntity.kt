package com.pft.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryType: String, // "EXPENSE" | "INCOME"
    val parentCategoryId: Long? = null,
    val icon: String? = null,
    val monthlyBudget: Double? = null, // null or 0 = no budget limit
    val displayOrder: Int = 0
)
