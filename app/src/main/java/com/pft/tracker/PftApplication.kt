package com.pft.tracker

import android.app.Application
import androidx.work.Configuration
import com.pft.tracker.di.AppContainer
import com.pft.tracker.notification.NotificationHelper
import com.pft.tracker.worker.RecurringTransactionWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PftApplication : Application(), Configuration.Provider {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this, applicationScope)
        NotificationHelper.ensureChannel(this)
        RecurringTransactionWorker.schedule(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(container.workerFactory)
            .build()
}
