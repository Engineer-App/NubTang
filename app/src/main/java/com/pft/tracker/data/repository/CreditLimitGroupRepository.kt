package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.CreditLimitGroupDao
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CreditLimitGroupRepository(private val dao: CreditLimitGroupDao) {
    fun observeAll(): Flow<List<CreditLimitGroupEntity>> = dao.observeAll()
    suspend fun getAllOnce(): List<CreditLimitGroupEntity> = dao.observeAll().first()
    suspend fun insertAll(groups: List<CreditLimitGroupEntity>) = dao.insertAll(groups)
    suspend fun deleteAll() = dao.deleteAll()
    fun observeById(id: Long): Flow<CreditLimitGroupEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): CreditLimitGroupEntity? = dao.getById(id)
    suspend fun insert(group: CreditLimitGroupEntity): Long = dao.insert(group)
    suspend fun update(group: CreditLimitGroupEntity) = dao.update(group)
    suspend fun delete(group: CreditLimitGroupEntity) = dao.delete(group)
}
