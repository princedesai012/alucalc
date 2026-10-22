package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.InvoiceViewModel

@Composable
fun InvoicesListScreen(
    viewModel: InvoiceViewModel,
    onCreateInvoiceClick: () -> Unit,
    onInvoiceClick: (String) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchInvoices()
    }

    val invoices = viewModel.invoicesList.sortedByDescending { it.createdAt ?: "" }

    Scaffold(
        bottomBar = {
            AluBottomNavigation(
                selectedTab = 3,
                onTabSelected = onTabSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateInvoiceClick,
                containerColor = PrimaryDark,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Invoice")
            }
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(LanguageManager.tr("invoices_title"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("manage_invoices_sub"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text("${LanguageManager.tr("invoices_title")} (${invoices.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (invoices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(LanguageManager.tr("no_invoices_yet"), color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(invoices) { inv ->
                        val dateDisp = formatReportDate(inv.createdAt)
                        val totalDisp = "₹ %.2f".format(inv.finalTotal)
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onInvoiceClick(inv.id) },
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
                                        Text(inv.partyName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                                        Text(inv.invoiceNumber ?: "INV", color = Color.Gray, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(totalDisp, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF16A34A))
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
                                    Text("${LanguageManager.tr("created_on")} $dateDisp", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
