package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager

@Composable
fun CreateInvoiceScreen(
    onBackClick: () -> Unit,
    onGenerateInvoiceClick: () -> Unit
) {
    var partyName by remember { mutableStateOf("") }
    var sqFeet by remember { mutableStateOf("") }
    var pricePerSqFt by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var otherItemName by remember { mutableStateOf("") }
    var otherItemAmount by remember { mutableStateOf("") }
    var showOtherItemField by remember { mutableStateOf(false) }

    val sqFtVal = sqFeet.toDoubleOrNull() ?: 0.0
    val priceVal = pricePerSqFt.toDoubleOrNull() ?: 0.0
    val baseTotal = sqFtVal * priceVal

    val otherVal = otherItemAmount.toDoubleOrNull() ?: 0.0
    val discVal = discount.toDoubleOrNull() ?: 0.0
    val netTotal = (baseTotal + otherVal - discVal).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryFont)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.tr("create_invoice"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(LanguageManager.tr("party_name"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(6.dp))
        AluTextField(value = partyName, onValueChange = { partyName = it }, placeholder = LanguageManager.tr("enter_name"))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(LanguageManager.tr("sq_feet"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                Spacer(modifier = Modifier.height(6.dp))
                AluTextField(value = sqFeet, onValueChange = { sqFeet = it }, placeholder = "e.g. 120", isNumeric = true)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(LanguageManager.tr("price_per_sqft"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                Spacer(modifier = Modifier.height(6.dp))
                AluTextField(value = pricePerSqFt, onValueChange = { pricePerSqFt = it }, placeholder = "e.g. 350", isNumeric = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showOtherItemField) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1.2f)) {
                    AluTextField(value = otherItemName, onValueChange = { otherItemName = it }, placeholder = "Item description")
                }
                Box(modifier = Modifier.weight(1f)) {
                    AluTextField(value = otherItemAmount, onValueChange = { otherItemAmount = it }, placeholder = "Amount ₹", isNumeric = true)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            TextButton(onClick = { showOtherItemField = true }) {
                Text(LanguageManager.tr("add_other_item"), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
            }
        }

        Text(LanguageManager.tr("discount"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(6.dp))
        AluTextField(value = discount, onValueChange = { discount = it }, placeholder = "0.00", isNumeric = true)

        Spacer(modifier = Modifier.height(24.dp))

        // Total summary box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Base Calculation:", color = Color.Gray, fontSize = 14.sp)
                    Text("₹ %.2f".format(baseTotal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
                }
                if (showOtherItemField && otherVal > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(otherItemName.ifBlank { "Other Item" }, color = Color.Gray, fontSize = 14.sp)
                        Text("+ ₹ %.2f".format(otherVal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
                    }
                }
                if (discVal > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount:", color = Color.Gray, fontSize = 14.sp)
                        Text("- ₹ %.2f".format(discVal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Red)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(LanguageManager.tr("total_amount"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                    Text("₹ %.2f".format(netTotal), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF16A34A))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AluPrimaryButton(
            text = LanguageManager.tr("create_invoice"),
            onClick = onGenerateInvoiceClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
