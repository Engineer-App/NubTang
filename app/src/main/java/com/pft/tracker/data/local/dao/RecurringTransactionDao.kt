package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Insert
    suspend fun insert(plan: RecurringTransactionEntity): Long

    @Insert
    suspend fun insertAll(plans: List<RecurringTransactionEntity>)

    @Query("DELETE FROM recurring_transactions")
    suspend fun deleteAll()

    @Update
    suspend fun update(plan: RecurringTransactionEntity)

    @Delete
    suspend fun delete(plan: RecurringTransactionEntity)

    @Query("SELECT * FROM recurring_transactions ORDER BY isActive DESC, nextRunDate ASC")
    fun observeAll(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getById(id: Long): RecurringTransactionEntity?

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    fun observeById(id: Long): Flow<RecurringTransactionEntity?>

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextRunDate <= :today")
    suspend fun getDue(today: Long): List<RecurringTransactionEntity>
}
