package com.softellix.alucalc.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark

// 1. The Track Type Selector (2T, 3T, 4T)
@Composable
fun TrackTypeSelector(selectedTrack: String, onTrackSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        listOf("2T", "3T", "4T").forEach { track ->
            val isSelected = selectedTrack == track
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (isSelected) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onTrackSelected(track) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = track,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.Black else Color.Gray
                )
            }
        }
    }
}

// 2. The Added Window Card (with the trash icon)
@Composable
fun AddedWindowCard(windowNumber: Int, width: String, height: String, track: String, qty: String, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Window #$windowNumber: $height × $width mm",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color.Gray,
                modifier = Modifier.clickable { onDelete() }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "$track Track • Qty: $qty", color = Color.Gray, fontSize = 12.sp)
    }
}

// 3. The Outlined Button
@Composable
fun AluOutlinedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderGray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryDark)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}