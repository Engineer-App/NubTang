package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.ExpenseBySourceRow
import com.pft.tracker.data.local.dao.TransactionDao
import com.pft.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {
    fun observeById(id: Long): Flow<TransactionEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): TransactionEntity? = dao.getById(id)

    fun observeFiltered(
        monthStart: Long,
        monthEnd: Long,
        type: String?,
        categoryId: Long?,
        accountId: Long?,
        cardId: Long?
    ): Flow<List<TransactionEntity>> =
        dao.observeFiltered(monthStart, monthEnd, type, categoryId, accountId, cardId)

    fun observeByAccount(accountId: Long): Flow<List<TransactionEntity>> = dao.observeByAccount(accountId)
    fun observeByCard(cardId: Long): Flow<List<TransactionEntity>> = dao.observeByCard(cardId)

    suspend fun getCardExpensesInPeriod(cardId: Long, periodStart: Long, periodEnd: Long): List<TransactionEntity> =
        dao.getCardExpensesInPeriod(cardId, periodStart, periodEnd)

    fun observeExpenseTotalsBySource(monthStart: Long, monthEnd: Long): Flow<List<ExpenseBySourceRow>> =
        dao.observeExpenseTotalsBySource(monthStart, monthEnd)

    fun observeTotalExpense(monthStart: Long, monthEnd: Long): Flow<Double> = dao.observeTotalExpense(monthStart, monthEnd)
    fun observeTotalIncome(monthStart: Long, monthEnd: Long): Flow<Double> = dao.observeTotalIncome(monthStart, monthEnd)

    suspend fun getAllOnce(): List<TransactionEntity> = dao.getAllOnce()
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun deleteInRange(start: Long, end: Long) = dao.deleteInRange(start, end)
    suspend fun insertRaw(transaction: TransactionEntity): Long = dao.insert(transaction)
    suspend fun insertAll(transactions: List<TransactionEntity>) = dao.insertAll(transactions)
}
