package com.pft.tracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.data.repository.RecurringTransactionRepository
import com.pft.tracker.domain.TransactionUseCase
import com.pft.tracker.domain.model.RecurringFrequency
import com.pft.tracker.domain.model.TransactionType
import com.pft.tracker.notification.NotificationHelper
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

/**
 * Implements doc §4.5: runs daily, generates due installment/recurring
 * transactions, advances each plan's schedule, and notifies the user.
 */
class RecurringTransactionWorker(
    context: Context,
    params: WorkerParameters,
    private val recurringRepository: RecurringTransactionRepository,
    private val transactionUseCase: TransactionUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val todayEpoch = today.atStartOfDay(zone).toInstant().toEpochMilli()

        val duePlans = recurringRepository.getDue(todayEpoch)

        duePlans.forEachIndexed { index, plan ->
            val transaction = TransactionEntity(
                transactionDate = plan.nextRunDate,
                transactionType = TransactionType.EXPENSE.name,
                title = plan.name,
                categoryId = plan.categoryId,
                amount = plan.amount,
                sourceAccountId = plan.sourceAccountId,
                sourceCreditCardId = plan.sourceCreditCardId,
                note = plan.note,
                isRecurringGenerated = true,
                recurringPlanId = plan.id
            )
            val insertResult = transactionUseCase.add(transaction)
            if (insertResult.isFailure) return@forEachIndexed

            val newInstallmentsGenerated = plan.installmentsGenerated + 1
            val nextRun = advance(plan.nextRunDate, RecurringFrequency.valueOf(plan.frequency), zone)
            val exhausted = plan.totalInstallments != null && newInstallmentsGenerated >= plan.totalInstallments
            val pastEnd = plan.endDate != null && nextRun > plan.endDate
            val isOneTime = RecurringFrequency.valueOf(plan.frequency) == RecurringFrequency.ONE_TIME

            recurringRepository.update(
                plan.copy(
                    installmentsGenerated = newInstallmentsGenerated,
                    nextRunDate = nextRun,
                    isActive = !(exhausted || pastEnd || isOneTime)
                )
            )

            val message = if (plan.totalInstallments != null) {
                "บันทึกรายการ '${plan.name}' งวดที่ $newInstallmentsGenerated/${plan.totalInstallments} ให้อัตโนมัติแล้ว"
            } else {
                "บันทึกรายการ '${plan.name}' ให้อัตโนมัติแล้ว"
            }
            NotificationHelper.notifyRecurringGenerated(
                applicationContext,
                notificationId = (plan.id + index).toInt(),
                title = "รายการประจำ",
                message = message
            )
        }

        return Result.success()
    }

    private fun advance(current: Long, frequency: RecurringFrequency, zone: ZoneId): Long {
        val date = java.time.Instant.ofEpochMilli(current).atZone(zone).toLocalDate()
        val next = when (frequency) {
            RecurringFrequency.WEEKLY -> date.plusWeeks(1)
            RecurringFrequency.MONTHLY -> date.plusMonths(1)
            RecurringFrequency.YEARLY -> date.plusYears(1)
            RecurringFrequency.ONE_TIME -> date
            RecurringFrequency.LAST_DAY_OF_MONTH -> {
                val nextMonth = date.plusMonths(1)
                nextMonth.withDayOfMonth(nextMonth.lengthOfMonth())
            }
        }
        return next.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    companion object {
        private const val WORK_NAME = "recurring_transaction_daily_check"

        fun schedule(context: Context) {
            val now = java.time.ZonedDateTime.now()
            var nextRun = now.withHour(0).withMinute(5).withSecond(0).withNano(0)
            if (nextRun.isBefore(now)) nextRun = nextRun.plusDays(1)
            val initialDelay = Duration.between(now, nextRun).toMinutes()

            val request = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
