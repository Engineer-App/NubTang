package com.pft.tracker

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.lock.LockScreen
import com.pft.tracker.ui.lock.LockViewModel
import com.pft.tracker.ui.nav.AppScaffold
import com.pft.tracker.ui.theme.PftTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Hides balances from the recent-apps thumbnail and blocks screenshots (doc §7).
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        enableEdgeToEdge()

        val container = (application as PftApplication).container

        setContent {
            PftTrackerTheme {
                val isLocked by container.autoLockManager.isLocked.collectAsState()
                if (isLocked) {
                    val lockViewModel: LockViewModel = viewModel(
                        factory = ViewModelFactory {
                            LockViewModel(container.securityPreferences, container.autoLockManager)
                        }
                    )
                    LockScreen(lockViewModel)
                } else {
                    AppScaffold(container)
                }
            }
        }
    }
}
