package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.CreditCardStatementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardStatementDao {

    @Insert
    suspend fun insert(statement: CreditCardStatementEntity): Long

    @Insert
    suspend fun insertAll(statements: List<CreditCardStatementEntity>)

    @Query("DELETE FROM credit_card_statements")
    suspend fun deleteAll()

    @Update
    suspend fun update(statement: CreditCardStatementEntity)

    @Delete
    suspend fun delete(statement: CreditCardStatementEntity)

    @Query("SELECT * FROM credit_card_statements WHERE creditCardId = :cardId ORDER BY periodStart DESC")
    fun observeByCard(cardId: Long): Flow<List<CreditCardStatementEntity>>

    @Query("SELECT * FROM credit_card_statements WHERE id = :id")
    suspend fun getById(id: Long): CreditCardStatementEntity?

    @Query("SELECT * FROM credit_card_statements WHERE creditCardId = :cardId AND periodStart = :periodStart AND periodEnd = :periodEnd LIMIT 1")
    suspend fun findByPeriod(cardId: Long, periodStart: Long, periodEnd: Long): CreditCardStatementEntity?
}
