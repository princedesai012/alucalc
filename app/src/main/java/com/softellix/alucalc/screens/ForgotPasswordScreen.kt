package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluPrimaryButton
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.passwordResetSuccess) {
        if (viewModel.passwordResetSuccess) {
            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(24.dp)
            .padding(top = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackToLogin) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryFont)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.tr("forgot_password"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!viewModel.otpSent) {
            // STEP 1: Enter Phone Number
            Text(LanguageManager.tr("reset_password"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("reset_sub"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(LanguageManager.tr("phone_number"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(8.dp))
            AluTextField(value = phone, onValueChange = { phone = it }, placeholder = LanguageManager.tr("enter_phone"), isNumeric = true)

            viewModel.errorMessage?.let { err ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(err, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                AluPrimaryButton(
                    text = LanguageManager.tr("send_otp"),
                    onClick = { viewModel.forgotPassword(phone) }
                )
            }
        } else if (!viewModel.otpVerified) {
            // STEP 2: Verify OTP
            Text(LanguageManager.tr("enter_otp"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text("${LanguageManager.tr("enter_otp")} $phone", color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text("OTP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(8.dp))
            AluTextField(value = otp, onValueChange = { otp = it }, placeholder = "123456", isNumeric = true)

            viewModel.errorMessage?.let { err ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(err, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                AluPrimaryButton(
                    text = LanguageManager.tr("verify_otp"),
                    onClick = { viewModel.verifyOtp(phone, otp) }
                )
            }
        } else {
            // STEP 3: Reset Password
            Text(LanguageManager.tr("new_password"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("pin_placeholder"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(LanguageManager.tr("new_password"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(8.dp))
            AluTextField(value = newPassword, onValueChange = { newPassword = it }, placeholder = LanguageManager.tr("pin_placeholder"), isPassword = true, isNumeric = true)

            Spacer(modifier = Modifier.height(16.dp))

            Text(LanguageManager.tr("confirm_password"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(8.dp))
            AluTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = LanguageManager.tr("pin_placeholder"), isPassword = true, isNumeric = true)

            viewModel.errorMessage?.let { err ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(err, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                AluPrimaryButton(
                    text = LanguageManager.tr("reset_password"),
                    onClick = { viewModel.resetPassword(newPassword, confirmPassword) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(LanguageManager.tr("remember_password") + " ", color = Color.Gray, fontSize = 13.sp)
            Text(
                LanguageManager.tr("back_to_login"),
                color = PrimaryFont,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }
}
