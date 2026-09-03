package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.components.AluBottomNav
import com.alucalc.app.ui.components.BottomTab
import com.alucalc.app.ui.components.clickableSimple
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluCardBorder
import com.alucalc.app.viewmodel.ProjectViewModel

@Composable
fun HomeScreen(
    userName: String,
    viewModel: ProjectViewModel,
    onNewProject: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenReports: () -> Unit,
    onSelectTab: (BottomTab) -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    val reports by viewModel.reportHistory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProjects()
        viewModel.loadReportHistory()
    }

    Scaffold(
        bottomBar = { AluBottomNav(current = BottomTab.HOME, onSelect = onSelectTab) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome back,", fontSize = 13.sp, color = Color.Gray)
                    Text(userName, style = MaterialTheme.typography.titleLarge)
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, AluCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile", tint = AluBlack)
                }
            }

            Spacer(Modifier.height(20.dp))

            // New estimation project card
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(AluBlack, RoundedCornerShape(16.dp))
                    .clickableSimple(onNewProject)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .size(44.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = AluBlack)
                }
                Spacer(Modifier.height(14.dp))
                Text("New Estimation Project", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(
                    "Calculate dimensions, profiles, and tracks",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard(
                    icon = Icons.Filled.Folder,
                    title = "Recent Projects",
                    value = "${projects.size} Projects",
                    modifier = Modifier.weight(1f),
                    onClick = onOpenProjects
                )
                InfoCard(
                    icon = Icons.Filled.Description,
                    title = "Report History",
                    value = "${reports.size} Reports",
                    modifier = Modifier.weight(1f),
                    onClick = onOpenReports
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, AluCardBorder, RoundedCornerShape(14.dp))
            .clickableSimple(onClick)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, tint = AluBlack)
            Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(title, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
