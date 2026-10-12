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
import java.util.TimeZone

data class ReportItemUI(
    val id: String,
    val projectTitle: String,
    val reportCode: String,
    val estimator: String,
    val date: String
)

fun formatReportDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) {
        return SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date())
    }
    return try {
        // Handle ISO 8601 strings like "2026-10-10T12:34:56Z" or "2026-10-10T12:34:56.789Z"
        val parser = if (rawDate.contains(".")) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        } else if (rawDate.contains("T")) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.US)
        }
        
        if (rawDate.endsWith("Z")) {
            parser.timeZone = TimeZone.getTimeZone("UTC")
        }

        val dateObj = parser.parse(rawDate)
        if (dateObj != null) {
            SimpleDateFormat("MMM dd, yyyy", Locale.US).format(dateObj)
        } else {
            rawDate.take(10) // fallback
        }
    } catch (e: Exception) {
        // Very safe fallback to standard substring logic if parsing completely fails
        try {
            if (rawDate.length >= 10) {
                val datePart = rawDate.substring(0, 10)
                val parts = datePart.split("-")
                if (parts.size == 3) {
                    val year = parts[0].toIntOrNull() ?: 2026
                    val month = parts[1].toIntOrNull() ?: 1
                    val day = parts[2].toIntOrNull() ?: 1
                    val cal = java.util.Calendar.getInstance().apply {
                        set(year, month - 1, day)
                    }
                    SimpleDateFormat("MMM dd, yyyy", Locale.US).format(cal.time)
                } else {
                    datePart
                }
            } else {
                rawDate
            }
        } catch (inner: Exception) {
            rawDate.take(10)
        }
    }
}

@Composable
fun ReportsHistoryScreen(
    viewModel: ProjectViewModel,
    onReportClick: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchRecentProjects()
    }

    // Sort by chronological order first (oldest to newest) to assign consistent IDs
    val chronologicalProjects = viewModel.recentProjectsList.sortedBy { p ->
        p.rawDate ?: ""
    }

    val projectsWithId = chronologicalProjects.mapIndexed { idx, p ->
        val resolvedNumber = p.projectNumber ?: (101 + idx)
        p to resolvedNumber
    }

    // Finally sort them newest first for display
    val reports = projectsWithId.sortedByDescending { it.first.rawDate ?: "" }.map { (p, num) ->
        val formattedDate = formatReportDate(p.rawDate)
        ReportItemUI(
            id = p.id,
            projectTitle = p.projectName,
            reportCode = "#AP-$num",
            estimator = viewModel.currentUser?.name ?: "Ram",
            date = formattedDate
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
                .padding(16.dp)
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(report.projectTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                                        Text(report.reportCode, color = Color.Gray, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
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
