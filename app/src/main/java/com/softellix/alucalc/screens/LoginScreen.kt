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
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Trigger navigation only when authentication is successful
    LaunchedEffect(viewModel.authSuccess) {
        if (viewModel.authSuccess) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Login to continue your window estimations.", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Text("PHONE NUMBER", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        AluTextField(value = phone, onValueChange = { phone = it }, placeholder = "Enter 10-digit number")

        Spacer(modifier = Modifier.height(16.dp))

        Text("PASSWORD", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        AluTextField(value = password, onValueChange = { password = it }, placeholder = "Enter password", isPassword = true)

        // Error Message Display
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = error, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AluPrimaryButton(
                text = "Login",
                onClick = { viewModel.login(phone, password) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account? ", color = Color.Gray)
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}