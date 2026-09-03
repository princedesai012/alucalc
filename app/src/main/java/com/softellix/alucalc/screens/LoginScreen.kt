package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        Spacer(modifier = Modifier.weight(1f))

        AluPrimaryButton(
            text = "Login",
            onClick = {
                // TODO: Wire up the Retrofit API call here later
                onLoginSuccess()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text link to navigate back to Register
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