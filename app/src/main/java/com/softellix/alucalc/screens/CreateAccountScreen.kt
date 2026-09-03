package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray

@Composable
fun CreateAccountScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    // State variables to hold user input
    var name by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Register AluCalc to start your window estimations.", color = androidx.compose.ui.graphics.Color.Gray)

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

        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ", color = androidx.compose.ui.graphics.Color.Gray)
            Text(
                text = "Login",
                fontWeight = FontWeight.Bold,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AluPrimaryButton(
            text = "Register",
            onClick = {
                if (name.isBlank() || phone.isBlank()) {
                    Toast.makeText(context, "Please enter your name and phone number", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    onRegisterSuccess()
                }
            }
        )
    }
}