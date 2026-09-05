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
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.components.WizardHeader
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun NewProjectScreen(
    viewModel: ProjectViewModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp) // Extra padding for status bar area
    ) {
        WizardHeader(
            title = "New Project",
            stepText = "STEP 1 OF 3",
            percentageText = "33% Complete",
            progress = 0.33f,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("PROJECT DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Project Name", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(
            value = viewModel.projectName,
            onValueChange = { viewModel.projectName = it },
            placeholder = "e.g. Marina heights apartment"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Contact Information", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(
            value = viewModel.contactInfo,
            onValueChange = { viewModel.contactInfo = it },
            placeholder = "Owner / Site head phone number"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("PROJECT SITE ADDRESS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Street Address", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(
            value = viewModel.streetAddress,
            onValueChange = { viewModel.streetAddress = it },
            placeholder = "Street line 1"
        )

        Spacer(modifier = Modifier.weight(1f))

        AluPrimaryButton(
            text = if (viewModel.isLoading) "Creating Project..." else "Next",
            onClick = {
                viewModel.createProjectOnBackend(onSuccess = onNextClick)
            }
        )
    }
}