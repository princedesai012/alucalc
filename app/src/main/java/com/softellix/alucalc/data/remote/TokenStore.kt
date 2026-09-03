package com.softellix.alucalc.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "alucalc_prefs")
private val TOKEN_KEY = stringPreferencesKey("auth_token")
private val NAME_KEY = stringPreferencesKey("user_name")

class TokenStore(private val context: Context) {

    suspend fun saveSession(token: String, userName: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[NAME_KEY] = userName
        }
        RetrofitClient.authToken = token
    }

    suspend fun getToken(): String? = context.dataStore.data.first()[TOKEN_KEY]

    suspend fun getUserName(): String? = context.dataStore.data.first()[NAME_KEY]

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
        RetrofitClient.authToken = null
    }
}
