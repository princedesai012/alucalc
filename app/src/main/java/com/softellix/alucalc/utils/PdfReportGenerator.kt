package com.softellix.alucalc.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.softellix.alucalc.data.model.ProjectReportResponse
import com.softellix.alucalc.screens.ReportWindowModel
import com.softellix.alucalc.screens.extractReportWindowModels
import com.softellix.alucalc.viewmodels.WindowItem
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    fun generatePdfReport(
        context: Context,
        projectName: String,
        profileName: String,
        totalUnits: String,
        businessName: String = "બારીમાપ",
        reportData: ProjectReportResponse?,
        addedWindows: List<WindowItem>
    ): File? {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Paint definitions matching Image 1
            val pageBgPaint = Paint().apply { color = Color.parseColor("#F1F5F9") }
            val navyBannerPaint = Paint().apply { color = Color.parseColor("#0F172A") }
            val accentBluePaint = Paint().apply { color = Color.parseColor("#2563EB") }
            val cardBgPaint = Paint().apply { color = Color.WHITE }
            val cardBorderPaint = Paint().apply {
                color = Color.parseColor("#CBD5E1")
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }

            val titlePaint = Paint().apply {
                color = Color.WHITE
                textSize = 18f
                isFakeBoldText = true
            }

            val subtitlePaint = Paint().apply {
                color = Color.parseColor("#94A3B8")
                textSize = 10.5f
            }

            val sectionTitlePaint = Paint().apply {
                color = Color.parseColor("#0F172A")
                textSize = 13f
                isFakeBoldText = true
            }

            val windowTitlePaint = Paint().apply {
                color = Color.parseColor("#334155")
                textSize = 11f
                isFakeBoldText = true
            }

            val badgeBgPaint = Paint().apply { color = Color.parseColor("#F1F5F9") }
            val badgePaint = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 9.5f
            }

            val greenValPaint = Paint().apply {
                color = Color.parseColor("#16A34A")
                textSize = 11.5f
                isFakeBoldText = true
            }

            val blueValPaint = Paint().apply {
                color = Color.parseColor("#2563EB")
                textSize = 11f
                isFakeBoldText = true
            }

            val pcsPaint = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 10f
            }

            val dividerPaint = Paint().apply {
                color = Color.parseColor("#F1F5F9")
                strokeWidth = 1f
            }

            // Fill page background
            canvas.drawRect(0f, 0f, 595f, 842f, pageBgPaint)

            // Top Navy Banner
            canvas.drawRect(0f, 0f, 595f, 75f, navyBannerPaint)
            canvas.drawRect(0f, 75f, 595f, 78f, accentBluePaint)

            val displayHeader = when {
                businessName.isBlank() || businessName.contains("AluCalc", ignoreCase = true) || businessName.contains("Doe Windows", ignoreCase = true) -> "બારીમાપ"
                else -> businessName
            }

            val reportTitle = when (LanguageManager.currentLanguage) {
                "GUJARATI" -> "$displayHeader અંદાજ રિપોર્ટ"
                "HINDI" -> "$displayHeader अनुमान रिपोर्ट"
                else -> "${displayHeader.uppercase()} ESTIMATION REPORT"
            }

            val metaSubtitle = "${LanguageManager.tr("project_name")}: $projectName   |   ${LanguageManager.tr("select_profile")}: $profileName   |   ${LanguageManager.tr("total_windows")}: $totalUnits"

            canvas.drawText(reportTitle, 25f, 36f, titlePaint)
            canvas.drawText(metaSubtitle, 25f, 58f, subtitlePaint)

            val cardLeft = 25f
            val cardRight = 570f
            var startY = 95f

            val windowModels = extractReportWindowModels(reportData, addedWindows)

            // Helper function to draw card / div
            fun drawCardSection(
                title: String,
                isGlass: Boolean = false,
                getValAndPcs: (ReportWindowModel) -> Pair<String, String> = { Pair("", "") }
            ) {
                val rowHeight = if (isGlass) 42f else 32f
                val cardHeight = 36f + (windowModels.size * rowHeight)
                val cardTop = startY
                val cardBottom = cardTop + cardHeight

                // Draw Card Div
                val cardRect = RectF(cardLeft, cardTop, cardRight, cardBottom)
                canvas.drawRoundRect(cardRect, 10f, 10f, cardBgPaint)
                canvas.drawRoundRect(cardRect, 10f, 10f, cardBorderPaint)

                // Left Blue Accent Bar
                canvas.drawRoundRect(RectF(cardLeft + 12f, cardTop + 12f, cardLeft + 16f, cardTop + 28f), 2f, 2f, accentBluePaint)

                // Section Title
                canvas.drawText(title, cardLeft + 24f, cardTop + 25f, sectionTitlePaint)

                var rowY = cardTop + 45f

                windowModels.forEachIndexed { i, win ->
                    // Window Title
                    canvas.drawText(win.title, cardLeft + 16f, rowY, windowTitlePaint)

                    // Track Badge
                    val badgeText = win.trackQty
                    val badgeTextWidth = badgePaint.measureText(badgeText)
                    val badgeLeft = cardLeft + 16f + windowTitlePaint.measureText(win.title) + 8f
                    canvas.drawRoundRect(RectF(badgeLeft, rowY - 11f, badgeLeft + badgeTextWidth + 12f, rowY + 3f), 4f, 4f, badgeBgPaint)
                    canvas.drawText(badgeText, badgeLeft + 6f, rowY - 1f, badgePaint)

                    if (!isGlass) {
                        val (valStr, pcsStr) = getValAndPcs(win)
                        val pcsWidth = pcsPaint.measureText(pcsStr)
                        val valWidth = greenValPaint.measureText(valStr)

                        canvas.drawText(pcsStr, cardRight - 16f - pcsWidth, rowY, pcsPaint)
                        canvas.drawText(valStr, cardRight - 16f - pcsWidth - 5f - valWidth, rowY, greenValPaint)
                    } else {
                        // Glass Dimensions Stacked Blue Values
                        val wText = "W: ${win.glassWidthVal}\""
                        val wPcs = "(${win.glassWidthPcs} ${LanguageManager.tr("pcs_unit")})"
                        val hText = "H: ${win.glassHeightVal}\""
                        val hPcs = "(${win.glassHeightPcs} ${LanguageManager.tr("pcs_unit")})"

                        val wPcsWidth = pcsPaint.measureText(wPcs)
                        val wTextWidth = blueValPaint.measureText(wText)
                        canvas.drawText(wPcs, cardRight - 16f - wPcsWidth, rowY - 5f, pcsPaint)
                        canvas.drawText(wText, cardRight - 16f - wPcsWidth - 4f - wTextWidth, rowY - 5f, blueValPaint)

                        val hPcsWidth = pcsPaint.measureText(hPcs)
                        val hTextWidth = blueValPaint.measureText(hText)
                        canvas.drawText(hPcs, cardRight - 16f - hPcsWidth, rowY + 11f, pcsPaint)
                        canvas.drawText(hText, cardRight - 16f - hPcsWidth - 4f - hTextWidth, rowY + 11f, blueValPaint)
                    }

                    if (i < windowModels.size - 1) {
                        val lineY = rowY + (if (isGlass) 18f else 14f)
                        canvas.drawLine(cardLeft + 16f, lineY, cardRight - 16f, lineY, dividerPaint)
                    }

                    rowY += rowHeight
                }

                startY = cardBottom + 14f
            }

            // 1. Interlock & Handle Card with Bifurcated Handle vs Interlock Pieces
            drawCardSection(LanguageManager.tr("sec_interlock_handle")) { win ->
                Pair("${win.interlockHandleVal}\"", "(${LanguageManager.tr("handle_short")}: ${win.handlePcs}, ${LanguageManager.tr("interlock_short")}: ${win.interlockPcs} ${LanguageManager.tr("pcs_unit")})")
            }

            // 2. Top & Side Card
            drawCardSection(LanguageManager.tr("sec_top_side")) { win ->
                Pair("${win.topSideVal}\"", "(${win.topSidePcs} ${LanguageManager.tr("pcs_unit")})")
            }

            // 3. Top & Bottom Card
            drawCardSection(LanguageManager.tr("sec_top_bottom")) { win ->
                Pair("${win.topBottomVal}\"", "(${win.topBottomPcs} ${LanguageManager.tr("pcs_unit")})")
            }

            // 4. Glass Dimensions Card
            drawCardSection(LanguageManager.tr("sec_glass_dimensions"), isGlass = true)

            // Footer
            canvas.drawLine(25f, 808f, 570f, 808f, dividerPaint)
            val footerText = if (LanguageManager.currentLanguage == "GUJARATI") "બારીમાપ વિન્ડો કેલ્ક્યુલેટર દ્વારા બનાવેલ" else "Generated via $displayHeader - Window Calculator"
            canvas.drawText(footerText, 25f, 824f, subtitlePaint.apply { color = Color.GRAY })

            pdfDocument.finishPage(page)

            val fileName = "BariMaap_${projectName.replace(" ", "_")}_Report.pdf"
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val pdfFile = File(downloadsDir, fileName)

            FileOutputStream(pdfFile).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            return pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun openOrSharePdf(context: Context, file: File, isShare: Boolean = false) {
        try {
            val uri: Uri = FileProvider.getUriForFile(context, "com.softellix.alucalc.fileprovider", file)
            val intent = Intent(if (isShare) Intent.ACTION_SEND else Intent.ACTION_VIEW).apply {
                if (isShare) {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "બારીમાપ PDF Report - ${file.name}")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(Intent.createChooser(intent, if (isShare) "Share PDF Report" else "Open PDF Report"))
        } catch (e: Exception) {
            Toast.makeText(context, "Saved PDF to ${file.absolutePath}", Toast.LENGTH_LONG).show()
        }
    }

    fun formatTrackName(track: String): String {
        return when {
            track.contains("THREE", ignoreCase = true) || track == "3T" -> "3 " + LanguageManager.tr("track_unit")
            track.contains("FOUR", ignoreCase = true) || track == "4T" -> "4 " + LanguageManager.tr("track_unit")
            else -> "2 " + LanguageManager.tr("track_unit")
        }
    }
}
