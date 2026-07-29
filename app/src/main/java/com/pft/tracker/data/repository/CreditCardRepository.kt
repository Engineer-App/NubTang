package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.CreditCardDao
import com.pft.tracker.data.local.dao.CreditCardUsedRow
import com.pft.tracker.data.local.entity.CreditCardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CreditCardRepository(private val dao: CreditCardDao) {
    fun observeAll(): Flow<List<CreditCardEntity>> = dao.observeAll()
    fun observeAllUsed(): Flow<List<CreditCardUsedRow>> = dao.observeAllUsed()
    suspend fun getAllOnce(): List<CreditCardEntity> = dao.observeAll().first()
    suspend fun insertAll(cards: List<CreditCardEntity>) = dao.insertAll(cards)
    suspend fun deleteAll() = dao.deleteAll()
    fun observeActive(): Flow<List<CreditCardEntity>> = dao.observeActive()
    fun observeById(id: Long): Flow<CreditCardEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): CreditCardEntity? = dao.getById(id)
    fun observeByGroup(groupId: Long): Flow<List<CreditCardEntity>> = dao.observeByGroup(groupId)
    suspend fun insert(card: CreditCardEntity): Long = dao.insert(card)
    suspend fun update(card: CreditCardEntity) = dao.update(card)
    suspend fun setActive(card: CreditCardEntity, isActive: Boolean) = dao.update(card.copy(isActive = isActive))
    suspend fun delete(card: CreditCardEntity) = dao.delete(card)
}
