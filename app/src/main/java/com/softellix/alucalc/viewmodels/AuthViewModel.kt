package com.softellix.alucalc.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softellix.alucalc.data.model.*
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

    var otpSent by mutableStateOf(false)
        private set

    var otpVerified by mutableStateOf(false)
        private set

    var resetToken by mutableStateOf<String?>(null)
        private set

    var passwordResetSuccess by mutableStateOf(false)
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
                    val userObj = authBody.user
                    val userName = userObj?.name ?: "User"
                    val uPhone = userObj?.phone ?: phone
                    val uBusiness = userObj?.businessName ?: "Doe Windows"

                    tokenStore.saveSession(
                        token = token,
                        userName = userName,
                        userPhone = uPhone,
                        userBusiness = uBusiness
                    )
                    authSuccess = true
                } else {
                    val errDetail = response.errorBody()?.string() ?: response.message()
                    errorMessage = "Login failed (${response.code()}): ${errDetail.ifBlank { "Invalid credentials" }}"
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
                    phone = phone,
                    businessName = businessName.ifBlank { "AluCalc Client" },
                    password = pass,
                    confirmPassword = pass,
                    address = Address(street = "123 Main St", city = "Surat", state = "Gujarat")
                )
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    // Registration succeeded -> trigger login to fetch user object and save session
                    login(phone, pass)
                } else {
                    val errDetail = response.errorBody()?.string() ?: response.message()
                    errorMessage = "Registration failed (${response.code()}): ${errDetail.ifBlank { "Invalid registration data" }}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveGuestSession(name: String = "John Doe", phone: String = "+91 9999999999", business: String = "Doe Windows") {
        viewModelScope.launch {
            tokenStore.saveSession("demo_guest_token", name, phone, business)
            authSuccess = true
        }
    }

    fun forgotPassword(phone: String) {
        if (phone.isBlank()) {
            errorMessage = "Please enter your phone number."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = apiService.forgotPassword(ForgotPasswordRequest(phone))
                if (res.isSuccessful) {
                    otpSent = true
                } else {
                    errorMessage = "Failed to send OTP: ${res.errorBody()?.string() ?: res.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyOtp(phone: String, otp: String) {
        if (otp.isBlank()) {
            errorMessage = "Please enter the OTP."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = apiService.verifyOtp(VerifyOtpRequest(phone, otp))
                if (res.isSuccessful && res.body() != null) {
                    resetToken = res.body()!!.resetToken
                    otpVerified = true
                } else {
                    errorMessage = "Invalid OTP: ${res.errorBody()?.string() ?: res.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPassword(newPass: String, confirmPass: String) {
        val token = resetToken
        if (token.isNullOrBlank()) {
            errorMessage = "Missing reset token. Please verify OTP again."
            return
        }
        if (newPass.isBlank() || newPass != confirmPass) {
            errorMessage = "Passwords do not match."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val req = ResetPasswordRequest(token, newPass, confirmPass)
                val res = apiService.resetPassword(req)
                if (res.isSuccessful) {
                    passwordResetSuccess = true
                } else {
                    errorMessage = "Reset failed: ${res.errorBody()?.string() ?: res.message()}"
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
        otpSent = false
        otpVerified = false
        resetToken = null
        passwordResetSuccess = false
        errorMessage = null
    }
}
