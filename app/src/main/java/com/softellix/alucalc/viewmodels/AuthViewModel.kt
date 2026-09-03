package com.softellix.alucalc.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softellix.alucalc.data.model.LoginRequest
import com.softellix.alucalc.data.model.RegisterRequest
import com.softellix.alucalc.data.remote.ApiService
import com.softellix.alucalc.data.remote.TokenStore
import kotlinx.coroutines.launch

class AuthViewModel(
    private val apiService: ApiService,
    private val tokenStore: TokenStore
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var authSuccess by mutableStateOf(false)
        private set

    fun login(phone: String, pass: String) {
        if (phone.isBlank() || pass.isBlank()) {
            errorMessage = "Please enter both phone and password."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.login(LoginRequest(phone, pass))
                if (response.isSuccessful && response.body() != null) {
                    val authBody = response.body()!!
                    val token = authBody.accessToken ?: ""
                    val userName = authBody.user?.name ?: "User"
                    tokenStore.saveSession(token, userName)
                    authSuccess = true
                } else {
                    errorMessage = "Login failed: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun register(name: String, businessName: String, phone: String, pass: String) {
        if (name.isBlank() || phone.isBlank() || pass.isBlank()) {
            errorMessage = "Please fill in all required fields."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = RegisterRequest(
                    name = name,
                    businessName = businessName,
                    phone = phone,
                    password = pass,
                    confirmPassword = pass
                )
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    login(phone, pass)
                } else {
                    errorMessage = "Registration failed: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        authSuccess = false
        errorMessage = null
    }
}
