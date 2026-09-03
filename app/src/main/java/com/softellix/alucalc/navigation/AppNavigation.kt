package com.softellix.alucalc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softellix.alucalc.screens.AddWindowsScreen
import com.softellix.alucalc.screens.CreateAccountScreen
import com.softellix.alucalc.screens.DashboardScreen
import com.softellix.alucalc.screens.LoginScreen
import com.softellix.alucalc.screens.NewProjectScreen
import com.softellix.alucalc.screens.ReportScreen
import com.softellix.alucalc.screens.SelectProfileScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun AppNavigation() {
    // This controller is the engine that drives the navigation
    val navController = rememberNavController()
    val sharedViewModel : ProjectViewModel = viewModel()

    // NavHost acts as the container for our screens
    NavHost(navController = navController, startDestination = "create_account") {

        // Route 1: Create Account
        composable("create_account") {
            CreateAccountScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        // Clears the backstack so you don't pile up infinite screens
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

        // Route 3: Dashboard (Placeholder)
        composable("dashboard") {
            DashboardScreen(
                onNewProjectClick = {
                    navController.navigate("new_project_step_1")
                }
            )
        }

        // Route 4: New Project Step 1 (Placeholder)
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

        // Route 5: New Project Step 2 (Placeholder)
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

        // Route 6: New Project Step 3 (Placeholder)
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

        // Route 7: Report Screen (Placeholder)
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