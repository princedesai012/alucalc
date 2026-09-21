package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.data.remote.TokenStore
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun DashboardScreen(
    viewModel: ProjectViewModel,
    onNewProjectClick: () -> Unit,
    onRecentProjectsClick: () -> Unit = {},
    onReportHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTabSelected: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }
    var storedName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchRecentProjects()
        viewModel.fetchCurrentUser()
        val name = tokenStore.getUserName()
        if (!name.isNullOrBlank()) {
            storedName = name
        }
    }

    val user = viewModel.currentUser
    val displayName = user?.name?.ifBlank { null } ?: storedName.takeIf { it != "User" && it.isNotBlank() } ?: "User"

    Scaffold(
        bottomBar = {
            AluBottomNavigation(
                selectedTab = 0,
                onTabSelected = onTabSelected
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Top Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(LanguageManager.tr("welcome_back") + ",", color = Color.Gray, fontSize = 14.sp)
                    Text(displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .border(1.dp, BorderGray, CircleShape)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Large "New Estimation Project" Card
            Button(
                onClick = onNewProjectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 130.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = PrimaryDark,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(LanguageManager.tr("new_project"), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(LanguageManager.tr("calculate_sub"), color = Color.LightGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Two Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = LanguageManager.tr("recent_projects"),
                    value = if (viewModel.isLoading) "..." else "${viewModel.recentProjectsList.size} ${LanguageManager.tr("all_projects")}",
                    icon = Icons.Outlined.Folder,
                    onClick = onRecentProjectsClick,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = LanguageManager.tr("report_history"),
                    value = if (viewModel.isLoading) "..." else "${viewModel.recentProjectsList.size} ${LanguageManager.tr("reports")}",
                    icon = Icons.Outlined.Description,
                    onClick = onReportHistoryClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(icon, contentDescription = title, tint = Color.Gray)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = Color.LightGray, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                Text(title, color = Color.Gray, fontSize = 12.sp)
                Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
            }
        }
    }
}
