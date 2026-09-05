package com.softellix.alucalc.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.softellix.alucalc.data.model.ProjectReportResponse
import com.softellix.alucalc.viewmodels.WindowItem
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    fun generatePdfReport(
        context: Context,
        projectName: String,
        profileName: String,
        totalUnits: String,
        reportData: ProjectReportResponse?,
        addedWindows: List<WindowItem>
    ): File? {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val titlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 18f
                isFakeBoldText = true
            }

            val subtitlePaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 12f
            }

            val headerPaint = Paint().apply {
                color = Color.BLACK
                textSize = 13f
                isFakeBoldText = true
            }

            val textPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 11f
            }

            val boldPaint = Paint().apply {
                color = Color.BLACK
                textSize = 11f
                isFakeBoldText = true
            }

            var y = 40f

            // Title Header
            canvas.drawText("ALUCALC ESTIMATION REPORT", 40f, y, titlePaint)
            y += 22f
            canvas.drawText("Project: $projectName", 40f, y, subtitlePaint)
            y += 16f
            canvas.drawText("Profile: $profileName | Total Windows: $totalUnits", 40f, y, subtitlePaint)
            y += 20f

            canvas.drawLine(40f, y, 555f, y, Paint().apply { color = Color.LTGRAY; strokeWidth = 1f })
            y += 20f

            canvas.drawText("DETAILED WINDOW CALCULATION BREAKDOWN", 40f, y, headerPaint)
            y += 20f

            val windows = reportData?.windows
            if (!windows.isNullOrEmpty()) {
                windows.forEachIndexed { i, win ->
                    if (y > 780f) return@forEachIndexed
                    canvas.drawText("Window #${i + 1}: ${win.width}\" x ${win.height}\" (${formatTrackName(win.trackType)}, Qty: ${win.quantity})", 40f, y, boldPaint)
                    y += 16f

                    win.calculation?.let { calc ->
                        calc.handleHeight?.let {
                            canvas.drawText("   • Handle Height: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.interlockHeight?.let {
                            canvas.drawText("   • Interlock Height: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.topAndSide?.let {
                            canvas.drawText("   • Top & Side: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.parts.forEach { part ->
                            canvas.drawText("   • ${part.name}: ${part.value}\" (${part.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                    }
                    y += 12f
                }
            } else {
                addedWindows.forEachIndexed { i, win ->
                    if (y > 780f) return@forEachIndexed
                    canvas.drawText("Window #${i + 1}: ${win.widthDisplay} x ${win.heightDisplay} (${formatTrackName(win.track)}, Qty: ${win.qty})", 40f, y, boldPaint)
                    y += 16f
                    win.calculation?.let { calc ->
                        calc.handleHeight?.let {
                            canvas.drawText("   • Handle Height: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.interlockHeight?.let {
                            canvas.drawText("   • Interlock Height: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.topAndSide?.let {
                            canvas.drawText("   • Top & Side: ${it.value}\" (${it.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                        calc.parts.forEach { part ->
                            canvas.drawText("   • ${part.name}: ${part.value}\" (${part.totalPieces} pcs)", 50f, y, textPaint)
                            y += 14f
                        }
                    }
                    y += 12f
                }
            }

            canvas.drawLine(40f, 800f, 555f, 800f, Paint().apply { color = Color.LTGRAY; strokeWidth = 1f })
            canvas.drawText("Generated via AluCalc - Aluminium Window Calculator", 40f, 818f, subtitlePaint)

            pdfDocument.finishPage(page)

            val fileName = "AluCalc_${projectName.replace(" ", "_")}_Report.pdf"
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
                    putExtra(Intent.EXTRA_SUBJECT, "AluCalc PDF Report - ${file.name}")
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
            track.contains("THREE", ignoreCase = true) || track == "3T" -> "3 Track"
            track.contains("FOUR", ignoreCase = true) || track == "4T" -> "4 Track"
            else -> "2 Track"
        }
    }
}
