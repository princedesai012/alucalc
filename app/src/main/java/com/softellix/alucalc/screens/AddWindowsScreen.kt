package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.*
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun AddWindowsScreen(
    viewModel: ProjectViewModel,
    onCalculateClick: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {}
) {
    // Height Inputs (Inches and Doro, 1 inch = 8 doro)
    var heightInch by remember { mutableStateOf("") }
    var heightDoro by remember { mutableStateOf("0") }

    // Width Inputs (Inches and Doro)
    var widthInch by remember { mutableStateOf("") }
    var widthDoro by remember { mutableStateOf("0") }

    var quantity by remember { mutableStateOf("") }
    var selectedTrack by remember { mutableStateOf("2T") }

    val headerTitle = if (viewModel.selectedProfile == "65mm") "Slim 65mm" else "Regular ${viewModel.selectedProfile}"

    val onAddWindowClick = {
        if (heightInch.isNotBlank() && widthInch.isNotBlank()) {
            val hInchVal = heightInch.toDoubleOrNull() ?: 0.0
            val hDoroVal = heightDoro.toDoubleOrNull() ?: 0.0
            val wInchVal = widthInch.toDoubleOrNull() ?: 0.0
            val wDoroVal = widthDoro.toDoubleOrNull() ?: 0.0

            val decimalHeight = hInchVal + (hDoroVal / 8.0)
            val decimalWidth = wInchVal + (wDoroVal / 8.0)

            val hDisplay = if (hDoroVal > 0) "${heightInch}\" ${heightDoro.toInt()}d" else "${heightInch}\""
            val wDisplay = if (wDoroVal > 0) "${widthInch}\" ${widthDoro.toInt()}d" else "${widthInch}\""

            viewModel.addWindow(
                heightDisplay = hDisplay,
                widthDisplay = wDisplay,
                decimalHeight = decimalHeight,
                decimalWidth = decimalWidth,
                track = selectedTrack,
                qty = quantity.ifBlank { "1" }
            )

            heightInch = ""
            heightDoro = "0"
            widthInch = ""
            widthDoro = "0"
            quantity = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header Row with Home Icon Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                WizardHeader(
                    title = headerTitle,
                    stepText = "STEP 3 OF 3",
                    percentageText = "100% Complete",
                    progress = 1.0f,
                    onBackClick = onBackClick
                )
            }
            IconButton(onClick = onHomeClick) {
                Icon(Icons.Default.Home, contentDescription = "Back to Home", tint = PrimaryFont)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Unit Information Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Text(
                "💡 Window Units: Inches & Doro (1 Inch = 8 Doro)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, BorderGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Scale: Auto Estimate Preview", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Height Inputs Row
        Text("HEIGHT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text("Inches", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                AluTextField(value = heightInch, onValueChange = { heightInch = it }, placeholder = "e.g. 48")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Doro (0-7)", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                AluTextField(value = heightDoro, onValueChange = { heightDoro = it }, placeholder = "0")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Width Inputs Row
        Text("WIDTH", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text("Inches", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                AluTextField(value = widthInch, onValueChange = { widthInch = it }, placeholder = "e.g. 36")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Doro (0-7)", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                AluTextField(value = widthDoro, onValueChange = { widthDoro = it }, placeholder = "4")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Quantity", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(value = quantity, onValueChange = { quantity = it }, placeholder = "e.g. 4")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Track Type", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        TrackTypeSelector(
            selectedTrack = selectedTrack,
            onTrackSelected = { selectedTrack = it }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("ADDED WINDOWS (${viewModel.addedWindows.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic window list from ProjectViewModel
        if (viewModel.addedWindows.isEmpty()) {
            Text("No windows added yet. Enter Inches & Doro above and tap 'Add Another Window'.", fontSize = 12.sp, color = Color.Gray)
        } else {
            viewModel.addedWindows.forEachIndexed { index, window ->
                AddedWindowCard(
                    windowNumber = index + 1,
                    height = window.heightDisplay,
                    width = window.widthDisplay,
                    track = window.track,
                    qty = window.qty,
                    onDelete = { viewModel.removeWindow(window) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AluOutlinedButton(text = "Add Another Window", onClick = onAddWindowClick)

        Spacer(modifier = Modifier.height(12.dp))

        AluPrimaryButton(
            text = "Save & Calculate",
            onClick = {
                if (heightInch.isNotBlank() && widthInch.isNotBlank()) {
                    onAddWindowClick()
                }
                onCalculateClick()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        AluOutlinedButton(
            text = "Back to Home",
            onClick = onHomeClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
