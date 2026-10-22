package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.components.ErrorSnackbar
import com.softellix.alucalc.data.model.InvoiceItemRequest
import com.softellix.alucalc.data.model.InvoiceRequest
import com.softellix.alucalc.data.model.ProjectResponse
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.InvoiceViewModel
import com.softellix.alucalc.viewmodels.ProjectViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ProjectLineState(
    val id: String = java.util.UUID.randomUUID().toString(),
    val selectedProject: ProjectResponse? = null,
    val sqFeet: String = "",
    val pricePerSqFt: String = "",
    val isDropdownExpanded: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    viewModel: InvoiceViewModel,
    projectViewModel: ProjectViewModel,
    onBackClick: () -> Unit,
    onGenerateInvoiceClick: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        projectViewModel.fetchProjectsList()
    }

    var partyName by remember { mutableStateOf("") }
    var notesAndTerms by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }

    var projectLines by remember { mutableStateOf(listOf(ProjectLineState())) }

    // Dynamic Calculation
    val projectsBaseTotal = projectLines.sumOf { line ->
        val sq = line.sqFeet.toDoubleOrNull() ?: 0.0
        val pr = line.pricePerSqFt.toDoubleOrNull() ?: 0.0
        sq * pr
    }

    val baseTotal = projectsBaseTotal
    val discVal = discount.toDoubleOrNull() ?: 0.0
    val netTotal = (baseTotal - discVal).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryFont)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.tr("create_invoice"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(LanguageManager.tr("party_name"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(6.dp))
        AluTextField(value = partyName, onValueChange = { partyName = it }, placeholder = LanguageManager.tr("enter_name"))

        Spacer(modifier = Modifier.height(24.dp))

        // --- Dynamic Project Selection Rows ---
        projectLines.forEachIndexed { index, line ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Project Selection ${index + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
                        if (projectLines.size > 1) {
                            IconButton(
                                onClick = { projectLines = projectLines.filter { it.id != line.id } },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Remove", tint = Color.Red, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = line.isDropdownExpanded,
                        onExpandedChange = { expanded ->
                            projectLines = projectLines.map { if (it.id == line.id) it.copy(isDropdownExpanded = expanded) else it }
                        }
                    ) {
                        OutlinedTextField(
                            value = line.selectedProject?.projectName ?: "No Project Selected",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = line.isDropdownExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = line.isDropdownExpanded,
                            onDismissRequest = {
                                projectLines = projectLines.map { if (it.id == line.id) it.copy(isDropdownExpanded = false) else it }
                            },
                            modifier = Modifier.background(Color.White)
                        ) {
                            projectViewModel.projectsList.forEach { project ->
                                DropdownMenuItem(
                                    text = { Text(project.projectName) },
                                    onClick = {
                                        // Set project and hide dropdown
                                        projectLines = projectLines.map { 
                                            if (it.id == line.id) it.copy(selectedProject = project, isDropdownExpanded = false) else it 
                                        }
                                        
                                        // Automatically fetch and calculate SqFt for selected project
                                        coroutineScope.launch {
                                            val sqFt = projectViewModel.getProjectSquareFeet(project.id)
                                            if (sqFt > 0) {
                                                val formatted = String.format(Locale.US, "%.2f", sqFt)
                                                projectLines = projectLines.map { 
                                                    if (it.id == line.id) it.copy(sqFeet = formatted) else it 
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(LanguageManager.tr("sq_feet"), fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            AluTextField(value = line.sqFeet, onValueChange = { newVal ->
                                projectLines = projectLines.map { if (it.id == line.id) it.copy(sqFeet = newVal) else it }
                            }, placeholder = "0.0", isNumeric = true)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(LanguageManager.tr("price_per_sqft"), fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            AluTextField(value = line.pricePerSqFt, onValueChange = { newVal ->
                                projectLines = projectLines.map { if (it.id == line.id) it.copy(pricePerSqFt = newVal) else it }
                            }, placeholder = "₹ 0.0", isNumeric = true)
                        }
                    }
                }
            }
        }

        TextButton(onClick = { projectLines = projectLines + ProjectLineState() }) {
            Text("+ Add Another Project", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(LanguageManager.tr("discount"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(6.dp))
        AluTextField(value = discount, onValueChange = { discount = it }, placeholder = "0.00", isNumeric = true)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Notes & Terms (Optional)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = notesAndTerms,
            onValueChange = { notesAndTerms = it },
            placeholder = { Text("e.g. 50% advance payment required...", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = BorderGray,
                focusedBorderColor = Color(0xFF2563EB)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Total summary box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Base Amount:", color = Color.Gray, fontSize = 14.sp)
                    Text("₹ %.2f".format(baseTotal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
                }
                if (discVal > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount (Fixed Amount):", color = Color.Gray, fontSize = 14.sp)
                        Text("- ₹ %.2f".format(discVal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Red)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(LanguageManager.tr("total_amount"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                    Text("₹ %.2f".format(netTotal), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF16A34A))
                }
            }
        }

        viewModel.errorMessage?.let { errorMsg ->
            Spacer(modifier = Modifier.height(16.dp))
            ErrorSnackbar(message = errorMsg)
            LaunchedEffect(errorMsg) {
                kotlinx.coroutines.delay(5000)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AluPrimaryButton(
                text = LanguageManager.tr("create_invoice"),
                onClick = {
                    if (partyName.isBlank()) {
                        Toast.makeText(context, "Party Name is required", Toast.LENGTH_SHORT).show()
                        return@AluPrimaryButton
                    }
                    
                    val items = mutableListOf<InvoiceItemRequest>()
                    
                    // Map Projects
                    projectLines.forEach { line ->
                        val sq = line.sqFeet.toDoubleOrNull() ?: 0.0
                        val pr = line.pricePerSqFt.toDoubleOrNull() ?: 0.0
                        if (line.selectedProject != null && sq > 0 && pr > 0) {
                            items.add(
                                InvoiceItemRequest(
                                    projectId = line.selectedProject.id,
                                    squareFeet = sq,
                                    pricePerSquareFeet = pr
                                )
                            )
                        }
                    }
                    
                    if (items.isEmpty()) {
                        Toast.makeText(context, "Please select at least one project with valid inputs.", Toast.LENGTH_SHORT).show()
                        return@AluPrimaryButton
                    }
                    
                    val req = InvoiceRequest(
                        partyName = partyName,
                        invoiceDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
                        note = notesAndTerms.ifBlank { null },
                        discountType = if (discVal > 0) "AMOUNT" else "PERCENTAGE", // Fixed to match Spring Boot Enum 'AMOUNT'
                        discountValue = if (discVal > 0) discVal else 0.0,
                        items = items
                    )
                    
                    viewModel.createInvoice(req, onSuccess = { invoiceId ->
                        Toast.makeText(context, "Invoice Created!", Toast.LENGTH_SHORT).show()
                        onGenerateInvoiceClick(invoiceId)
                    })
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
