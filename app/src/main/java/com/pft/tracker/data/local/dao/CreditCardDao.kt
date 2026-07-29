package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.CreditCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {

    @Insert
    suspend fun insert(card: CreditCardEntity): Long

    @Insert
    suspend fun insertAll(cards: List<CreditCardEntity>)

    @Query("DELETE FROM credit_cards")
    suspend fun deleteAll()

    @Update
    suspend fun update(card: CreditCardEntity)

    @Delete
    suspend fun delete(card: CreditCardEntity)

    @Query("SELECT * FROM credit_cards ORDER BY isActive DESC, name ASC")
    fun observeAll(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE isActive = 1 ORDER BY name ASC")
    fun observeActive(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE id = :id")
    suspend fun getById(id: Long): CreditCardEntity?

    @Query("SELECT * FROM credit_cards WHERE id = :id")
    fun observeById(id: Long): Flow<CreditCardEntity?>

    @Query("SELECT * FROM credit_cards WHERE creditLimitGroupId = :groupId")
    fun observeByGroup(groupId: Long): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE creditLimitGroupId = :groupId")
    suspend fun getByGroup(groupId: Long): List<CreditCardEntity>

    /**
     * Canonical currentUsed(card) formula, per doc §4.2. Single source of truth.
     */
    @Query(
        """
        SELECT :cardId AS id, (
            COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = :cardId), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'CREDIT_CARD_PAYMENT' AND destinationCreditCardId = :cardId), 0.0)
        ) AS currentUsed
        """
    )
    fun observeUsed(cardId: Long): Flow<CreditCardUsedRow?>

    @Query(
        """
        SELECT c.id AS id, (
            COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = c.id), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'CREDIT_CARD_PAYMENT' AND destinationCreditCardId = c.id), 0.0)
        ) AS currentUsed
        FROM credit_cards c
        """
    )
    fun observeAllUsed(): Flow<List<CreditCardUsedRow>>
}

data class CreditCardUsedRow(
    val id: Long,
    val currentUsed: Double
)
