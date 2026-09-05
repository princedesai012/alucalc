package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluOutlinedButton
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.data.model.CalculationPiece
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.utils.PdfReportGenerator
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun ReportScreen(
    viewModel: ProjectViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchReportOnBackend()
    }

    val reportData = viewModel.reportResponse
    val projectName = reportData?.projectName ?: viewModel.projectName.ifBlank { "Marina Heights - A" }
    val totalUnits = if (reportData != null && reportData.windows.isNotEmpty()) {
        "${reportData.windows.sumOf { it.quantity }} Units"
    } else if (viewModel.addedWindows.isNotEmpty()) {
        "${viewModel.addedWindows.sumOf { it.qty.toIntOrNull() ?: 1 }} Units"
    } else {
        "6 Units"
    }
    val profileName = reportData?.selectedProfile ?: if (viewModel.selectedProfile == "65mm") "Slim 65mm" else "Reg ${viewModel.selectedProfile}"

    val apiWindows = reportData?.windows
    val addedWindows = viewModel.addedWindows

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Report", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

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
        Text("DETAILED CALCULATION BREAKDOWN", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Live Calculation Details Rendering for ALL Windows
        if (!apiWindows.isNullOrEmpty()) {
            apiWindows.forEachIndexed { index, win ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderGray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Window #${index + 1}: ${win.width}\" x ${win.height}\"",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${PdfReportGenerator.formatTrackName(win.trackType)} • Qty: ${win.quantity}",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

                        win.calculation?.let { calc ->
                            calc.handleHeight?.let { CalculationRow(it) }
                            calc.interlockHeight?.let { CalculationRow(it) }
                            calc.topAndSide?.let { CalculationRow(it) }
                            calc.parts.forEach { part ->
                                CalculationRow(part)
                            }
                        } ?: run {
                            Text("Calculations generated based on profile standards.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else if (addedWindows.isNotEmpty()) {
            addedWindows.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderGray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Window #${index + 1}: ${item.widthDisplay} x ${item.heightDisplay}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${PdfReportGenerator.formatTrackName(item.track)} • Qty: ${item.qty}",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

                        item.calculation?.let { calc ->
                            calc.handleHeight?.let { CalculationRow(it) }
                            calc.interlockHeight?.let { CalculationRow(it) }
                            calc.topAndSide?.let { CalculationRow(it) }
                            calc.parts.forEach { part -> CalculationRow(part) }
                        } ?: run {
                            Text("Calculation metrics synced from backend standards.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            // Default demo card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Window #1: 36.0\" x 48.0\" (Regular 40mm)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)
                    CalculationRow(CalculationPiece("Handle Height", 46.5, 1, 2))
                    CalculationRow(CalculationPiece("Interlock Height", 46.5, 1, 2))
                    CalculationRow(CalculationPiece("Top & Side", 46.0, 4, 8))
                    CalculationRow(CalculationPiece("Top & Bottom", 14.75, 4, 8))
                    CalculationRow(CalculationPiece("Glass Height", 44.0, 1, 2))
                    CalculationRow(CalculationPiece("Glass Width", 15.375, 1, 2))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Disclaimer Note
        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Calculations generated using Aluminum Standard Multipliers. Track cuts and glass sizing reflect total pieces.",
                fontSize = 11.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AluOutlinedButton(
            text = "Download PDF Report",
            onClick = {
                val file = PdfReportGenerator.generatePdfReport(
                    context = context,
                    projectName = projectName,
                    profileName = profileName,
                    totalUnits = totalUnits,
                    reportData = reportData,
                    addedWindows = addedWindows
                )
                if (file != null) {
                    Toast.makeText(context, "PDF Report generated successfully!", Toast.LENGTH_SHORT).show()
                    PdfReportGenerator.openOrSharePdf(context, file, isShare = false)
                } else {
                    Toast.makeText(context, "Failed to generate PDF Report", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AluPrimaryButton(
            text = "Share Complete Report",
            onClick = {
                val file = PdfReportGenerator.generatePdfReport(
                    context = context,
                    projectName = projectName,
                    profileName = profileName,
                    totalUnits = totalUnits,
                    reportData = reportData,
                    addedWindows = addedWindows
                )
                if (file != null) {
                    PdfReportGenerator.openOrSharePdf(context, file, isShare = true)
                } else {
                    Toast.makeText(context, "Failed to generate PDF Report for sharing", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- Helper Composables ---

@Composable
fun CalculationRow(piece: CalculationPiece) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(piece.name ?: "Part", fontSize = 12.sp, color = Color.DarkGray)
        Row {
            Text("${piece.value ?: 0.0}\"", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(12.dp))
            Text("(${piece.totalPieces ?: 0} pcs)", fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ReportInfoColumn(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
