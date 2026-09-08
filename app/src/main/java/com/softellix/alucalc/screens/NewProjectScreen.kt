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
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
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
            .padding(top = 16.dp)
    ) {
        WizardHeader(
            title = LanguageManager.tr("new_project"),
            stepText = LanguageManager.tr("step_1_sub"),
            percentageText = "33% Complete",
            progress = 0.33f,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(LanguageManager.tr("project_details"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(8.dp))

        Text(LanguageManager.tr("project_name"), fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(
            value = viewModel.projectName,
            onValueChange = { viewModel.projectName = it },
            placeholder = LanguageManager.tr("enter_name")
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(LanguageManager.tr("contact_info"), fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        AluTextField(
            value = viewModel.contactInfo,
            onValueChange = { viewModel.contactInfo = it },
            placeholder = LanguageManager.tr("enter_phone"),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(LanguageManager.tr("street_address"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(8.dp))

        AluTextField(
            value = viewModel.streetAddress,
            onValueChange = { viewModel.streetAddress = it },
            placeholder = LanguageManager.tr("street_address")
        )

        Spacer(modifier = Modifier.weight(1f))

        AluPrimaryButton(
            text = if (viewModel.isLoading) "..." else LanguageManager.tr("next"),
            onClick = {
                viewModel.createProjectOnBackend(onSuccess = onNextClick)
            }
        )
    }
}
