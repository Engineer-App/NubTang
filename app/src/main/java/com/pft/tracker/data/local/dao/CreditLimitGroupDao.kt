package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditLimitGroupDao {

    @Insert
    suspend fun insert(group: CreditLimitGroupEntity): Long

    @Insert
    suspend fun insertAll(groups: List<CreditLimitGroupEntity>)

    @Query("DELETE FROM credit_limit_groups")
    suspend fun deleteAll()

    @Update
    suspend fun update(group: CreditLimitGroupEntity)

    @Delete
    suspend fun delete(group: CreditLimitGroupEntity)

    @Query("SELECT * FROM credit_limit_groups ORDER BY name ASC")
    fun observeAll(): Flow<List<CreditLimitGroupEntity>>

    @Query("SELECT * FROM credit_limit_groups WHERE id = :id")
    suspend fun getById(id: Long): CreditLimitGroupEntity?

    @Query("SELECT * FROM credit_limit_groups WHERE id = :id")
    fun observeById(id: Long): Flow<CreditLimitGroupEntity?>
}
