package com.pft.tracker.ui.lock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.security.AutoLockManager
import com.pft.tracker.security.SecurityPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class LockMode { LOADING, ENTER_PIN, SETUP_NEW_PIN, SETUP_CONFIRM_PIN }

data class LockUiState(
    val mode: LockMode = LockMode.LOADING,
    val biometricEnabled: Boolean = false,
    val pinInput: String = "",
    val pendingNewPin: String = "",
    val error: String? = null,
    val requestBiometricTick: Int = 0
)

class LockViewModel(
    private val securityPreferences: SecurityPreferences,
    private val autoLockManager: AutoLockManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LockUiState())
    val uiState: StateFlow<LockUiState> = _uiState.asStateFlow()

    init {
        Log.d("LockViewModel", "Initializing...")
        viewModelScope.launch {
            try {
                // Wait for the first emission to ensure we know if PIN exists
                val hasPin = securityPreferences.hasPinFlow.first()
                Log.d("LockViewModel", "hasPin: $hasPin")
                _uiState.value = _uiState.value.copy(
                    mode = if (hasPin) LockMode.ENTER_PIN else LockMode.SETUP_NEW_PIN
                )
            } catch (e: Exception) {
                Log.e("LockViewModel", "Error loading PIN status", e)
                _uiState.value = _uiState.value.copy(mode = LockMode.SETUP_NEW_PIN)
            }
        }
        viewModelScope.launch {
            securityPreferences.biometricEnabledFlow.collect { enabled ->
                Log.d("LockViewModel", "biometricEnabled: $enabled")
                _uiState.value = _uiState.value.copy(biometricEnabled = enabled)
                if (enabled && _uiState.value.mode == LockMode.ENTER_PIN) {
                    triggerBiometric()
                }
            }
        }
    }

    fun onDigit(digit: Char) {
        val state = _uiState.value
        val current = state.pinInput
        if (current.length >= 4) return
        val updated = current + digit
        _uiState.value = state.copy(pinInput = updated, error = null)
        if (updated.length == 4) {
            maybeSubmit(updated)
        }
    }

    fun onBackspace() {
        val state = _uiState.value
        if (state.pinInput.isEmpty()) return
        _uiState.value = state.copy(pinInput = state.pinInput.dropLast(1), error = null)
    }

    fun submit() {
        maybeSubmit(_uiState.value.pinInput, forceSubmit = true)
    }

    private fun maybeSubmit(pin: String, forceSubmit: Boolean = false) {
        if (pin.length != 4) return
        val state = _uiState.value
        when (state.mode) {
            LockMode.ENTER_PIN -> {
                viewModelScope.launch {
                    val ok = securityPreferences.verifyPin(pin)
                    if (ok) {
                        autoLockManager.unlock()
                        _uiState.value = state.copy(pinInput = "", error = null)
                    } else {
                        _uiState.value = state.copy(pinInput = "", error = "PIN ไม่ถูกต้อง")
                    }
                }
            }
            LockMode.SETUP_NEW_PIN -> {
                _uiState.value = state.copy(
                    mode = LockMode.SETUP_CONFIRM_PIN,
                    pendingNewPin = pin,
                    pinInput = ""
                )
            }
            LockMode.SETUP_CONFIRM_PIN -> {
                if (pin == state.pendingNewPin) {
                    viewModelScope.launch {
                        securityPreferences.setPin(pin)
                        autoLockManager.unlock()
                        _uiState.value = state.copy(pinInput = "", pendingNewPin = "", error = null)
                    }
                } else {
                    _uiState.value = state.copy(
                        mode = LockMode.SETUP_NEW_PIN,
                        pinInput = "",
                        pendingNewPin = "",
                        error = "PIN ไม่ตรงกัน กรุณาตั้งใหม่"
                    )
                }
            }
            LockMode.LOADING -> Unit
        }
    }

    fun triggerBiometric() {
        _uiState.value = _uiState.value.copy(requestBiometricTick = _uiState.value.requestBiometricTick + 1)
    }

    fun onBiometricSuccess() {
        autoLockManager.unlock()
    }
}
