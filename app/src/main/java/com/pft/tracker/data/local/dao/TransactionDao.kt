package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun observeById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC, id DESC")
    suspend fun getAllOnce(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("DELETE FROM transactions WHERE transactionDate BETWEEN :start AND :end")
    suspend fun deleteInRange(start: Long, end: Long)

    @Query(
        """
        SELECT * FROM transactions
        WHERE transactionDate BETWEEN :monthStart AND :monthEnd
        AND (:type IS NULL OR transactionType = :type)
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        AND (:accountId IS NULL OR sourceAccountId = :accountId OR destinationAccountId = :accountId)
        AND (:cardId IS NULL OR sourceCreditCardId = :cardId OR destinationCreditCardId = :cardId)
        ORDER BY transactionDate DESC, id DESC
        """
    )
    fun observeFiltered(
        monthStart: Long,
        monthEnd: Long,
        type: String?,
        categoryId: Long?,
        accountId: Long?,
        cardId: Long?
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE sourceAccountId = :accountId OR destinationAccountId = :accountId ORDER BY transactionDate DESC, id DESC")
    fun observeByAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE sourceCreditCardId = :cardId OR destinationCreditCardId = :cardId ORDER BY transactionDate DESC, id DESC")
    fun observeByCard(cardId: Long): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions
        WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = :cardId
        AND transactionDate BETWEEN :periodStart AND :periodEnd
        ORDER BY transactionDate ASC, id ASC
        """
    )
    suspend fun getCardExpensesInPeriod(cardId: Long, periodStart: Long, periodEnd: Long): List<TransactionEntity>

    @Query(
        """
        SELECT sourceAccountId AS accountId, sourceCreditCardId AS cardId, COALESCE(SUM(amount), 0.0) AS total
        FROM transactions
        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN :monthStart AND :monthEnd
        GROUP BY sourceAccountId, sourceCreditCardId
        """
    )
    fun observeExpenseTotalsBySource(monthStart: Long, monthEnd: Long): Flow<List<ExpenseBySourceRow>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN :monthStart AND :monthEnd
        """
    )
    fun observeTotalExpense(monthStart: Long, monthEnd: Long): Flow<Double>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE transactionType = 'INCOME' AND transactionDate BETWEEN :monthStart AND :monthEnd
        """
    )
    fun observeTotalIncome(monthStart: Long, monthEnd: Long): Flow<Double>
}

data class ExpenseBySourceRow(
    val accountId: Long?,
    val cardId: Long?,
    val total: Double
)
