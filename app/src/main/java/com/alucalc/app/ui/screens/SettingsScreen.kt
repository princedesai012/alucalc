@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.alucalc.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alucalc.app.ui.components.AluBottomNav
import com.alucalc.app.ui.components.BottomTab
import com.alucalc.app.ui.components.PrimaryButton

@Composable
fun SettingsScreen(
    userName: String,
    onLogout: () -> Unit,
    onSelectTab: (BottomTab) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) },
        bottomBar = { AluBottomNav(current = BottomTab.SETTINGS, onSelect = onSelectTab) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Text("Signed in as $userName", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            PrimaryButton(text = "Log Out", onClick = onLogout)
        }
    }
}
