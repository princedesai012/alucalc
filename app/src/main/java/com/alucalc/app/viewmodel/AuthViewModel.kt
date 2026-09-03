package com.alucalc.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alucalc.app.data.model.LoginRequest
import com.alucalc.app.data.model.RegisterRequest
import com.alucalc.app.data.remote.TokenStore
import com.alucalc.app.data.repository.AluRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}

class AuthViewModel(
    private val repository: AluRepository,
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    fun register(
        name: String, businessName: String, phone: String, password: String,
        confirmPassword: String, street: String, city: String, state: String
    ) {
        if (password != confirmPassword) {
            _state.value = UiState.Error("Passwords do not match")
            return
        }
        if (name.isBlank() || phone.isBlank() || password.length < 8) {
            _state.value = UiState.Error("Please fill required fields (password min 8 chars)")
            return
        }
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.register(
                RegisterRequest(name, businessName, phone, password, street, city, state)
            )
            result.onSuccess {
                tokenStore.saveSession(it.token, it.name)
                _state.value = UiState.Success
            }.onFailure {
                _state.value = UiState.Error(it.message ?: "Registration failed")
            }
        }
    }

    fun login(phone: String, password: String) {
        if (phone.isBlank() || password.isBlank()) {
            _state.value = UiState.Error("Enter phone number and password")
            return
        }
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.login(LoginRequest(phone, password))
            result.onSuccess {
                tokenStore.saveSession(it.token, it.name)
                _state.value = UiState.Success
            }.onFailure {
                _state.value = UiState.Error(it.message ?: "Login failed")
            }
        }
    }

    fun resetState() { _state.value = UiState.Idle }
}
