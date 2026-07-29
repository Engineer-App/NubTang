package com.pft.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pft.tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert
    suspend fun insert(account: AccountEntity): Long

    @Insert
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Query("SELECT * FROM accounts ORDER BY isActive DESC, name ASC")
    fun observeAll(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE isActive = 1 ORDER BY name ASC")
    fun observeActive(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getById(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun observeById(id: Long): Flow<AccountEntity?>

    /**
     * Canonical current-balance formula, per doc §4.1. Single source of truth —
     * callers must go through this query rather than re-deriving the sum elsewhere.
     */
    @Query(
        """
        SELECT :accountId AS id, (
            (SELECT openingBalance FROM accounts WHERE id = :accountId)
            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'INCOME' AND destinationAccountId = :accountId), 0.0)
            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL') AND destinationAccountId = :accountId), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceAccountId = :accountId), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL', 'CREDIT_CARD_PAYMENT') AND sourceAccountId = :accountId), 0.0)
        ) AS currentBalance
        """
    )
    fun observeBalance(accountId: Long): Flow<AccountBalanceRow?>

    @Query(
        """
        SELECT a.id AS id, (
            a.openingBalance
            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'INCOME' AND destinationAccountId = a.id), 0.0)
            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL') AND destinationAccountId = a.id), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceAccountId = a.id), 0.0)
            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL', 'CREDIT_CARD_PAYMENT') AND sourceAccountId = a.id), 0.0)
        ) AS currentBalance
        FROM accounts a
        """
    )
    fun observeAllBalances(): Flow<List<AccountBalanceRow>>
}

data class AccountBalanceRow(
    val id: Long,
    val currentBalance: Double
)
