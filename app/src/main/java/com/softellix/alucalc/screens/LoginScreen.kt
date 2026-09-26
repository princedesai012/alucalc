package com.softellix.alucalc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.softellix.alucalc.components.ErrorSnackbar
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit = {}
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
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(LanguageManager.tr("welcome_back"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Text(LanguageManager.tr("login_sub"), color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Text(LanguageManager.tr("phone_number"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(8.dp))
        AluTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = LanguageManager.tr("enter_phone"),
            isNumeric = true,
            maxLength = 10
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(LanguageManager.tr("password"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        Spacer(modifier = Modifier.height(8.dp))
        AluTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = LanguageManager.tr("pin_placeholder"),
            isPassword = true,
            isNumeric = true,
            maxLength = 4
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = LanguageManager.tr("forgot_password"),
                color = PrimaryFont,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToForgotPassword() }
            )
        }

        // Error Message Banner
        viewModel.errorMessage?.let { error ->
            LaunchedEffect(error) {
                kotlinx.coroutines.delay(5000)
                viewModel.clearErrorMessage()
            }
            Spacer(modifier = Modifier.height(16.dp))
            ErrorSnackbar(message = error)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AluPrimaryButton(
                text = LanguageManager.tr("login"),
                onClick = { viewModel.login(phone, password) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = LanguageManager.tr("no_account") + " ", color = Color.Gray)
            Text(
                text = LanguageManager.tr("register"),
                color = PrimaryFont,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
