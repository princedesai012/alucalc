package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is UiState.Success -> { onLoggedIn(); viewModel.resetState() }
            is UiState.Error -> snackbarHostState.showSnackbar(s.message)
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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

            Spacer(Modifier.height(32.dp))
            Text("Welcome Back", style = MaterialTheme.typography.headlineSmall)
            Text("Login to continue your estimations.", fontSize = 13.sp, color = Color.Gray)

            Spacer(Modifier.height(24.dp))
            LabeledTextField(
                "Phone Number", phone, { phone = it }, "Enter 10-digit number",
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(12.dp))
            LabeledTextField("Password", password, { password = it }, "Enter your password", isPassword = true)

            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                text = "Login",
                loading = uiState is UiState.Loading,
                onClick = { viewModel.login(phone, password) }
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", fontSize = 13.sp, color = Color.Gray)
                Text(
                    "Register",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AluBlack,
                    modifier = Modifier.clickableSimple(onGoToRegister)
                )
            }
        }
    }
}
