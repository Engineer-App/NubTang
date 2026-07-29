package com.pft.tracker.security

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.securityDataStore by preferencesDataStore(name = "security_prefs")

class SecurityPreferences(private val context: Context) {

    private object Keys {
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val AUTO_LOCK_MINUTES = intPreferencesKey("auto_lock_minutes")
        val HIDE_BALANCES = booleanPreferencesKey("hide_balances")
    }

    val hasPinFlow: Flow<Boolean> = context.securityDataStore.data.map { it[Keys.PIN_HASH] != null }
    val biometricEnabledFlow: Flow<Boolean> = context.securityDataStore.data.map { it[Keys.BIOMETRIC_ENABLED] ?: false }
    val autoLockMinutesFlow: Flow<Int> = context.securityDataStore.data.map { it[Keys.AUTO_LOCK_MINUTES] ?: 1 }
    val hideBalancesFlow: Flow<Boolean> = context.securityDataStore.data.map { it[Keys.HIDE_BALANCES] ?: false }

    suspend fun hasPin(): Boolean = hasPinFlow.first()

    suspend fun setPin(pin: String) {
        Log.d("SecurityPreferences", "Setting new PIN...")
        val salt = PinHasher.generateSalt()
        val hash = PinHasher.hash(pin, salt)
        context.securityDataStore.edit {
            it[Keys.PIN_SALT] = salt
            it[Keys.PIN_HASH] = hash
        }
        Log.d("SecurityPreferences", "PIN saved successfully")
    }

    suspend fun verifyPin(pin: String): Boolean {
        val prefs = context.securityDataStore.data.first()
        val salt = prefs[Keys.PIN_SALT] ?: return false
        val hash = prefs[Keys.PIN_HASH] ?: return false
        return PinHasher.verify(pin, salt, hash)
    }

    suspend fun clearPin() {
        context.securityDataStore.edit {
            it.remove(Keys.PIN_HASH)
            it.remove(Keys.PIN_SALT)
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.securityDataStore.edit { it[Keys.BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setAutoLockMinutes(minutes: Int) {
        context.securityDataStore.edit { it[Keys.AUTO_LOCK_MINUTES] = minutes }
    }

    suspend fun setHideBalances(hide: Boolean) {
        context.securityDataStore.edit { it[Keys.HIDE_BALANCES] = hide }
    }
}
