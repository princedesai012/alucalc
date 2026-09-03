@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.alucalc.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alucalc.app.ui.components.AluBottomNav
import com.alucalc.app.ui.components.BottomTab
import com.alucalc.app.ui.components.clickableSimple
import com.alucalc.app.ui.theme.AluCardBorder
import com.alucalc.app.viewmodel.ProjectViewModel

@Composable
fun ProjectsListScreen(
    viewModel: ProjectViewModel,
    onOpenProject: (Long) -> Unit,
    onSelectTab: (BottomTab) -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadProjects() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Projects") }) },
        bottomBar = { AluBottomNav(current = BottomTab.PROJECTS, onSelect = onSelectTab) }
    ) { padding ->
        if (projects.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No projects yet. Create one from Home.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(projects) { project ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, AluCardBorder, RoundedCornerShape(12.dp))
                            .clickableSimple { onOpenProject(project.id) }
                            .padding(16.dp)
                    ) {
                        Text(project.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${project.estimatorName ?: ""}  ·  ${project.totalWindows} Windows",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
