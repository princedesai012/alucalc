@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.alucalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alucalc.app.ui.components.LabeledTextField
import com.alucalc.app.ui.components.PrimaryButton
import com.alucalc.app.ui.components.SectionLabel
import com.alucalc.app.ui.components.StepHeader
import com.alucalc.app.viewmodel.ProjectViewModel

@Composable
fun NewProjectScreen(
    viewModel: ProjectViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var name by remember { mutableStateOf(viewModel.projectName) }
    var contact by remember { mutableStateOf(viewModel.contactInfo) }
    var street by remember { mutableStateOf(viewModel.street) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Project") },
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
            StepHeader(step = 1, totalSteps = 3, title = "Project Details")
            Spacer(Modifier.height(20.dp))

            SectionLabel("Project Details")
            LabeledTextField("Project Name", name, { name = it }, "e.g. Marina heights apartment")
            Spacer(Modifier.height(12.dp))
            LabeledTextField(
                "Contact Information", contact, { contact = it }, "Owner / Site head phone number",
                keyboardType = KeyboardType.Phone
            )

            Spacer(Modifier.height(20.dp))
            SectionLabel("Project Site Address")
            LabeledTextField("Street Address", street, { street = it }, "Street line 1")

            Spacer(Modifier.height(32.dp))
            PrimaryButton(
                text = "Next",
                onClick = {
                    viewModel.projectName = name
                    viewModel.contactInfo = contact
                    viewModel.street = street
                    onNext()
                }
            )
        }
    }
}
