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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluOutlinedButton
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.InvoiceViewModel

@Composable
fun InvoiceDetailScreen(
    invoiceId: String,
    viewModel: InvoiceViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(invoiceId) {
        viewModel.fetchInvoiceDetails(invoiceId)
    }

    val invoice = viewModel.currentInvoice

    if (viewModel.isLoading || invoice == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundGray), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val shopName = invoice.user?.businessName?.ifBlank { "બારીમાપ WINDOWS" } ?: "બારીમાપ WINDOWS"
    val shopContact = invoice.user?.phone ?: ""
    val shopAddress = invoice.user?.address ?: ""
    val invNo = invoice.invoiceNumber ?: "N/A"

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.White,
            title = { Text("Delete Invoice?", fontWeight = FontWeight.Bold, color = PrimaryDark) },
            text = { Text("Are you sure you want to delete invoice $invNo?", color = Color(0xFF334155)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteInvoice(invoiceId) {
                            Toast.makeText(context, "Invoice Deleted", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }
                    }
                ) {
                    Text(LanguageManager.tr("delete"), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(LanguageManager.tr("cancel"), color = PrimaryFont, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryFont)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invoice Preview", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            }
            Row {
                IconButton(onClick = {
                    viewModel.downloadAndShareInvoicePdf(context, invoiceId, invNo, true)
                }) {
                    Icon(Icons.Outlined.Share, contentDescription = "Share", tint = PrimaryDark)
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Invoice Card Container (Matching Web UI PDF)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                
                // Header Details
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(shopName, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color(0xFF2196F3)) // Bright Blue
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val addressLine = buildString {
                        if (shopContact.isNotBlank()) append("Phone: $shopContact")
                        if (shopContact.isNotBlank() && shopAddress.isNotBlank()) append(" | ")
                        if (shopAddress.isNotBlank()) append(shopAddress)
                    }
                    if (addressLine.isNotBlank()) {
                        Text(addressLine, fontSize = 11.sp, color = Color.DarkGray)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("INVOICE", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PrimaryFont, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Meta Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Bill To:", fontSize = 12.sp, color = PrimaryFont, fontWeight = FontWeight.Bold)
                        Text(invoice.partyName, fontSize = 13.sp, color = Color.DarkGray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Invoice No: $invNo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                        Text("Date: ${formatReportDate(invoice.createdAt)}", fontSize = 12.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Items Table Header (Blue Style)
                Surface(
                    color = Color(0xFF2196F3),
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Project Name", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(2f))
                        Text("Sq.Ft", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        Text("Price / Sq.Ft", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1.5f), textAlign = TextAlign.End)
                        Text("Amount", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
                    }
                }

                // Items Rows
                invoice.items.forEachIndexed { index, item ->
                    val isEven = index % 2 == 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isEven) Color.White else Color(0xFFF8FAFC))
                            .border(BorderStroke(0.5.dp, BorderGray))
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.projectName ?: item.description ?: "Item", fontSize = 12.sp, color = PrimaryFont, modifier = Modifier.weight(2f))
                        Text(item.squareFeet?.toString() ?: "-", fontSize = 12.sp, color = PrimaryFont, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        Text(item.pricePerSquareFeet?.let { "%.2f".format(it) } ?: "-", fontSize = 12.sp, color = PrimaryFont, modifier = Modifier.weight(1.5f), textAlign = TextAlign.End)
                        Text(item.amount?.let { "%.2f".format(it) } ?: "-", fontSize = 12.sp, color = PrimaryFont, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Subtotal, Discount, Final Total
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Column(modifier = Modifier.width(200.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal:", fontSize = 12.sp, color = PrimaryFont)
                            Text("%.2f".format(invoice.subtotal ?: 0.0), fontSize = 12.sp, color = PrimaryFont)
                        }
                        
                        if (invoice.discountAmount != null && invoice.discountAmount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Discount:", fontSize = 12.sp, color = PrimaryFont)
                                Text("- %.2f".format(invoice.discountAmount), fontSize = 12.sp, color = PrimaryFont)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Final Total:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PrimaryFont)
                            Text("%.2f".format(invoice.finalTotal ?: 0.0), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = PrimaryFont)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Notes / Terms Dynamic Render
                val notesStr = buildString {
                    if (!invoice.terms.isNullOrBlank()) appendLine(invoice.terms)
                    if (!invoice.note.isNullOrBlank()) append(invoice.note)
                }.trim()

                if (notesStr.isNotBlank()) {
                    Text("Notes & Terms:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(notesStr, fontSize = 11.sp, color = Color.DarkGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AluOutlinedButton(
            text = "Download PDF Invoice",
            onClick = {
                viewModel.downloadAndShareInvoicePdf(context, invoiceId, invNo, false)
            }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        AluPrimaryButton(
            text = "Share Invoice",
            onClick = {
                viewModel.downloadAndShareInvoicePdf(context, invoiceId, invNo, true)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
