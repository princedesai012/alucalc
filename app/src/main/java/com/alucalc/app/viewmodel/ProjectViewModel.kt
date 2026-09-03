package com.alucalc.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alucalc.app.data.model.*
import com.alucalc.app.data.repository.AluRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Holds state for the whole "New Project" wizard: details -> profile -> windows -> report,
 * matching the 3-step flow in the Figma design.
 */
class ProjectViewModel(private val repository: AluRepository) : ViewModel() {

    private val _projects = MutableStateFlow<List<ProjectSummary>>(emptyList())
    val projects: StateFlow<List<ProjectSummary>> = _projects

    private val _reportHistory = MutableStateFlow<List<ReportResponse>>(emptyList())
    val reportHistory: StateFlow<List<ReportResponse>> = _reportHistory

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    // Wizard in-progress data
    var projectId: Long? = null
    var projectName: String = ""
    var contactInfo: String = ""
    var street: String = ""

    var selectedProfile: String = "REGULAR_40" // REGULAR_40, REGULAR_60, SLIM_65
    var selectedTrack: String = "2T"           // 2T, 3T, 4T

    private val _windows = MutableStateFlow<List<WindowItem>>(emptyList())
    val windows: StateFlow<List<WindowItem>> = _windows

    private val _report = MutableStateFlow<ReportResponse?>(null)
    val report: StateFlow<ReportResponse?> = _report

    fun loadProjects() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            repository.getProjects()
                .onSuccess { _projects.value = it; _state.value = UiState.Success }
                .onFailure { _state.value = UiState.Error(it.message ?: "Could not load projects") }
        }
    }

    fun loadReportHistory() {
        viewModelScope.launch {
            repository.getReportHistory().onSuccess { _reportHistory.value = it }
        }
    }

    fun createProject(onDone: () -> Unit) {
        if (projectName.isBlank()) {
            _state.value = UiState.Error("Project name is required")
            return
        }
        _state.value = UiState.Loading
        viewModelScope.launch {
            repository.createProject(CreateProjectRequest(projectName, contactInfo, street))
                .onSuccess {
                    projectId = it.id
                    _state.value = UiState.Success
                    onDone()
                }
                .onFailure { _state.value = UiState.Error(it.message ?: "Could not create project") }
        }
    }

    fun addWindowLocally(height: Int, width: Int, quantity: Int) {
        val item = WindowItem(
            profileType = selectedProfile,
            trackType = selectedTrack,
            height = height,
            width = width,
            quantity = quantity
        )
        _windows.value = _windows.value + item
    }

    fun removeWindowAt(index: Int) {
        _windows.value = _windows.value.toMutableList().also { it.removeAt(index) }
    }

    fun saveAndCalculate(onDone: () -> Unit) {
        val id = projectId
        if (id == null) {
            _state.value = UiState.Error("Missing project — please restart the wizard")
            return
        }
        if (_windows.value.isEmpty()) {
            _state.value = UiState.Error("Add at least one window before calculating")
            return
        }
        _state.value = UiState.Loading
        viewModelScope.launch {
            // Push each window to backend, then trigger calculation
            var failed = false
            for (w in _windows.value) {
                repository.addWindow(id, w).onFailure { failed = true }
            }
            if (failed) {
                _state.value = UiState.Error("Some windows failed to save")
                return@launch
            }
            repository.calculate(id)
                .onSuccess {
                    _report.value = it
                    _state.value = UiState.Success
                    onDone()
                }
                .onFailure { _state.value = UiState.Error(it.message ?: "Calculation failed") }
        }
    }

    fun resetWizard() {
        projectId = null
        projectName = ""
        contactInfo = ""
        street = ""
        selectedProfile = "REGULAR_40"
        selectedTrack = "2T"
        _windows.value = emptyList()
        _report.value = null
        _state.value = UiState.Idle
    }

    fun resetState() { _state.value = UiState.Idle }
}
