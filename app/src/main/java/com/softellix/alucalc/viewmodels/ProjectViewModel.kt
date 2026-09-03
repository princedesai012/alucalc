package com.softellix.alucalc.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Data class to hold individual window details
data class WindowItem(
    val id: Int,
    val height: String,
    val width: String,
    val track: String,
    val qty: String
)

class ProjectViewModel : ViewModel() {
    // Step 1: Project Details
    var projectName by mutableStateOf("")
    var contactInfo by mutableStateOf("")
    var streetAddress by mutableStateOf("")

    // Step 2: Profile Selection
    var selectedProfile by mutableStateOf("40mm")

    // Step 3: Added Windows (using a special observable list for Compose)
    private val _addedWindows = mutableStateListOf<WindowItem>()
    val addedWindows: List<WindowItem> get() = _addedWindows

    // Helper functions to manage the list
    fun addWindow(height: String, width: String, track: String, qty: String) {
        val newId = (_addedWindows.maxOfOrNull { it.id } ?: 0) + 1
        _addedWindows.add(WindowItem(newId, height, width, track, qty))
    }

    fun removeWindow(window: WindowItem) {
        _addedWindows.remove(window)
    }

    // Clear data when starting a new project
    fun resetProject() {
        projectName = ""
        contactInfo = ""
        streetAddress = ""
        selectedProfile = "40mm"
        _addedWindows.clear()
    }
}