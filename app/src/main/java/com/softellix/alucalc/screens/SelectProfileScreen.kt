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
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
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
            title = LanguageManager.tr("select_profile"),
            stepText = LanguageManager.tr("step_2_sub"),
            percentageText = LanguageManager.tr("pct_67"),
            progress = 0.67f,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(LanguageManager.tr("choose_profile"), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            LanguageManager.tr("choose_profile_sub"),
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(LanguageManager.tr("regular_series"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileSelectionCard(
                title = "40 mm",
                seriesName = LanguageManager.tr("reg_40mm"),
                isSelected = viewModel.selectedProfile == "40mm",
                onClick = { viewModel.selectedProfile = "40mm" },
                modifier = Modifier.weight(1f)
            )
            ProfileSelectionCard(
                title = "60 mm",
                seriesName = LanguageManager.tr("reg_60mm"),
                isSelected = viewModel.selectedProfile == "60mm",
                onClick = { viewModel.selectedProfile = "60mm" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(LanguageManager.tr("slim_series"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileSelectionCard(
                title = "65 mm",
                seriesName = LanguageManager.tr("slim_65mm"),
                isSelected = viewModel.selectedProfile == "65mm",
                onClick = { viewModel.selectedProfile = "65mm" },
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }

        Spacer(modifier = Modifier.weight(1f))

        AluPrimaryButton(
            text = LanguageManager.tr("next"),
            onClick = {
                viewModel.updateProfileOnBackend(viewModel.selectedProfile, onSuccess = onNextClick)
            }
        )
    }
}
