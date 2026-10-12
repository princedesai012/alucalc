package com.softellix.alucalc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

@Composable
fun OtpInputBoxes(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    otpLength: Int = 6
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Single underlying BasicTextField that manages typing without losing focus
        BasicTextField(
            value = otpValue,
            onValueChange = { newValue ->
                if (newValue.length <= otpLength && newValue.all { it.isDigit() }) {
                    onOtpChange(newValue)
                }
            },
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {}
        )

        // Visual 6 boxes representing the OTP characters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { focusRequester.requestFocus() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until otpLength) {
                val char = otpValue.getOrNull(i)?.toString() ?: ""
                val isFocused = otpValue.length == i || (otpValue.length == otpLength && i == otpLength - 1)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(horizontal = 4.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(
                            width = if (isFocused) 2.dp else 1.dp,
                            color = if (isFocused) Color(0xFF2563EB) else BorderGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}
