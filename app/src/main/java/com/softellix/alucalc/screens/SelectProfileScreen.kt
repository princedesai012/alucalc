package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.ProfileSelectionCard
import com.softellix.alucalc.components.WizardHeader
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun SelectProfileScreen(
    viewModel: ProjectViewModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
    ) {
        WizardHeader(
            title = "Select Profile",
            stepText = "STEP 2 OF 3",
            percentageText = "67% Complete",
            progress = 0.67f,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Choose Profile Type", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Choose a profile type for your window. This dictates the material dimensions, track thickness, and calculation constants.",
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("REGULAR SERIES", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Row for the Regular Series (40mm and 60mm)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileSelectionCard(
                title = "40 mm",
                seriesName = "Regular",
                isSelected = viewModel.selectedProfile == "40mm",
                onClick = { viewModel.selectedProfile = "40mm" },
                modifier = Modifier.weight(1f)
            )
            ProfileSelectionCard(
                title = "60 mm",
                seriesName = "Regular",
                isSelected = viewModel.selectedProfile == "60mm",
                onClick = { viewModel.selectedProfile = "60mm" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("SLIM / DOMBAL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Row for the Slim Series (65mm). Wrapped in a Row with weight to keep it half-width.
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileSelectionCard(
                title = "65 mm",
                seriesName = "Slim Series",
                isSelected = viewModel.selectedProfile == "65mm",
                onClick = { viewModel.selectedProfile = "65mm" },
                modifier = Modifier.weight(0.5f) // Takes up half the screen width
            )
            Spacer(modifier = Modifier.weight(0.5f)) // Empty space for the other half
        }

        Spacer(modifier = Modifier.weight(1f))

        AluPrimaryButton(text = "Next", onClick = onNextClick)
    }
}