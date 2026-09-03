package com.alucalc.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.theme.AluBlack
import com.alucalc.app.ui.theme.AluTextTertiary

enum class BottomTab(val label: String) {
    HOME("Home"), PROJECTS("Projects"), REPORTS("Reports"), SETTINGS("Settings")
}

@Composable
fun AluBottomNav(current: BottomTab, onSelect: (BottomTab) -> Unit) {
    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        val items = listOf(
            Triple(BottomTab.HOME, Icons.Filled.Home, "Home"),
            Triple(BottomTab.PROJECTS, Icons.Filled.Folder, "Projects"),
            Triple(BottomTab.REPORTS, Icons.Filled.Description, "Reports"),
            Triple(BottomTab.SETTINGS, Icons.Filled.Settings, "Settings")
        )
        items.forEach { (tab, icon, label) ->
            NavigationBarItem(
                selected = current == tab,
                onClick = { onSelect(tab) },
                icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(22.dp)) },
                label = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AluBlack,
                    selectedTextColor = AluBlack,
                    unselectedIconColor = AluTextTertiary,
                    unselectedTextColor = AluTextTertiary,
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    }
}
