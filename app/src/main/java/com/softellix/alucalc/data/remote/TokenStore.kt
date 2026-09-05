package com.softellix.alucalc.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "alucalc_prefs")
private val TOKEN_KEY = stringPreferencesKey("auth_token")
private val NAME_KEY = stringPreferencesKey("user_name")
private val PHONE_KEY = stringPreferencesKey("user_phone")
private val BUSINESS_KEY = stringPreferencesKey("user_business")

class TokenStore(private val context: Context) {

    suspend fun saveSession(
        token: String,
        userName: String,
        userPhone: String? = null,
        userBusiness: String? = null
    ) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[NAME_KEY] = userName
            if (!userPhone.isNullOrBlank()) prefs[PHONE_KEY] = userPhone
            if (!userBusiness.isNullOrBlank()) prefs[BUSINESS_KEY] = userBusiness
        }
        RetrofitClient.authToken = token
    }

    suspend fun getToken(): String? = context.dataStore.data.first()[TOKEN_KEY]

    suspend fun getUserName(): String? = context.dataStore.data.first()[NAME_KEY]

    suspend fun getUserPhone(): String? = context.dataStore.data.first()[PHONE_KEY]

    suspend fun getUserBusiness(): String? = context.dataStore.data.first()[BUSINESS_KEY]

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
        RetrofitClient.authToken = null
    }
}
