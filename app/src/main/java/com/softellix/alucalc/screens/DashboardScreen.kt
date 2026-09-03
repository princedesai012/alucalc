package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark

@Composable
fun DashboardScreen(onNewProjectClick: () -> Unit) {
    Scaffold(
        bottomBar = { AluBottomNavigation() },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Top Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome back,", color = Color.Gray, fontSize = 14.sp)
                    Text("John Doe", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(
                    onClick = { /* TODO: Profile */ },
                    modifier = Modifier
                        .border(1.dp, BorderGray, CircleShape)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Large "New Estimation Project" Card
            Button(
                onClick = onNewProjectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = PrimaryDark,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("New Estimation Project", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Calculate dimensions, profiles, and tracks", color = Color.LightGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Two Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = "Recent Projects",
                    value = "12 Projects",
                    icon = Icons.Outlined.Folder,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Report History",
                    value = "8 Reports",
                    icon = Icons.Outlined.Description,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(icon, contentDescription = title, tint = Color.Gray)
                Icon(Icons.Default.ArrowForward, contentDescription = "Go", tint = Color.LightGray, modifier = Modifier.size(16.dp))
            }
            Column {
                Text(title, color = Color.Gray, fontSize = 12.sp)
                Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}