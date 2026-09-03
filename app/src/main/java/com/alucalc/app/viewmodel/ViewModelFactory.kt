package com.alucalc.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alucalc.app.data.remote.TokenStore
import com.alucalc.app.data.repository.AluRepository

class ViewModelFactory(
    private val repository: AluRepository,
    private val tokenStore: TokenStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repository, tokenStore) as T
            modelClass.isAssignableFrom(ProjectViewModel::class.java) ->
                ProjectViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
