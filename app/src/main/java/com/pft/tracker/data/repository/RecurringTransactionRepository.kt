package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.RecurringTransactionDao
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RecurringTransactionRepository(private val dao: RecurringTransactionDao) {
    fun observeAll(): Flow<List<RecurringTransactionEntity>> = dao.observeAll()
    suspend fun getAllOnce(): List<RecurringTransactionEntity> = dao.observeAll().first()
    suspend fun insertAll(plans: List<RecurringTransactionEntity>) = dao.insertAll(plans)
    suspend fun deleteAll() = dao.deleteAll()
    fun observeById(id: Long): Flow<RecurringTransactionEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): RecurringTransactionEntity? = dao.getById(id)
    suspend fun insert(plan: RecurringTransactionEntity): Long = dao.insert(plan)
    suspend fun update(plan: RecurringTransactionEntity) = dao.update(plan)
    suspend fun delete(plan: RecurringTransactionEntity) = dao.delete(plan)
    suspend fun getDue(today: Long): List<RecurringTransactionEntity> = dao.getDue(today)
}
