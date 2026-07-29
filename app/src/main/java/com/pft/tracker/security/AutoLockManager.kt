package com.pft.tracker.security

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

/**
 * Locks the app after it has been backgrounded for longer than the configured
 * auto-lock timeout (doc §7). Purely in-memory state — the actual gate is the
 * lock screen route in the nav graph reading [isLocked].
 */
class AutoLockManager(private val securityPreferences: SecurityPreferences) : DefaultLifecycleObserver {

    private val _isLocked = MutableStateFlow(true)
    val isLocked: StateFlow<Boolean> = _isLocked

    private var backgroundedAt: Long? = null
    private var autoLockMillis: Long = TimeUnit.MINUTES.toMillis(1)

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun updateAutoLockMinutes(minutes: Int) {
        autoLockMillis = TimeUnit.MINUTES.toMillis(minutes.toLong())
    }

    fun unlock() {
        _isLocked.value = false
    }

    fun lockNow() {
        _isLocked.value = true
    }

    override fun onStop(owner: LifecycleOwner) {
        backgroundedAt = System.currentTimeMillis()
    }

    override fun onStart(owner: LifecycleOwner) {
        val since = backgroundedAt
        if (since != null && System.currentTimeMillis() - since >= autoLockMillis) {
            _isLocked.value = true
        }
        backgroundedAt = null
    }
}
