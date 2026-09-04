package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.components.AluOutlinedButton
import com.softellix.alucalc.data.remote.RetrofitClient
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray

@Composable
fun SettingsScreen(
    onLogoutClick: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf("ENGLISH") }

    Scaffold(
        bottomBar = {
            AluBottomNavigation(
                selectedTab = 3,
                onTabSelected = onTabSelected
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text("Settings & Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Manage user preferences and account settings", color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(1.dp, BorderGray, CircleShape)
                            .background(Color(0xFFF0F0F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User", tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Doe Windows • Fabricator", color = Color.Gray, fontSize = 12.sp)
                        Text("+91 9999999999", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("PREFERENCES", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Language Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = "Lang", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Application Language", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("ENGLISH", "HINDI", "GUJARATI").forEach { lang ->
                            FilterChip(
                                selected = selectedLanguage == lang,
                                onClick = {
                                    selectedLanguage = lang
                                    Toast.makeText(context, "Language updated to $lang", Toast.LENGTH_SHORT).show()
                                },
                                label = { Text(lang, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Server Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Storage, contentDescription = "Backend", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Backend Environment", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(RetrofitClient.baseUrl, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            AluOutlinedButton(
                text = "Logout",
                onClick = {
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    onLogoutClick()
                }
            )
        }
    }
}
