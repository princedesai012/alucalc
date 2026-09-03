package com.alucalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.components.PrimaryButton
import com.alucalc.app.ui.components.SectionLabel
import com.alucalc.app.ui.components.SelectableCard
import com.alucalc.app.ui.components.StepHeader
import com.alucalc.app.viewmodel.ProjectViewModel

@Composable
fun SelectProfileScreen(
    viewModel: ProjectViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selectedProfile by remember { mutableStateOf(viewModel.selectedProfile) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            StepHeader(step = 2, totalSteps = 3, title = "Choose Profile Type")
            Text(
                "Choose a profile type for your window. This dictates the material dimensions, track thickness, and calculation constants.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )

            SectionLabel("Regular Series")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SelectableCard(
                    title = "Regular",
                    subtitle = "40 mm",
                    selected = selectedProfile == "REGULAR_40",
                    onClick = { selectedProfile = "REGULAR_40" },
                    modifier = Modifier.weight(1f)
                )
                SelectableCard(
                    title = "Regular",
                    subtitle = "60 mm",
                    selected = selectedProfile == "REGULAR_60",
                    onClick = { selectedProfile = "REGULAR_60" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))
            SectionLabel("Slim / Dombal")
            SelectableCard(
                title = "Slim Series",
                subtitle = "65 mm",
                selected = selectedProfile == "SLIM_65",
                onClick = { selectedProfile = "SLIM_65" },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
            PrimaryButton(
                text = "Next",
                onClick = {
                    viewModel.selectedProfile = selectedProfile
                    onNext()
                }
            )
        }
    }
}
