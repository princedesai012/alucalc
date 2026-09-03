package com.alucalc.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.composed
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluCardBorder
import com.alucalc.app.ui.theme.AluTextSecondary

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AluBlack)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = AluTextSecondary) },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = AluCardBorder,
                focusedBorderColor = AluBlack,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AluBlack, contentColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AluCardBorder),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = AluBlack),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

@Composable
fun StepHeader(step: Int, totalSteps: Int, title: String) {
    val percent = (step * 100) / totalSteps
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("STEP $step OF $totalSteps", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AluTextSecondary)
            Text("$percent% Complete", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AluTextSecondary)
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { step.toFloat() / totalSteps },
            color = AluBlack,
            trackColor = AluCardBorder,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = AluTextSecondary,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun SelectableCard(
    title: String,
    subtitle: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) AluBlack else AluCardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AluTextSecondary)
            RadioIndicator(selected)
        }
        subtitle?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AluBlack)
        }
    }
}

fun Modifier.clickableSimple(onClick: () -> Unit): Modifier = this.composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
}

@Composable
fun RadioIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .border(2.dp, if (selected) AluBlack else AluCardBorder, CircleShape)
            .padding(3.dp)
    ) {
        if (selected) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(AluBlack, CircleShape)
            )
        }
    }
}
