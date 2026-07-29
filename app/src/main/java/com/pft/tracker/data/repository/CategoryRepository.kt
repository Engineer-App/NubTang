package com.pft.tracker.data.repository

import com.pft.tracker.data.local.dao.CategoryDao
import com.pft.tracker.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CategoryRepository(private val dao: CategoryDao) {
    fun observeAll(): Flow<List<CategoryEntity>> = dao.observeAll()
    suspend fun getAllOnce(): List<CategoryEntity> = dao.observeAll().first()
    suspend fun insertAll(categories: List<CategoryEntity>) = dao.insertAll(categories)
    suspend fun deleteAll() = dao.deleteAll()
    fun observeByType(type: String): Flow<List<CategoryEntity>> = dao.observeByType(type)
    fun observeById(id: Long): Flow<CategoryEntity?> = dao.observeById(id)
    suspend fun getById(id: Long): CategoryEntity? = dao.getById(id)
    suspend fun insert(category: CategoryEntity): Long = dao.insert(category)
    suspend fun update(category: CategoryEntity) = dao.update(category)
    suspend fun delete(category: CategoryEntity) = dao.delete(category)
}
