package com.softellix.alucalc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont

@Composable
fun OtpInputBoxes(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    otpLength: Int = 6
) {
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until otpLength) {
            val char = otpValue.getOrNull(i)?.toString() ?: ""
            val isFocused = otpValue.length == i || (otpValue.length == otpLength && i == otpLength - 1)

            BasicTextField(
                value = char,
                onValueChange = { newValue ->
                    if (newValue.length <= 1) {
                        val newOtp = buildString {
                            append(otpValue.take(i))
                            append(newValue)
                            if (i + 1 < otpValue.length) {
                                append(otpValue.substring(i + 1))
                            }
                        }.take(otpLength)
                        
                        onOtpChange(newOtp)
                        
                        // Auto-advance focus
                        if (newValue.isNotEmpty() && i < otpLength - 1) {
                            focusRequesters[i + 1].requestFocus()
                        }
                    } else if (newValue.isEmpty() && char.isNotEmpty()) {
                        // Handle backspace properly
                        val newOtp = otpValue.removeRange(i, i + 1)
                        onOtpChange(newOtp)
                        if (i > 0) {
                            focusRequesters[i - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f) // Ensure it's a perfect square
                    .padding(horizontal = 4.dp)
                    .focusRequester(focusRequesters[i])
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = if (isFocused) Color(0xFF2563EB) else BorderGray, // Blue border when focused
                        shape = RoundedCornerShape(8.dp)
                    ),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }
}
