package com.pft.tracker.di

import android.content.Context
import com.pft.tracker.backup.BackupManager
import com.pft.tracker.backup.CsvExporter
import com.pft.tracker.data.local.AppDatabase
import com.pft.tracker.data.repository.AccountRepository
import com.pft.tracker.data.repository.CategoryRepository
import com.pft.tracker.data.repository.CreditCardRepository
import com.pft.tracker.data.repository.CreditCardStatementRepository
import com.pft.tracker.data.repository.CreditLimitGroupRepository
import com.pft.tracker.data.repository.RecurringTransactionRepository
import com.pft.tracker.data.repository.TransactionRepository
import com.pft.tracker.domain.AccountBalanceUseCase
import com.pft.tracker.domain.BillingCycleUseCase
import com.pft.tracker.domain.BudgetUseCase
import com.pft.tracker.domain.CreditCardBalanceUseCase
import com.pft.tracker.domain.TransactionUseCase
import com.pft.tracker.security.AutoLockManager
import com.pft.tracker.security.SecurityPreferences
import com.pft.tracker.worker.AppWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Manual dependency container (no Hilt) — a single place wiring DB, DAOs,
 * repositories, use cases, and cross-cutting singletons for the whole app.
 */
class AppContainer(context: Context, applicationScope: CoroutineScope) {

    val appContext = context.applicationContext

    val database: AppDatabase = AppDatabase.getInstance(appContext, applicationScope)

    private val accountDao = database.accountDao()
    private val creditLimitGroupDao = database.creditLimitGroupDao()
    private val creditCardDao = database.creditCardDao()
    private val categoryDao = database.categoryDao()
    private val transactionDao = database.transactionDao()
    private val creditCardStatementDao = database.creditCardStatementDao()
    private val recurringTransactionDao = database.recurringTransactionDao()

    val accountRepository = AccountRepository(accountDao)
    val creditCardRepository = CreditCardRepository(creditCardDao)
    val creditLimitGroupRepository = CreditLimitGroupRepository(creditLimitGroupDao)
    val categoryRepository = CategoryRepository(categoryDao)
    val transactionRepository = TransactionRepository(transactionDao)
    val creditCardStatementRepository = CreditCardStatementRepository(creditCardStatementDao)
    val recurringTransactionRepository = RecurringTransactionRepository(recurringTransactionDao)

    val accountBalanceUseCase = AccountBalanceUseCase(accountDao)
    val creditCardBalanceUseCase = CreditCardBalanceUseCase(creditCardDao, creditLimitGroupDao)
    val billingCycleUseCase = BillingCycleUseCase()
    val budgetUseCase = BudgetUseCase(categoryDao)
    val transactionUseCase = TransactionUseCase(database, transactionDao)
    val monthlyAggregationUseCase = com.pft.tracker.domain.MonthlyAggregationUseCase(transactionRepository, creditCardRepository, billingCycleUseCase)

    val securityPreferences = SecurityPreferences(appContext)
    val userPreferences = com.pft.tracker.security.UserPreferences(appContext)
    val autoLockManager = AutoLockManager(securityPreferences)

    val workerFactory = AppWorkerFactory(recurringTransactionRepository, transactionUseCase)

    val backupManager = BackupManager(this)
    val csvExporter = CsvExporter(this)

    init {
        securityPreferences.autoLockMinutesFlow
            .onEach { autoLockManager.updateAutoLockMinutes(it) }
            .launchIn(applicationScope)
    }
}
