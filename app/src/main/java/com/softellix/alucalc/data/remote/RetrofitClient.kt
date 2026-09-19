package com.softellix.alucalc.data.remote

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softellix.alucalc.BuildConfig
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Final Render Backend Deployment URL
    var baseUrl: String = "https://aluminium-softellix-tech-backend.onrender.com/"
        set(value) {
            val formatted = if (!value.endsWith("/")) "$value/" else value
            field = formatted
            _apiService = null // Force re-creation of Retrofit client with new URL
        }

    // Holds the bearer token in memory for the current session.
    var authToken: String? = null
    var tokenStore: TokenStore? = null

    fun initialize(context: Context) {
        tokenStore = TokenStore(context.applicationContext)
    }

    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
        chain.proceed(requestBuilder.build())
    }

    private val tokenAuthenticator = Authenticator { _, response ->
        if (response.responseCount >= 3) {
            return@Authenticator null // Prevent infinite loop
        }

        val store = tokenStore ?: return@Authenticator null
        val userId = runBlocking { store.getUserId() } ?: return@Authenticator null

        synchronized(this) {
            val currentToken = authToken
            val headerToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (currentToken != null && currentToken != headerToken) {
                return@Authenticator response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshJson = JSONObject().apply { put("userUuid", userId) }.toString()
            val refreshRequest = Request.Builder()
                .url("${baseUrl}api/auth/refresh")
                .post(refreshJson.toRequestBody("application/json".toMediaType()))
                .build()

            val refreshHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            try {
                val refreshResponse = refreshHttpClient.newCall(refreshRequest).execute()
                if (refreshResponse.isSuccessful) {
                    val bodyString = refreshResponse.body?.string() ?: ""
                    val jsonResponse = JSONObject(bodyString)
                    val newAccessToken = jsonResponse.optString("accessToken")

                    if (!newAccessToken.isNullOrBlank()) {
                        authToken = newAccessToken
                        runBlocking { store.updateAccessToken(newAccessToken) }

                        return@Authenticator response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    }
                } else {
                    runBlocking { store.clear() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        null
    }

    private val Response.responseCount: Int
        get() {
            var result = 1
            var prior = priorResponse
            while (prior != null) {
                result++
                prior = prior.priorResponse
            }
            return result
        }

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Handles Render cold start
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .addInterceptor(logging)
        .build()

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private var _apiService: ApiService? = null

    val apiService: ApiService
        get() {
            if (_apiService == null) {
                _apiService = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(ApiService::class.java)
            }
            return _apiService!!
        }
}
