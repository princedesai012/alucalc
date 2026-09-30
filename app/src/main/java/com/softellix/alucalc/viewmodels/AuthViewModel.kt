package com.softellix.alucalc.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softellix.alucalc.data.model.*
import com.softellix.alucalc.data.remote.ApiService
import com.softellix.alucalc.data.remote.TokenStore
import com.softellix.alucalc.utils.LanguageManager
import org.json.JSONObject
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
        
    var isRegOtpSent by mutableStateOf(false)
        private set
        
    var isRegPhoneVerified by mutableStateOf(false)
        private set

    var resetToken by mutableStateOf<String?>(null)
        private set

    var passwordResetSuccess by mutableStateOf(false)
        private set

    fun clearErrorMessage() {
        errorMessage = null
    }

    private fun extractErrorMessage(errorBodyStr: String?): String {
        if (errorBodyStr.isNullOrBlank()) return LanguageManager.tr("err_action_failed")
        return try {
            val jsonObject = JSONObject(errorBodyStr)
            jsonObject.optString("message").takeIf { it.isNotBlank() } ?: LanguageManager.tr("err_action_failed")
        } catch (e: Exception) {
            LanguageManager.tr("err_action_failed")
        }
    }
    
    fun setCustomError(msg: String) {
        errorMessage = msg
    }

    fun login(phone: String, pass: String, fallbackName: String = "") {
        if (phone.isBlank() || pass.isBlank()) {
            errorMessage = LanguageManager.tr("err_phone_pass_empty")
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
                    val userId = userObj?.id
                    val userName = userObj?.name?.ifBlank { null } ?: fallbackName.ifBlank { "User" }
                    val uPhone = userObj?.phone ?: phone
                    val uBusiness = userObj?.businessName ?: "AluCalc Client"

                    tokenStore.saveSession(
                        token = token,
                        userId = userId,
                        userName = userName,
                        userPhone = uPhone,
                        userBusiness = uBusiness
                    )
                    authSuccess = true
                } else {
                    errorMessage = extractErrorMessage(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun register(name: String, businessName: String, phone: String, pass: String) {
        if (name.isBlank() || phone.isBlank() || pass.isBlank()) {
            errorMessage = LanguageManager.tr("err_fill_required")
            return
        }
        
        if (phone.length < 10) {
            errorMessage = LanguageManager.tr("err_invalid_phone")
            return
        }
        
        if (pass.length != 4) {
            errorMessage = LanguageManager.tr("err_invalid_pin")
            return
        }
        
        if (!isRegPhoneVerified) {
            errorMessage = LanguageManager.tr("err_verify_mobile_first")
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
                    login(phone, pass, fallbackName = name)
                } else {
                    errorMessage = extractErrorMessage(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // --- REGISTRATION OTP FLOW ---
    
    fun sendRegistrationOtp(phone: String) {
        if (phone.length < 10) {
            errorMessage = LanguageManager.tr("err_invalid_phone")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = apiService.sendRegistrationOtp(ForgotPasswordRequest(phone))
                if (res.isSuccessful) {
                    isRegOtpSent = true
                } else {
                    errorMessage = extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun resendRegistrationOtp(phone: String) {
        if (phone.length < 10) {
            errorMessage = LanguageManager.tr("err_invalid_phone")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = apiService.resendRegistrationOtp(ForgotPasswordRequest(phone))
                if (res.isSuccessful) {
                    errorMessage = LanguageManager.tr("msg_otp_resent")
                } else {
                    errorMessage = extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun verifyRegistrationOtp(phone: String, otp: String) {
        if (otp.length < 6) {
            errorMessage = LanguageManager.tr("err_enter_full_otp")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = apiService.verifyRegistrationOtp(VerifyOtpRequest(phone, otp))
                if (res.isSuccessful && res.body()?.verified == true) {
                    isRegPhoneVerified = true
                    isRegOtpSent = false
                } else {
                    errorMessage = res.body()?.message ?: extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // ----------------------------

    fun saveGuestSession(name: String = "John Doe", phone: String = "+91 9999999999", business: String = "Doe Windows") {
        viewModelScope.launch {
            tokenStore.saveSession(
                token = "demo_guest_token",
                userId = "guest_uuid",
                userName = name,
                userPhone = phone,
                userBusiness = business
            )
            authSuccess = true
        }
    }

    fun forgotPassword(phone: String) {
        if (phone.isBlank()) {
            errorMessage = LanguageManager.tr("err_phone_pass_empty")
            return
        }
        
        if (phone.length < 10) {
            errorMessage = LanguageManager.tr("err_invalid_phone")
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
                    errorMessage = extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyOtp(phone: String, otp: String) {
        if (otp.isBlank()) {
            errorMessage = LanguageManager.tr("err_enter_full_otp")
            return
        }
        
        if (otp.length < 6) {
            errorMessage = LanguageManager.tr("err_enter_full_otp")
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
                    errorMessage = extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPassword(newPass: String, confirmPass: String) {
        val token = resetToken
        if (token.isNullOrBlank()) {
            errorMessage = LanguageManager.tr("err_missing_reset_token")
            return
        }
        if (newPass.isBlank() || confirmPass.isBlank()) {
            errorMessage = LanguageManager.tr("err_fill_both_pass")
            return
        }
        if (newPass.length != 4) {
            errorMessage = LanguageManager.tr("err_new_pass_4_digits")
            return
        }
        if (newPass != confirmPass) {
            errorMessage = LanguageManager.tr("err_pass_mismatch")
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
                    errorMessage = extractErrorMessage(res.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "${LanguageManager.tr("err_network")}${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        authSuccess = false
        otpSent = false
        otpVerified = false
        isRegOtpSent = false
        isRegPhoneVerified = false
        resetToken = null
        passwordResetSuccess = false
        errorMessage = null
    }
}
