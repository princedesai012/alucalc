package com.softellix.alucalc.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.utils.LanguageManager

@Composable
fun AluBottomNavigation(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = LanguageManager.tr("home")) },
            label = { Text(LanguageManager.tr("home"), fontSize = 11.sp) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryDark,
                selectedTextColor = PrimaryDark,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Folder, contentDescription = LanguageManager.tr("all_projects")) },
            label = { Text(LanguageManager.tr("all_projects"), fontSize = 11.sp) },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryDark,
                selectedTextColor = PrimaryDark,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Description, contentDescription = LanguageManager.tr("reports")) },
            label = { Text(LanguageManager.tr("reports"), fontSize = 11.sp) },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryDark,
                selectedTextColor = PrimaryDark,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Receipt, contentDescription = LanguageManager.tr("invoices")) },
            label = { Text(LanguageManager.tr("invoices"), fontSize = 11.sp) },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryDark,
                selectedTextColor = PrimaryDark,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = LanguageManager.tr("settings")) },
            label = { Text(LanguageManager.tr("settings"), fontSize = 11.sp) },
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryDark,
                selectedTextColor = PrimaryDark,
                indicatorColor = Color.Transparent
            )
        )
    }
}
