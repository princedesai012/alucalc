package com.softellix.alucalc.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.ProjectViewModel

@Composable
fun SettingsScreen(
    viewModel: ProjectViewModel,
    onLogoutClick: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }

    var storedName by remember { mutableStateOf("") }
    var storedPhone by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf(LanguageManager.currentLanguage) }

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
        val name = tokenStore.getUserName()
        if (!name.isNullOrBlank()) storedName = name
        val phone = tokenStore.getUserPhone()
        if (!phone.isNullOrBlank()) storedPhone = phone
    }

    val user = viewModel.currentUser
    val displayName = user?.name?.ifBlank { null } ?: storedName.takeIf { it != "User" && it.isNotBlank() } ?: "User"
    val displayPhone = user?.phone?.ifBlank { null } ?: storedPhone

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
                .padding(16.dp)
        ) {
            Text(LanguageManager.tr("settings_title"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("manage_settings_sub"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Card (Contains ONLY User Name and Mobile Number)
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryFont)
                        Spacer(modifier = Modifier.height(2.dp))
                        if (displayPhone.isNotBlank()) {
                            Text(displayPhone, color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(LanguageManager.tr("preferences"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
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
                        Text(LanguageManager.tr("app_language"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
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
                                    LanguageManager.setLanguage(lang)
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

            Spacer(modifier = Modifier.height(20.dp))

            // HELP & SUPPORT Section (Fully Localized)
            Text(LanguageManager.tr("help_support_title"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Customer Care Contact
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917984620052"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Contact: +91 7984620052", Toast.LENGTH_LONG).show()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Phone, contentDescription = LanguageManager.tr("customer_care"), tint = PrimaryDark)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(LanguageManager.tr("customer_care"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
                                Text("+91 7984620052", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Text(LanguageManager.tr("call_action"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderGray)

                    // WhatsApp Support
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    val url = "https://api.whatsapp.com/send?phone=919714865744"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp: +91 9714865744", Toast.LENGTH_LONG).show()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Message, contentDescription = LanguageManager.tr("whatsapp_support"), tint = Color(0xFF25D366))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(LanguageManager.tr("whatsapp_support"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryFont)
                                Text("+91 9714865744", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Text(LanguageManager.tr("chat_action"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25D366))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            AluOutlinedButton(
                text = LanguageManager.tr("logout"),
                onClick = {
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    onLogoutClick()
                }
            )
        }
    }
}
