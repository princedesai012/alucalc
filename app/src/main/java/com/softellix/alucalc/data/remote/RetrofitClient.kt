package com.softellix.alucalc.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    // Base URL for API requests.
    // Note: '10.0.2.2' is only for Android Emulator.
    // For physical phone on local Wi-Fi (e.g. OnePlus 10R 5G), use your PC's Wi-Fi IP address:
    var baseUrl: String = "http://192.168.0.5:3000/"
        set(value) {
            val formatted = if (!value.endsWith("/")) "$value/" else value
            field = formatted
            _apiService = null // Force re-creation of Retrofit client with new URL
        }

    // Holds the bearer token in memory for the current session.
    var authToken: String? = null

    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
        chain.proceed(requestBuilder.build())
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
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
