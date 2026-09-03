package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.viewmodels.AuthViewModel

@Composable
fun CreateAccountScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Trigger navigation only when authentication is successful
    LaunchedEffect(viewModel.authSuccess) {
        if (viewModel.authSuccess) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Register AluCalc to start your window estimations.", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Text("PERSONAL INFO", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        AluTextField(value = name, onValueChange = { name = it }, placeholder = "Enter full name")
        Spacer(modifier = Modifier.height(12.dp))

        AluTextField(value = businessName, onValueChange = { businessName = it }, placeholder = "Enter business name")
        Spacer(modifier = Modifier.height(12.dp))

        AluTextField(value = phone, onValueChange = { phone = it }, placeholder = "Enter 10-digit number")
        Spacer(modifier = Modifier.height(12.dp))

        AluTextField(value = password, onValueChange = { password = it }, placeholder = "Minimum 8 characters", isPassword = true)

        // Error Message Display
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = error, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ", color = Color.Gray)
            Text(
                text = "Login",
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AluPrimaryButton(
                text = "Register",
                onClick = { viewModel.register(name, businessName, phone, password) }
            )
        }
    }
}