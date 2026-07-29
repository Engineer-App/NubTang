package com.pft.tracker.security

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val userNameFlow: Flow<String> = context.userDataStore.data.map { it[Keys.USER_NAME] ?: "ผู้ใช้งาน" }

    suspend fun setUserName(name: String) {
        context.userDataStore.edit { it[Keys.USER_NAME] = name }
    }
}
