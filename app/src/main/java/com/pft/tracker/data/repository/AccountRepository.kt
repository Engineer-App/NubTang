package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.AccountDao
import com.pft.tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AccountRepository(private val dao: AccountDao) {
    fun observeAll(): Flow<List<AccountEntity>> = dao.observeAll()
    fun observeActive(): Flow<List<AccountEntity>> = dao.observeActive()
    fun observeById(id: Long): Flow<AccountEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): AccountEntity? = dao.getById(id)
    suspend fun insert(account: AccountEntity): Long = dao.insert(account)
    suspend fun update(account: AccountEntity) = dao.update(account)
    suspend fun setActive(account: AccountEntity, isActive: Boolean) = dao.update(account.copy(isActive = isActive))
    suspend fun delete(account: AccountEntity) = dao.delete(account)
    suspend fun getAllOnce(): List<AccountEntity> = dao.observeAll().first()
    suspend fun insertAll(accounts: List<AccountEntity>) = dao.insertAll(accounts)
    suspend fun deleteAll() = dao.deleteAll()
}
