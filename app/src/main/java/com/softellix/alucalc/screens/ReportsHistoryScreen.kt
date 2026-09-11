package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReportItemUI(
    val id: String,
    val projectTitle: String,
    val reportCode: String,
    val estimator: String,
    val date: String
)

@Composable
fun ReportsHistoryScreen(
    viewModel: ProjectViewModel,
    onReportClick: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val todayDate = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()) }

    LaunchedEffect(Unit) {
        viewModel.fetchRecentProjects()
    }

    // Newest / Latest reports on top
    val sortedProjects = viewModel.recentProjectsList.reversed()
    val reports = sortedProjects.mapIndexed { idx, p ->
        val codeNumber = 100 + (sortedProjects.size - idx)
        ReportItemUI(
            id = p.id,
            projectTitle = p.projectName,
            reportCode = "#AP-$codeNumber",
            estimator = viewModel.currentUser?.name ?: "Ram",
            date = todayDate
        )
    }

    Scaffold(
        bottomBar = {
            AluBottomNavigation(
                selectedTab = 2,
                onTabSelected = onTabSelected
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(LanguageManager.tr("reports_title"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("manage_reports_sub"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text("${LanguageManager.tr("generated_reports_count")} (${reports.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (reports.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(LanguageManager.tr("no_reports_yet"), color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(reports) { report ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onReportClick(report.id) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, BorderGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(report.projectTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                                        Text(report.reportCode, color = Color.Gray, fontSize = 12.sp)
                                    }
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "View",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${LanguageManager.tr("created_on")} ${report.date}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
