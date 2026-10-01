package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager

@Composable
fun InvoiceDetailScreen(
    invoiceId: String,
    onBackClick: () -> Unit
) {
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
            IconButton(onClick = { /* Share UI */ }) {
                Icon(Icons.Outlined.Share, contentDescription = "Share", tint = PrimaryDark)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Invoice Card Container (A4 style card preview)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Shop Header
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("SHREE RAM ALUMINIUM & GLASS", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryDark)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Custom Aluminium Windows & Fabrications", fontSize = 12.sp, color = Color.Gray)
                    Text("Contact: +91 97148 65744 | Surat, Gujarat", fontSize = 11.sp, color = Color.Gray)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderGray)

                // Invoice Meta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Party Name:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("Smit Desai", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Invoice No: #INV-001", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        Text("Date: Oct 01, 2026", fontSize = 11.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Items Table Header
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Description", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryFont)
                        Text("Amount", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryFont)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Items Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Aluminium Sliding Windows (40mm)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryFont)
                        Text("120 Sq. Ft @ ₹ 350.00 / sq.ft", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text("₹ 42,000.00", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryFont)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Hardware & Fitting charges", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryFont)
                    Text("₹ 2,500.00", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryFont)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Discount Applied", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Red)
                    Text("- ₹ 1,500.00", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Red)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = PrimaryDark)

                // Total Amount Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Payable:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                    Text("₹ 43,000.00", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF16A34A))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notes / Terms
                Text("Notes & Terms:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                Text("1. 50% advance payment required upon order confirmation.\n2. Goods once sold will not be taken back.", fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AluPrimaryButton(
            text = "Download PDF Invoice",
            onClick = { /* Download UI action */ }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
