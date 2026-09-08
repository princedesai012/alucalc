package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluOutlinedButton
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.data.model.ProjectReportResponse
import com.softellix.alucalc.data.remote.TokenStore
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.utils.PdfReportGenerator
import com.softellix.alucalc.viewmodels.ProjectViewModel
import com.softellix.alucalc.viewmodels.WindowItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReportWindowModel(
    val title: String,
    val trackQty: String,
    val interlockHandleVal: Double,
    val handlePcs: Int,
    val interlockPcs: Int,
    val topSideVal: Double,
    val topSidePcs: Int,
    val topBottomVal: Double,
    val topBottomPcs: Int,
    val glassWidthVal: Double,
    val glassWidthPcs: Int,
    val glassHeightVal: Double,
    val glassHeightPcs: Int
)

@Composable
fun ReportScreen(
    viewModel: ProjectViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }
    var estimatorName by remember { mutableStateOf("User") }

    LaunchedEffect(Unit) {
        viewModel.fetchReportOnBackend()
        viewModel.fetchCurrentUser()
        val name = tokenStore.getUserName()
        if (!name.isNullOrBlank()) {
            estimatorName = name
        }
    }

    val reportData = viewModel.reportResponse
    val projectName = reportData?.projectName ?: viewModel.projectName.ifBlank { "Marina Heights - A" }
    val estimator = viewModel.currentUser?.name ?: estimatorName
    val createdDateFormatted = remember(reportData) {
        reportData?.createdDate?.take(10) ?: SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    val totalUnits = if (reportData != null && reportData.windows.isNotEmpty()) {
        "${reportData.windows.sumOf { it.quantity }} Units"
    } else if (viewModel.addedWindows.isNotEmpty()) {
        "${viewModel.addedWindows.sumOf { it.qty.toIntOrNull() ?: 1 }} Units"
    } else {
        "3 Units"
    }
    val profileName = reportData?.selectedProfile ?: if (viewModel.selectedProfile == "65mm") "Slim 65mm" else "Reg ${viewModel.selectedProfile}"

    val addedWindows = viewModel.addedWindows

    val windowModels = remember(reportData, addedWindows) {
        extractReportWindowModels(reportData, addedWindows)
    }

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryFont)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.tr("report"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
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
                    Text(projectName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                    Text("#AP-098", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportInfoColumn(LanguageManager.tr("estimator"), estimator)
                    ReportInfoColumn(LanguageManager.tr("created_date"), createdDateFormatted)
                    ReportInfoColumn(LanguageManager.tr("total_windows"), totalUnits)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(LanguageManager.tr("calc_breakdown"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(12.dp))

        // 1. Interlock & Handle Section (Bifurcated Handle vs Interlock Pieces)
        SectionCard(title = LanguageManager.tr("sec_interlock_handle")) {
            windowModels.forEach { win ->
                SinglePieceRow(
                    title = win.title,
                    badgeText = win.trackQty,
                    valueText = "${win.interlockHandleVal}\"",
                    pcsText = "(${LanguageManager.tr("handle_short")}: ${win.handlePcs}, ${LanguageManager.tr("interlock_short")}: ${win.interlockPcs} ${LanguageManager.tr("pcs_unit")})",
                    accentColor = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Top & Side Section
        SectionCard(title = LanguageManager.tr("sec_top_side")) {
            windowModels.forEach { win ->
                SinglePieceRow(
                    title = win.title,
                    badgeText = win.trackQty,
                    valueText = "${win.topSideVal}\"",
                    pcsText = "(${win.topSidePcs} ${LanguageManager.tr("pcs_unit")})",
                    accentColor = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Top & Bottom Section
        SectionCard(title = LanguageManager.tr("sec_top_bottom")) {
            windowModels.forEach { win ->
                SinglePieceRow(
                    title = win.title,
                    badgeText = win.trackQty,
                    valueText = "${win.topBottomVal}\"",
                    pcsText = "(${win.topBottomPcs} ${LanguageManager.tr("pcs_unit")})",
                    accentColor = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Glass Dimensions (Width & Height) Section
        SectionCard(title = LanguageManager.tr("sec_glass_dimensions")) {
            windowModels.forEach { win ->
                GlassDimensionRow(
                    title = win.title,
                    badgeText = win.trackQty,
                    wVal = win.glassWidthVal,
                    wPcs = win.glassWidthPcs,
                    hVal = win.glassHeightVal,
                    hPcs = win.glassHeightPcs
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Disclaimer Note
        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                LanguageManager.tr("disclaimer_text"),
                fontSize = 11.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AluOutlinedButton(
            text = LanguageManager.tr("download_pdf"),
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
            text = LanguageManager.tr("share_report"),
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

// --- Helper Data Extractor ---

fun extractReportWindowModels(
    reportData: ProjectReportResponse?,
    addedWindows: List<WindowItem>
): List<ReportWindowModel> {
    val result = mutableListOf<ReportWindowModel>()

    val apiWindows = reportData?.windows
    if (!apiWindows.isNullOrEmpty()) {
        apiWindows.forEachIndexed { index, win ->
            val calc = win.calculation
            val handle = calc?.handleHeight
            val interlockH = calc?.interlockHeight
            val topSide = calc?.topAndSide

            val qtyInt = win.quantity.coerceAtLeast(1)

            val ihVal = handle?.value ?: interlockH?.value ?: 0.0

            // Handle pieces are ALWAYS 2 per window unit (2 * quantity)
            val hPcs = handle?.totalPieces ?: (2 * qtyInt)

            // Interlock pieces vary by track type (2T = 2, 3T = 4, 4T = 6 per window unit)
            val iPcs = interlockH?.totalPieces ?: run {
                val perWinI = when {
                    win.trackType.contains("THREE", ignoreCase = true) || win.trackType == "3T" -> 4
                    win.trackType.contains("FOUR", ignoreCase = true) || win.trackType == "4T" -> 6
                    else -> 2
                }
                perWinI * qtyInt
            }

            val tsVal = topSide?.value ?: 0.0
            val tsPcs = topSide?.totalPieces ?: (4 * qtyInt)

            val tbPart = calc?.parts?.firstOrNull { it.name.equals("Top & Bottom", ignoreCase = true) }
            val tbVal = tbPart?.value ?: 0.0
            val tbPcs = tbPart?.totalPieces ?: (4 * qtyInt)

            val gwPart = calc?.parts?.firstOrNull { it.name.equals("Glass Width", ignoreCase = true) }
            val gwVal = gwPart?.value ?: 0.0
            val gwPcs = gwPart?.totalPieces ?: (1 * qtyInt)

            val ghPart = calc?.parts?.firstOrNull { it.name.equals("Glass Height", ignoreCase = true) }
            val ghVal = ghPart?.value ?: 0.0
            val ghPcs = ghPart?.totalPieces ?: (1 * qtyInt)

            result.add(
                ReportWindowModel(
                    title = "${LanguageManager.tr("window_prefix")} #${index + 1}: ${win.width}\" x ${win.height}\"",
                    trackQty = "${PdfReportGenerator.formatTrackName(win.trackType)}, ${LanguageManager.tr("qty_unit")}: ${win.quantity}",
                    interlockHandleVal = ihVal,
                    handlePcs = hPcs,
                    interlockPcs = iPcs,
                    topSideVal = tsVal,
                    topSidePcs = tsPcs,
                    topBottomVal = tbVal,
                    topBottomPcs = tbPcs,
                    glassWidthVal = gwVal,
                    glassWidthPcs = gwPcs,
                    glassHeightVal = ghVal,
                    glassHeightPcs = ghPcs
                )
            )
        }
    } else if (addedWindows.isNotEmpty()) {
        addedWindows.forEachIndexed { index, item ->
            val calc = item.calculation
            val handle = calc?.handleHeight
            val interlockH = calc?.interlockHeight
            val topSide = calc?.topAndSide

            val qtyInt = (item.qty.toIntOrNull() ?: 1).coerceAtLeast(1)

            val ihVal = handle?.value ?: interlockH?.value ?: 0.0
            val hPcs = handle?.totalPieces ?: (2 * qtyInt)

            val iPcs = interlockH?.totalPieces ?: run {
                val perWinI = when {
                    item.track.contains("THREE", ignoreCase = true) || item.track == "3T" -> 4
                    item.track.contains("FOUR", ignoreCase = true) || item.track == "4T" -> 6
                    else -> 2
                }
                perWinI * qtyInt
            }

            val tsVal = topSide?.value ?: 0.0
            val tsPcs = topSide?.totalPieces ?: (4 * qtyInt)

            val tbPart = calc?.parts?.firstOrNull { it.name.equals("Top & Bottom", ignoreCase = true) }
            val tbVal = tbPart?.value ?: 0.0
            val tbPcs = tbPart?.totalPieces ?: (4 * qtyInt)

            val gwPart = calc?.parts?.firstOrNull { it.name.equals("Glass Width", ignoreCase = true) }
            val gwVal = gwPart?.value ?: 0.0
            val gwPcs = gwPart?.totalPieces ?: (1 * qtyInt)

            val ghPart = calc?.parts?.firstOrNull { it.name.equals("Glass Height", ignoreCase = true) }
            val ghVal = ghPart?.value ?: 0.0
            val ghPcs = ghPart?.totalPieces ?: (1 * qtyInt)

            result.add(
                ReportWindowModel(
                    title = "${LanguageManager.tr("window_prefix")} #${index + 1}: ${item.widthDisplay} x ${item.heightDisplay}",
                    trackQty = "${PdfReportGenerator.formatTrackName(item.track)}, ${LanguageManager.tr("qty_unit")}: ${item.qty}",
                    interlockHandleVal = ihVal,
                    handlePcs = hPcs,
                    interlockPcs = iPcs,
                    topSideVal = tsVal,
                    topSidePcs = tsPcs,
                    topBottomVal = tbVal,
                    topBottomPcs = tbPcs,
                    glassWidthVal = gwVal,
                    glassWidthPcs = gwPcs,
                    glassHeightVal = ghVal,
                    glassHeightPcs = ghPcs
                )
            )
        }
    } else {
        // Fallback demo windows: Handle is ALWAYS 2 pcs per window unit
        result.add(
            ReportWindowModel(
                title = "${LanguageManager.tr("window_prefix")} #1: 35.0\" x 45.875\"",
                trackQty = "3 ${LanguageManager.tr("track_unit")}, ${LanguageManager.tr("qty_unit")}: 1",
                interlockHandleVal = 44.375,
                handlePcs = 2,
                interlockPcs = 4,
                topSideVal = 43.875,
                topSidePcs = 4,
                topBottomVal = 13.292,
                topBottomPcs = 6,
                glassWidthVal = 13.917,
                glassWidthPcs = 1,
                glassHeightVal = 41.875,
                glassHeightPcs = 1
            )
        )
        result.add(
            ReportWindowModel(
                title = "${LanguageManager.tr("window_prefix")} #2: 20.375\" x 15.0\"",
                trackQty = "2 ${LanguageManager.tr("track_unit")}, ${LanguageManager.tr("qty_unit")}: 1",
                interlockHandleVal = 13.5,
                handlePcs = 2,
                interlockPcs = 2,
                topSideVal = 13.0,
                topSidePcs = 4,
                topBottomVal = 5.25,
                topBottomPcs = 4,
                glassWidthVal = 5.875,
                glassWidthPcs = 1,
                glassHeightVal = 11.0,
                glassHeightPcs = 1
            )
        )
        result.add(
            ReportWindowModel(
                title = "${LanguageManager.tr("window_prefix")} #3: 13.5\" x 12.625\"",
                trackQty = "4 ${LanguageManager.tr("track_unit")}, ${LanguageManager.tr("qty_unit")}: 1",
                interlockHandleVal = 11.125,
                handlePcs = 2,
                interlockPcs = 6,
                topSideVal = 10.625,
                topSidePcs = 4,
                topBottomVal = 3.5,
                topBottomPcs = 8,
                glassWidthVal = 4.0,
                glassWidthPcs = 1,
                glassHeightVal = 8.625,
                glassHeightPcs = 1
            )
        )
    }

    return result
}

// --- Helper UI Composables ---

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderGray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .background(Color(0xFF2563EB), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = PrimaryFont
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SinglePieceRow(
    title: String,
    badgeText: String,
    valueText: String,
    pcsText: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(
                title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    badgeText,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(valueText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accentColor, maxLines = 1)
            Spacer(modifier = Modifier.width(6.dp))
            Text(pcsText, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
        }
    }
}

@Composable
fun GlassDimensionRow(
    title: String,
    badgeText: String,
    wVal: Double,
    wPcs: Int,
    hVal: Double,
    hPcs: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(
                title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    badgeText,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.wrapContentWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("W: $wVal\"", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), maxLines = 1)
                Spacer(modifier = Modifier.width(4.dp))
                Text("($wPcs ${LanguageManager.tr("pcs_unit")})", fontSize = 11.sp, color = Color.Gray, maxLines = 1)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("H: $hVal\"", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), maxLines = 1)
                Spacer(modifier = Modifier.width(4.dp))
                Text("($hPcs ${LanguageManager.tr("pcs_unit")})", fontSize = 11.sp, color = Color.Gray, maxLines = 1)
            }
        }
    }
}

@Composable
fun ReportInfoColumn(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryFont)
    }
}
