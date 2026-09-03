package com.softellix.alucalc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.ui.theme.PrimaryDark

@Composable
fun WizardHeader(
    title: String,
    stepText: String,
    percentageText: String,
    progress: Float,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Top Bar with Back Arrow and Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Text (e.g., "STEP 1 OF 3" and "33% Complete")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stepText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = percentageText, fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // The Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = PrimaryDark,
            trackColor = Color(0xFFE0E0E0)
        )
    }
}