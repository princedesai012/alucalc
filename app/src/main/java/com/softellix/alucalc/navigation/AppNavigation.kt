package com.softellix.alucalc.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val tokenStore = TokenStore(context)
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
    val scope = rememberCoroutineScope()
    val tokenStore = TokenStore(context)

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )
    val sharedViewModel: ProjectViewModel = viewModel()

    NavHost(navController = navController, startDestination = "create_account") {

        // Route 1: Create Account
        composable("create_account") {
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

        // Route 2: Login
        composable("login") {
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

        // Route 3: Dashboard / Home
        composable("dashboard") {
            DashboardScreen(
                onNewProjectClick = {
                    navController.navigate("new_project_step_1")
                },
                onRecentProjectsClick = {
                    navController.navigate("projects_list")
                },
                onReportHistoryClick = {
                    navController.navigate("reports_history")
                },
                onProfileClick = {
                    navController.navigate("settings")
                },
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> { /* Already on Dashboard */ }
                        1 -> navController.navigate("projects_list") { popUpTo("dashboard") }
                        2 -> navController.navigate("reports_history") { popUpTo("dashboard") }
                        3 -> navController.navigate("settings") { popUpTo("dashboard") }
                    }
                }
            )
        }

        // Route 4: Projects List
        composable("projects_list") {
            ProjectsListScreen(
                onProjectClick = { _ ->
                    navController.navigate("report")
                },
                onNewProjectClick = {
                    navController.navigate("new_project_step_1")
                },
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> navController.navigate("dashboard") { popUpTo("dashboard") }
                        1 -> { /* Already on Projects List */ }
                        2 -> navController.navigate("reports_history") { popUpTo("dashboard") }
                        3 -> navController.navigate("settings") { popUpTo("dashboard") }
                    }
                }
            )
        }

        // Route 5: Reports History
        composable("reports_history") {
            ReportsHistoryScreen(
                onReportClick = { _ ->
                    navController.navigate("report")
                },
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> navController.navigate("dashboard") { popUpTo("dashboard") }
                        1 -> navController.navigate("projects_list") { popUpTo("dashboard") }
                        2 -> { /* Already on Reports History */ }
                        3 -> navController.navigate("settings") { popUpTo("dashboard") }
                    }
                }
            )
        }

        // Route 6: Settings & Profile
        composable("settings") {
            SettingsScreen(
                onLogoutClick = {
                    scope.launch {
                        tokenStore.clear()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> navController.navigate("dashboard") { popUpTo("dashboard") }
                        1 -> navController.navigate("projects_list") { popUpTo("dashboard") }
                        2 -> navController.navigate("reports_history") { popUpTo("dashboard") }
                        3 -> { /* Already on Settings */ }
                    }
                }
            )
        }

        // Route 7: New Project Step 1
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

        // Route 8: New Project Step 2
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

        // Route 9: New Project Step 3
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

        // Route 10: Estimation Report
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
