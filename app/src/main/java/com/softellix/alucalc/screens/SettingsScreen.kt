package com.softellix.alucalc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
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
import com.softellix.alucalc.data.remote.TokenStore
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun SettingsScreen(
    viewModel: ProjectViewModel,
    onLogoutClick: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }

    var userName by remember { mutableStateOf("User") }
    var userPhone by remember { mutableStateOf("") }
    var userBusiness by remember { mutableStateOf("Fabricator") }
    var selectedLanguage by remember { mutableStateOf(viewModel.currentLanguage) }

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
        val name = tokenStore.getUserName()
        if (!name.isNullOrBlank()) userName = name
        val phone = tokenStore.getUserPhone()
        if (!phone.isNullOrBlank()) userPhone = phone
        val biz = tokenStore.getUserBusiness()
        if (!biz.isNullOrBlank()) userBusiness = biz
    }

    val user = viewModel.currentUser
    val displayName = user?.name ?: userName
    val displayPhone = user?.phone ?: userPhone
    val displayBusiness = user?.businessName ?: userBusiness

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
            Text("Settings & Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
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
                            .size(52.dp)
                            .border(1.dp, BorderGray, CircleShape)
                            .background(Color(0xFFF0F0F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User", tint = Color.Gray, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryFont)
                        Text("$displayBusiness • Fabricator", color = Color.Gray, fontSize = 12.sp)
                        if (displayPhone.isNotBlank()) {
                            Text(displayPhone, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("PREFERENCES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
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
                        Text("Application Language", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
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
                                    viewModel.updateLanguage(lang) {
                                        Toast.makeText(context, "App language updated to $lang", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                label = { Text(lang, fontSize = 12.sp) }
                            )
                        }
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
