package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.components.LabeledTextField
import com.alucalc.app.ui.components.PrimaryButton
import com.alucalc.app.ui.components.clickableSimple
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluCardBorder
import com.alucalc.app.viewmodel.AuthViewModel
import com.alucalc.app.viewmodel.UiState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    val uiState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is UiState.Success -> { onRegistered(); viewModel.resetState() }
            is UiState.Error -> { snackbarHostState.showSnackbar(s.message) }
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Logo block matching the Figma splash card
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .size(64.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, AluCardBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.GridView, contentDescription = null, tint = AluBlack)
                }
                Spacer(Modifier.height(10.dp))
                Text("AluCalc", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Aluminium Window Calculator", fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(Modifier.height(28.dp))
            Text("Create Account", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Register AluCalc to start your window estimations.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))
            Text("PERSONAL INFO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Spacer(Modifier.height(10.dp))

            LabeledTextField("Name", name, { name = it }, "Enter full name")
            Spacer(Modifier.height(12.dp))
            LabeledTextField("Business Name", businessName, { businessName = it }, "Enter business name")
            Spacer(Modifier.height(12.dp))
            LabeledTextField(
                "Phone Number", phone, { phone = it }, "Enter 10-digit number",
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(12.dp))
            LabeledTextField(
                "Password", password, { password = it }, "Minimum 8 characters",
                isPassword = true
            )
            Spacer(Modifier.height(12.dp))
            LabeledTextField(
                "Confirm Password", confirmPassword, { confirmPassword = it }, "Repeat password",
                isPassword = true
            )

            Spacer(Modifier.height(20.dp))
            Text("ADDRESS DETAIL", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Spacer(Modifier.height(10.dp))

            LabeledTextField("Street", street, { street = it }, "Apartment, Street name")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledTextField("City", city, { city = it }, "City", modifier = Modifier.weight(1f))
                LabeledTextField("State", state, { state = it }, "State", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                text = "Register",
                loading = uiState is UiState.Loading,
                onClick = {
                    viewModel.register(name, businessName, phone, password, confirmPassword, street, city, state)
                }
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", fontSize = 13.sp, color = Color.Gray)
                Text(
                    "Login",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AluBlack,
                    modifier = Modifier.clickableSimple(onGoToLogin)
                )
            }
        }
    }
}
