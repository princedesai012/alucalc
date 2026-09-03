package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun AddWindowsScreen(
    onCalculateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // Temporary state variables
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var selectedTrack by remember { mutableStateOf("2T") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState()) // Makes the screen scrollable
    ) {
        WizardHeader(
            title = "Regular 40mm", // In a real app, this title is dynamic based on Step 2
            stepText = "STEP 3 OF 3",
            percentageText = "100% Complete",
            progress = 1.0f,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, BorderGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Scale: Auto Estimate Preview", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Height & Width Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Height (mm)", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                AluTextField(value = height, onValueChange = { height = it }, placeholder = "e.g. 1500")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Width (mm)", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                AluTextField(value = width, onValueChange = { width = it }, placeholder = "e.g. 1200")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Quantity", fontSize = 12.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(value = quantity, onValueChange = { quantity = it }, placeholder = "e.g. 4")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Track Type", fontSize = 12.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        TrackTypeSelector(
            selectedTrack = selectedTrack,
            onTrackSelected = { selectedTrack = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("ADDED WINDOWS (2)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Hardcoded dummy data to match the Figma design temporarily
        AddedWindowCard(windowNumber = 1, height = "1500", width = "1200", track = "2T", qty = "4", onDelete = {})
        Spacer(modifier = Modifier.height(8.dp))
        AddedWindowCard(windowNumber = 2, height = "1800", width = "2400", track = "3T", qty = "2", onDelete = {})

        Spacer(modifier = Modifier.height(24.dp))

        AluOutlinedButton(text = "Add Another Window", onClick = { /* TODO */ })

        Spacer(modifier = Modifier.height(12.dp))

        AluPrimaryButton(text = "Save & Calculate", onClick = onCalculateClick)

        Spacer(modifier = Modifier.height(24.dp)) // Bottom padding
    }
}