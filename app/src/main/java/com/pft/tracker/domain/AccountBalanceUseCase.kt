package com.pft.tracker.domain

import com.pft.tracker.data.local.dao.AccountDao
import com.pft.tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Implements the account current-balance rule from doc §4.1. The actual SUM
 * formula lives once in [AccountDao] — this layer never re-derives it, it only
 * shapes the result for callers (per §2: all balance math funnels through here).
 */
class AccountBalanceUseCase(private val accountDao: AccountDao) {

    fun observeBalance(accountId: Long): Flow<Double> =
        accountDao.observeBalance(accountId).map { it?.currentBalance ?: 0.0 }

    fun observeAllWithBalance(): Flow<List<AccountWithBalance>> =
        combine(accountDao.observeAll(), accountDao.observeAllBalances()) { accounts, balances ->
            val balanceMap = balances.associateBy({ it.id }, { it.currentBalance })
            accounts.map { AccountWithBalance(it, balanceMap[it.id] ?: it.openingBalance) }
        }

    fun observeActiveWithBalance(): Flow<List<AccountWithBalance>> =
        observeAllWithBalance().map { list -> list.filter { it.account.isActive } }

    fun observeCashTotal(): Flow<Double> =
        observeActiveWithBalance().map { list ->
            list.filter { it.account.accountType == "CASH" && it.account.isOwnedBySelf }.sumOf { it.currentBalance }
        }

    fun observeBankTotal(): Flow<Double> =
        observeActiveWithBalance().map { list ->
            list.filter { it.account.accountType == "BANK" && it.account.isOwnedBySelf }.sumOf { it.currentBalance }
        }
}

data class AccountWithBalance(
    val account: AccountEntity,
    val currentBalance: Double
)
