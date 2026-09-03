package com.alucalc.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alucalc.app.data.remote.RetrofitClient
import com.alucalc.app.data.remote.TokenStore
import com.alucalc.app.data.repository.AluRepository
import com.alucalc.app.ui.components.BottomTab
import com.alucalc.app.ui.screens.*
import com.alucalc.app.viewmodel.AuthViewModel
import com.alucalc.app.viewmodel.ProjectViewModel
import com.alucalc.app.viewmodel.ViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROJECTS = "projects"
    const val REPORTS = "reports"
    const val SETTINGS = "settings"
    const val NEW_PROJECT = "new_project"
    const val SELECT_PROFILE = "select_profile"
    const val WINDOW_DETAILS = "window_details"
    const val REPORT = "report"
}

@Composable
fun AluNavGraph(tokenStore: TokenStore) {
    val navController = rememberNavController()
    val repository = remember { AluRepository() }
    val factory = remember { ViewModelFactory(repository, tokenStore) }

    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val projectViewModel: ProjectViewModel = viewModel(factory = factory)

    var userName by remember { mutableStateOf("") }
    var startDestination by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Check for a saved session on launch; skip the login screen if one exists.
    LaunchedEffect(Unit) {
        val token = tokenStore.getToken()
        if (!token.isNullOrBlank()) {
            RetrofitClient.authToken = token
            userName = tokenStore.getUserName() ?: ""
            startDestination = Routes.HOME
        } else {
            startDestination = Routes.REGISTER
        }
    }

    val start = startDestination ?: return

    NavHost(navController = navController, startDestination = start) {

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegistered = {
                    scope.launch { userName = tokenStore.getUserName() ?: "" }
                    navController.navigate(Routes.HOME) { popUpTo(0) }
                },
                onGoToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoggedIn = {
                    scope.launch { userName = tokenStore.getUserName() ?: "" }
                    navController.navigate(Routes.HOME) { popUpTo(0) }
                },
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userName = userName,
                viewModel = projectViewModel,
                onNewProject = {
                    projectViewModel.resetWizard()
                    navController.navigate(Routes.NEW_PROJECT)
                },
                onOpenProjects = { navController.navigate(Routes.PROJECTS) },
                onOpenReports = { navController.navigate(Routes.REPORTS) },
                onSelectTab = { tab -> navigateToTab(navController, tab) }
            )
        }

        composable(Routes.PROJECTS) {
            ProjectsListScreen(
                viewModel = projectViewModel,
                onOpenProject = { /* navigate to a project detail screen if needed */ },
                onSelectTab = { tab -> navigateToTab(navController, tab) }
            )
        }

        composable(Routes.REPORTS) {
            ReportsHistoryScreen(
                viewModel = projectViewModel,
                onOpenReport = { navController.navigate(Routes.REPORT) },
                onSelectTab = { tab -> navigateToTab(navController, tab) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                userName = userName,
                onLogout = {
                    scope.launch {
                        tokenStore.clear()
                        navController.navigate(Routes.LOGIN) { popUpTo(0) }
                    }
                },
                onSelectTab = { tab -> navigateToTab(navController, tab) }
            )
        }

        composable(Routes.NEW_PROJECT) {
            NewProjectScreen(
                viewModel = projectViewModel,
                onBack = { navController.popBackStack() },
                onNext = {
                    projectViewModel.createProject {
                        navController.navigate(Routes.SELECT_PROFILE)
                    }
                }
            )
        }

        composable(Routes.SELECT_PROFILE) {
            SelectProfileScreen(
                viewModel = projectViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Routes.WINDOW_DETAILS) }
            )
        }

        composable(Routes.WINDOW_DETAILS) {
            WindowDetailsScreen(
                viewModel = projectViewModel,
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(Routes.REPORT) { popUpTo(Routes.HOME) }
                }
            )
        }

        composable(Routes.REPORT) {
            val report by projectViewModel.report.collectAsState()
            ReportScreen(
                report = report,
                onBack = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) } },
                onDownloadPdf = { /* open report.pdfUrl via an Intent, or download via Retrofit @Streaming */ },
                onShare = { /* Intent.ACTION_SEND with the pdf/report link */ }
            )
        }
    }
}

private fun navigateToTab(navController: NavHostController, tab: BottomTab) {
    val route = when (tab) {
        BottomTab.HOME -> Routes.HOME
        BottomTab.PROJECTS -> Routes.PROJECTS
        BottomTab.REPORTS -> Routes.REPORTS
        BottomTab.SETTINGS -> Routes.SETTINGS
    }
    navController.navigate(route) { popUpTo(Routes.HOME) { inclusive = false } }
}
