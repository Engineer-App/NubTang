package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: CategoryEntity): Long

    @Insert
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY displayOrder ASC, name ASC")
    fun observeAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE categoryType = :type ORDER BY displayOrder ASC, name ASC")
    fun observeByType(type: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories WHERE id = :id")
    fun observeById(id: Long): Flow<CategoryEntity?>

    /**
     * Canonical spent(category, month) formula, per doc §4.4. Single source of truth.
     */
    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE transactionType = 'EXPENSE' AND categoryId = :categoryId
        AND transactionDate BETWEEN :monthStart AND :monthEnd
        """
    )
    fun observeSpent(categoryId: Long, monthStart: Long, monthEnd: Long): Flow<Double>

    @Query(
        """
        SELECT categoryId AS categoryId, COALESCE(SUM(amount), 0.0) AS spent FROM transactions
        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN :monthStart AND :monthEnd
        GROUP BY categoryId
        """
    )
    fun observeSpentByCategory(monthStart: Long, monthEnd: Long): Flow<List<CategorySpentRow>>
}

data class CategorySpentRow(
    val categoryId: Long?,
    val spent: Double
)
