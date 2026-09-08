package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
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
    val date: String,
    val totalWindows: Int,
    val aluminumMeters: String,
    val glassSqm: String
)

@Composable
fun ReportsHistoryScreen(
    viewModel: ProjectViewModel,
    onReportClick: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val todayDate = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()) }

    LaunchedEffect(Unit) {
        viewModel.fetchRecentProjects()
    }

    val reports = viewModel.recentProjectsList.mapIndexed { idx, p ->
        val codeNumber = 101 + idx
        ReportItemUI(
            id = p.id,
            projectTitle = p.projectName,
            reportCode = "#AP-$codeNumber",
            estimator = viewModel.currentUser?.name ?: "User",
            date = todayDate,
            totalWindows = p.projectNumber ?: 1,
            aluminumMeters = "Calculated",
            glassSqm = "Calculated"
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
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(LanguageManager.tr("aluminum"), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text("${report.aluminumMeters} m", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryFont)
                                    }
                                    Column {
                                        Text(LanguageManager.tr("glass_area"), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text("${report.glassSqm} m²", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryFont)
                                    }
                                    Column {
                                        Text(LanguageManager.tr("total_windows"), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text("${report.totalWindows} Windows", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryFont)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${LanguageManager.tr("created_on")} ${report.date}", fontSize = 11.sp, color = Color.Gray)
                                    IconButton(
                                        onClick = {
                                            val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(android.content.Intent.EXTRA_SUBJECT, "AluCalc Report - ${report.projectTitle}")
                                                putExtra(
                                                    android.content.Intent.EXTRA_TEXT,
                                                    "Report ${report.reportCode} for ${report.projectTitle}\nAluminum: ${report.aluminumMeters} | Glass: ${report.glassSqm}\nGenerated via AluCalc."
                                                )
                                            }
                                            context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Report"))
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = PrimaryDark, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
