package com.softellix.alucalc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softellix.alucalc.components.AluBottomNavigation
import com.softellix.alucalc.components.AluTextField
import com.softellix.alucalc.ui.theme.BackgroundGray
import com.softellix.alucalc.ui.theme.BorderGray
import com.softellix.alucalc.ui.theme.PrimaryDark
import com.softellix.alucalc.ui.theme.PrimaryFont
import com.softellix.alucalc.utils.LanguageManager
import com.softellix.alucalc.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ProjectItemUI(
    val id: String,
    val name: String,
    val address: String,
    val profile: String,
    val totalWindows: Int,
    val date: String
)

fun formatProfileLabel(profileType: String?): String {
    if (profileType.isNullOrBlank()) return LanguageManager.tr("reg_40mm")
    val t = profileType.uppercase()
    return when {
        t.contains("65") || t.contains("SLIM") -> LanguageManager.tr("slim_65mm")
        t.contains("60") -> LanguageManager.tr("reg_60mm")
        else -> LanguageManager.tr("reg_40mm")
    }
}

@Composable
fun ProjectsListScreen(
    viewModel: ProjectViewModel,
    onProjectClick: (String) -> Unit,
    onNewProjectClick: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val todayDate = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()) }

    LaunchedEffect(Unit) {
        viewModel.fetchProjectsList()
    }

    val liveProjects = viewModel.projectsList.map { p ->
        ProjectItemUI(
            id = p.id,
            name = p.projectName,
            address = p.projectAddress ?: "Surat, Gujarat",
            profile = formatProfileLabel(p.profileType),
            totalWindows = p.projectNumber ?: 1,
            date = todayDate
        )
    }

    val filteredProjects = liveProjects.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            AluBottomNavigation(
                selectedTab = 1,
                onTabSelected = onTabSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewProjectClick,
                containerColor = PrimaryDark,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Project")
            }
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(LanguageManager.tr("projects_title"), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Text(LanguageManager.tr("manage_projects_sub"), color = Color.Gray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            AluTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = LanguageManager.tr("search_placeholder")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("${LanguageManager.tr("all_projects_count")} (${filteredProjects.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryFont)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredProjects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) LanguageManager.tr("no_projects_match") else LanguageManager.tr("no_projects_yet"),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(filteredProjects) { project ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProjectClick(project.id) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, BorderGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryFont)
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "View",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(project.address, color = Color.Gray, fontSize = 12.sp)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${project.profile}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryFont,
                                        modifier = Modifier.weight(1f, fill = false),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${project.totalWindows} Windows • ${project.date}",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
