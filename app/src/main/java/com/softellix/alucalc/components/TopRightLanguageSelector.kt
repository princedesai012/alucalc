package com.softellix.alucalc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager

@Composable
fun TopRightLanguageSelector(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("ENGLISH", "HINDI", "GUJARATI")

    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Language, contentDescription = "Change Language", tint = PrimaryFont)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            languages.forEach { lang ->
                val isSelected = LanguageManager.currentLanguage == lang
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = lang, 
                            color = if (isSelected) Color(0xFF2563EB) else PrimaryFont,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        ) 
                    },
                    onClick = {
                        LanguageManager.setLanguage(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}
