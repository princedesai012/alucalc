@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.data.model.MaterialBreakdownRow
import com.alucalc.app.data.model.ReportResponse
import com.alucalc.app.ui.components.PrimaryButton
import com.alucalc.app.ui.components.SecondaryButton
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluCardBorder

@Composable
fun ReportScreen(
    report: ReportResponse?,
    onBack: () -> Unit,
    onDownloadPdf: () -> Unit,
    onShare: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        if (report == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No report available yet.", color = Color.Gray)
            }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(14.dp))
                    .border(1.dp, AluCardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(report.projectName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    report.reportCode?.let {
                        Text("#$it", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ReportMeta("ESTIMATOR", report.estimatorName ?: "—")
                    ReportMeta("CREATED DATE", report.createdDate ?: "—")
                    ReportMeta("TOTAL WINDOWS", "${report.totalWindows} Units")
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("MATERIAL BREAKDOWN TABLE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Spacer(Modifier.height(10.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, AluCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                TableHeaderRow()
                Spacer(Modifier.height(8.dp))
                report.materialBreakdown.forEach { row ->
                    TableDataRow(row)
                    Spacer(Modifier.height(8.dp))
                }
                Divider(color = AluCardBorder)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(
                        "Aluminum: ${report.totalAluminumMeters} m   Glass: ${report.totalGlassSqm} m²",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${report.totalWindows}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AluBlack, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Calculation generated using Aluminium Standard Multipliers. Total waste coefficient has been accounted for.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(28.dp))
            SecondaryButton(text = "Download PDF", onClick = onDownloadPdf)
            Spacer(Modifier.height(12.dp))
            PrimaryButton(
                text = "Share Report",
                onClick = onShare,
            )
        }
    }
}

@Composable
private fun ReportMeta(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TableHeaderRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("W#", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(0.6f))
        Text("Profile", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1.4f))
        Text("H × W (mm)", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1.6f))
        Text("Track", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
        Text("Qty", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(0.7f))
    }
}

@Composable
private fun TableDataRow(row: MaterialBreakdownRow) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("#${row.serial}", fontSize = 12.sp, modifier = Modifier.weight(0.6f))
        Text(row.profile, fontSize = 12.sp, modifier = Modifier.weight(1.4f))
        Text("${row.height} × ${row.width}", fontSize = 12.sp, modifier = Modifier.weight(1.6f))
        Text(row.trackType, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text("${row.quantity}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.7f))
    }
}
