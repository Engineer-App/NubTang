package com.pft.tracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.di.AppContainer
import com.pft.tracker.util.UpdateInfo
import com.pft.tracker.util.UpdateManager
import com.pft.tracker.util.UpdateStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val hasPin: Boolean = false,
    val biometricEnabled: Boolean = false,
    val autoLockMinutes: Int = 1
)

class SettingsViewModel(private val container: AppContainer) : ViewModel() {

    private val updateManager = UpdateManager(container.appContext)
    val updateStatus: StateFlow<UpdateStatus> = updateManager.status

    val uiState: StateFlow<SettingsUiState> = combine(
        container.securityPreferences.hasPinFlow,
        container.securityPreferences.biometricEnabledFlow,
        container.securityPreferences.autoLockMinutesFlow
    ) { hasPin, biometricEnabled, autoLockMinutes ->
        SettingsUiState(hasPin, biometricEnabled, autoLockMinutes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun changePin(currentPin: String, newPin: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            if (!container.securityPreferences.verifyPin(currentPin)) {
                onResult(false, "PIN ปัจจุบันไม่ถูกต้อง")
                return@launch
            }
            if (newPin.length < 4) {
                onResult(false, "PIN ใหม่ต้องมีอย่างน้อย 4 หลัก")
                return@launch
            }
            container.securityPreferences.setPin(newPin)
            onResult(true, null)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch { container.securityPreferences.setBiometricEnabled(enabled) }
    }

    fun setAutoLockMinutes(minutes: Int) {
        viewModelScope.launch { container.securityPreferences.setAutoLockMinutes(minutes) }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            // Placeholder URL - User will replace this with their actual GitHub/Drive link
            updateManager.checkForUpdates("https://raw.githubusercontent.com/username/repo/main/version.json")
        }
    }

    fun downloadUpdate(info: UpdateInfo) {
        viewModelScope.launch {
            updateManager.downloadAndInstall(info)
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            container.transactionUseCase.resetEverything().onSuccess {
                com.pft.tracker.data.local.SeedData.populate(container.database)
            }
        }
    }

    fun resetMonthData(year: Int, month: Int) {
        val ym = java.time.YearMonth.of(year, month)
        val start = ym.atDay(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = ym.atEndOfMonth().atTime(23, 59, 59).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        viewModelScope.launch { container.transactionUseCase.deleteInRange(start, end) }
    }

    suspend fun exportBackup(): ByteArray = container.backupManager.exportEncrypted()
    suspend fun importBackup(bytes: ByteArray) = container.backupManager.importEncrypted(bytes)
    suspend fun exportCsv(): String = container.csvExporter.buildCsv()
}
