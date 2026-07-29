package com.pft.tracker.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.pft.tracker.data.repository.RecurringTransactionRepository
import com.pft.tracker.domain.TransactionUseCase

class AppWorkerFactory(
    private val recurringTransactionRepository: RecurringTransactionRepository,
    private val transactionUseCase: TransactionUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            RecurringTransactionWorker::class.java.name -> RecurringTransactionWorker(
                appContext,
                workerParameters,
                recurringTransactionRepository,
                transactionUseCase
            )
            else -> null
        }
    }
}
