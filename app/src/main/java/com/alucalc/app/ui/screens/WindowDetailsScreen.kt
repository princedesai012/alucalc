package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.data.model.WindowItem
import com.alucalc.app.ui.components.LabeledTextField
import com.alucalc.app.ui.components.PrimaryButton
import com.alucalc.app.ui.components.SecondaryButton
import com.alucalc.app.ui.components.SectionLabel
import com.alucalc.app.ui.components.StepHeader
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluCardBorder
import com.alucalc.app.viewmodel.ProjectViewModel
import com.alucalc.app.viewmodel.UiState

private val profileLabel = mapOf(
    "REGULAR_40" to "Regular 40mm",
    "REGULAR_60" to "Regular 60mm",
    "SLIM_65" to "Slim 65mm"
)

@Composable
fun WindowDetailsScreen(
    viewModel: ProjectViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var trackType by remember { mutableStateOf(viewModel.selectedTrack) }

    val windows by viewModel.windows.collectAsState()
    val uiState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is UiState.Success -> { onSaved(); viewModel.resetState() }
            is UiState.Error -> snackbarHostState.showSnackbar(s.message)
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(profileLabel[viewModel.selectedProfile] ?: "Window Details") },
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
            StepHeader(step = 3, totalSteps = 3, title = "")

            Spacer(Modifier.height(4.dp))
            // Preview box
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFF2F2F3), RoundedCornerShape(12.dp))
                    .border(1.dp, AluCardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.GridOn, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text("Scale: Auto Estimate Preview", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledTextField(
                    "Height (mm)", height, { height = it }, "e.g. 1500",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f)
                )
                LabeledTextField(
                    "Width (mm)", width, { width = it }, "e.g. 1200",
                    keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))
            LabeledTextField(
                "Quantity", quantity, { quantity = it }, "e.g. 4",
                keyboardType = KeyboardType.Number
            )

            Spacer(Modifier.height(16.dp))
            SectionLabel("Track Type")
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F3), RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                listOf("2T", "3T", "4T").forEach { track ->
                    val selected = trackType == track
                    Box(
                        Modifier
                            .weight(1f)
                            .background(if (selected) AluBlack else Color.Transparent, RoundedCornerShape(8.dp))
                            .padding(vertical = 10.dp)
                            .then(Modifier),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            track,
                            color = if (selected) Color.White else AluBlack,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { trackType = track }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            SectionLabel("Added Windows (${windows.size})")
            windows.forEachIndexed { index, item ->
                WindowRow(item, index + 1) { viewModel.removeWindowAt(index) }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(12.dp))
            SecondaryButton(
                text = "Add Another Window",
                onClick = {
                    val h = height.toIntOrNull()
                    val w = width.toIntOrNull()
                    val q = quantity.toIntOrNull()
                    if (h != null && w != null && q != null && q > 0) {
                        viewModel.selectedTrack = trackType
                        viewModel.addWindowLocally(h, w, q)
                        height = ""; width = ""; quantity = ""
                    }
                }
            )

            Spacer(Modifier.height(12.dp))
            PrimaryButton(
                text = "Save & Calculate",
                loading = uiState is UiState.Loading,
                onClick = {
                    // Also add whatever is currently in the form if not yet added
                    val h = height.toIntOrNull()
                    val w = width.toIntOrNull()
                    val q = quantity.toIntOrNull()
                    if (h != null && w != null && q != null && q > 0) {
                        viewModel.selectedTrack = trackType
                        viewModel.addWindowLocally(h, w, q)
                    }
                    viewModel.saveAndCalculate(onSaved)
                }
            )
        }
    }
}

@Composable
private fun WindowRow(item: WindowItem, index: Int, onDelete: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, AluCardBorder, RoundedCornerShape(10.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Window #$index: ${item.height} × ${item.width} mm",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Text(
                "${item.trackType} Track · Qty: ${item.quantity}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Remove", tint = Color.Gray)
        }
    }
}
