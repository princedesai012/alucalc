package com.softellix.alucalc.screens

import android.widget.Toast
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

data class ReportItemUI(
    val id: String,
    val projectTitle: String,
    val reportCode: String,
    val estimator: String,
    val date: String,
    val totalWindows: Int,
    val aluminumMeters: Double,
    val glassSqm: Double
)

@Composable
fun ReportsHistoryScreen(
    onReportClick: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current

    val reports = remember {
        listOf(
            ReportItemUI("1", "Marina Heights - A", "#AP-098", "John Doe", "Jan 26, 2025", 6, 36.4, 14.2),
            ReportItemUI("2", "City Mall Phase 1", "#AP-102", "John Doe", "Feb 02, 2025", 12, 84.8, 32.6),
            ReportItemUI("3", "Green Valley Tower", "#AP-105", "John Doe", "Feb 10, 2025", 8, 52.0, 20.4)
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
            Text("Report History", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Access and share all previously generated calculation reports", color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text("GENERATED REPORTS (${reports.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

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
                                    Text(report.projectTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                                    Text("Aluminum", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text("${report.aluminumMeters} m", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Column {
                                    Text("Glass Area", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text("${report.glassSqm} m²", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Column {
                                    Text("Total Units", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text("${report.totalWindows} Windows", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Created on ${report.date}", fontSize = 11.sp, color = Color.Gray)
                                IconButton(
                                    onClick = {
                                        val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(android.content.Intent.EXTRA_SUBJECT, "AluCalc Report - ${report.projectTitle}")
                                            putExtra(
                                                android.content.Intent.EXTRA_TEXT,
                                                "Report ${report.reportCode} for ${report.projectTitle}\nAluminum: ${report.aluminumMeters}m | Glass: ${report.glassSqm}m²\nGenerated by AluCalc."
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
