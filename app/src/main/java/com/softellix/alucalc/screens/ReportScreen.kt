package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluOutlinedButton
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun ReportScreen(
    viewModel: ProjectViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val projectName = viewModel.projectName.ifBlank { "Marina Heights - A" }
    val totalUnits = if (viewModel.addedWindows.isEmpty()) {
        "6 Units"
    } else {
        "${viewModel.addedWindows.sumOf { it.qty.toIntOrNull() ?: 1 }} Units"
    }
    val profileName = if (viewModel.selectedProfile == "65mm") "Slim 65mm" else "Reg ${viewModel.selectedProfile}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
    ) {
        // Top Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Report", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Project Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(projectName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("#AP-098", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportInfoColumn("ESTIMATOR", "John Doe")
                    ReportInfoColumn("CREATED DATE", "Jan 26, 2025")
                    ReportInfoColumn("TOTAL WINDOWS", totalUnits)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("MATERIAL BREAKDOWN TABLE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Custom Data Table
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            // Table Header Row
            Row(modifier = Modifier.fillMaxWidth()) {
                TableCell("W #", weight = 1f, isHeader = true)
                TableCell("Profile", weight = 1.5f, isHeader = true)
                TableCell("H x W (mm)", weight = 2f, isHeader = true)
                TableCell("Track", weight = 1f, isHeader = true)
                TableCell("Qty", weight = 1f, isHeader = true)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

            if (viewModel.addedWindows.isEmpty()) {
                // Fallback demo rows
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell("#1", 1f)
                    TableCell("Reg 40mm", 1.5f)
                    TableCell("1500 x 1200", 2f)
                    TableCell("2T", 1f)
                    TableCell("4", 1f)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell("#2", 1f)
                    TableCell("Reg 40mm", 1.5f)
                    TableCell("1800 x 2400", 2f)
                    TableCell("3T", 1f)
                    TableCell("2", 1f)
                }
            } else {
                viewModel.addedWindows.forEachIndexed { index, item ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TableCell("#${index + 1}", 1f)
                        TableCell(profileName, 1.5f)
                        TableCell("${item.height} x ${item.width}", 2f)
                        TableCell(item.track, 1f)
                        TableCell(item.qty, 1f)
                    }
                    if (index < viewModel.addedWindows.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

            // Total Row
            Row(modifier = Modifier.fillMaxWidth()) {
                TableCell("Total", 1f, isHeader = true)
                TableCell("Aluminum: Ready for API", 1.5f)
                TableCell("Glass: Ready for API", 2f)
                TableCell("", 1f)
                TableCell(if (viewModel.addedWindows.isEmpty()) "6" else "${viewModel.addedWindows.sumOf { it.qty.toIntOrNull() ?: 1 }}", 1f, isHeader = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Disclaimer Note
        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Calculation generated using Aluminum Standard Multipliers. Backend calculation API will provide live material metrics.",
                fontSize = 11.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AluOutlinedButton(
            text = "Download PDF",
            onClick = {
                Toast.makeText(context, "Preparing PDF report for $projectName...", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AluPrimaryButton(
            text = "Share Report",
            onClick = {
                val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_SUBJECT, "AluCalc Window Estimation - $projectName")
                    putExtra(
                        android.content.Intent.EXTRA_TEXT,
                        "AluCalc Window Estimation Report\nProject: $projectName\nProfile: $profileName\nTotal Windows: $totalUnits\nEstimator: John Doe"
                    )
                }
                val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Report")
                context.startActivity(shareIntent)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- Helper Composables ---

@Composable
fun ReportInfoColumn(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float, isHeader: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        fontSize = if (isHeader) 10.sp else 12.sp,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        color = if (isHeader) Color.Gray else Color.Black
    )
}