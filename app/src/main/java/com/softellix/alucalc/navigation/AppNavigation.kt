package com.softellix.alucalc.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softellix.alucalc.data.remote.RetrofitClient
import com.softellix.alucalc.data.remote.TokenStore
import com.softellix.alucalc.screens.*
import com.softellix.alucalc.viewmodels.AuthViewModel
import com.softellix.alucalc.viewmodels.ProjectViewModel

// 1. Create a Factory to instantiate the AuthViewModel with its required dependencies
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val tokenStore = TokenStore(context)
            // Note: Ensure your RetrofitClient exposes the apiService instance like this
            val apiService = RetrofitClient.apiService
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(apiService, tokenStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 2. Instantiate the ViewModels
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )
    val sharedViewModel: ProjectViewModel = viewModel()

    NavHost(navController = navController, startDestination = "create_account") {

        composable("create_account") {
            // 3. Pass the AuthViewModel to the screen
            CreateAccountScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("create_account") { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("create_account") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            // 4. Pass the AuthViewModel to the screen
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("create_account") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNewProjectClick = {
                    navController.navigate("new_project_step_1")
                }
            )
        }

        composable("new_project_step_1") {
            LaunchedEffect(Unit) { sharedViewModel.resetProject() }
            NewProjectScreen(
                viewModel = sharedViewModel,
                onNextClick = {
                    navController.navigate("new_project_step_2")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("new_project_step_2") {
            SelectProfileScreen(
                viewModel = sharedViewModel,
                onNextClick = {
                    navController.navigate("new_project_step_3")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("new_project_step_3") {
            AddWindowsScreen(
                viewModel = sharedViewModel,
                onCalculateClick = {
                    navController.navigate("report")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("report") {
            ReportScreen(
                viewModel = sharedViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}