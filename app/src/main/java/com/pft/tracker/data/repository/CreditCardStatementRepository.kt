package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.CreditCardStatementDao
import com.pft.tracker.data.local.entity.CreditCardStatementEntity
import kotlinx.coroutines.flow.Flow

class CreditCardStatementRepository(private val dao: CreditCardStatementDao) {
    fun observeByCard(cardId: Long): Flow<List<CreditCardStatementEntity>> = dao.observeByCard(cardId)
    suspend fun insert(statement: CreditCardStatementEntity): Long = dao.insert(statement)
    suspend fun update(statement: CreditCardStatementEntity) = dao.update(statement)
    suspend fun insertAll(statements: List<CreditCardStatementEntity>) = dao.insertAll(statements)
    suspend fun deleteAll() = dao.deleteAll()
}
