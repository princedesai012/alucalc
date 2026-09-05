package com.softellix.alucalc.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softellix.alucalc.data.model.*
import com.softellix.alucalc.data.repository.AluRepository
import kotlinx.coroutines.launch

data class WindowItem(
    val id: Int,
    val apiId: String? = null,
    val heightDisplay: String, // e.g. "48" 0d"
    val widthDisplay: String,  // e.g. "36" 4d"
    val decimalHeight: Double, // e.g. 48.0
    val decimalWidth: Double,  // e.g. 36.5
    val track: String,
    val qty: String,
    val calculation: CalculationDetail? = null
)

class ProjectViewModel(
    private val repository: AluRepository = AluRepository()
) : ViewModel() {

    // Step 1: Project Details
    var projectName by mutableStateOf("")
    var contactInfo by mutableStateOf("")
    var streetAddress by mutableStateOf("")
    var currentProjectId by mutableStateOf<String?>(null)

    // Step 2: Profile Selection
    var selectedProfile by mutableStateOf("40mm")

    // Step 3: Added Windows
    private val _addedWindows = mutableStateListOf<WindowItem>()
    val addedWindows: List<WindowItem> get() = _addedWindows

    // User & Preference State
    var currentUser by mutableStateOf<User?>(null)
        private set
    var currentLanguage by mutableStateOf("ENGLISH")
        private set

    // Report and List State
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var reportResponse by mutableStateOf<ProjectReportResponse?>(null)
        private set
    var projectsList by mutableStateOf<List<ProjectResponse>>(emptyList())
        private set
    var recentProjectsList by mutableStateOf<List<ProjectResponse>>(emptyList())
        private set

    // --- Helper functions ---

    fun addWindow(
        heightDisplay: String,
        widthDisplay: String,
        decimalHeight: Double,
        decimalWidth: Double,
        track: String,
        qty: String
    ) {
        val newId = (_addedWindows.maxOfOrNull { it.id } ?: 0) + 1
        val item = WindowItem(newId, null, heightDisplay, widthDisplay, decimalHeight, decimalWidth, track, qty)
        _addedWindows.add(item)

        val pId = currentProjectId
        if (pId != null) {
            viewModelScope.launch {
                val req = AddWindowRequest(
                    width = decimalWidth,
                    height = decimalHeight,
                    quantity = qty.toIntOrNull() ?: 1,
                    trackType = mapTrackType(track)
                )
                repository.addWindow(pId, req).onSuccess { res ->
                    val index = _addedWindows.indexOf(item)
                    if (index != -1) {
                        _addedWindows[index] = item.copy(apiId = res.id, calculation = res.calculation)
                    }
                }
            }
        }
    }

    fun removeWindow(window: WindowItem) {
        _addedWindows.remove(window)
        val pId = currentProjectId
        val wId = window.apiId
        if (pId != null && wId != null) {
            viewModelScope.launch {
                repository.deleteWindow(pId, wId)
            }
        }
    }

    fun createProjectOnBackend(onSuccess: () -> Unit = {}) {
        if (projectName.isBlank()) {
            onSuccess()
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val req = CreateProjectRequest(
                projectName = projectName,
                projectAddress = streetAddress.ifBlank { "Surat, Gujarat" },
                contactInformation = contactInfo.ifBlank { "+91 9999999999" },
                profileType = mapProfileType(selectedProfile),
                windowType = "REGULAR"
            )
            repository.createProject(req).onSuccess { project ->
                currentProjectId = project.id
                onSuccess()
            }.onFailure { err ->
                errorMessage = err.localizedMessage
                onSuccess() // Fallback to local mode
            }
            isLoading = false
        }
    }

    fun updateProfileOnBackend(profile: String, onSuccess: () -> Unit = {}) {
        selectedProfile = profile
        val pId = currentProjectId
        if (pId != null) {
            viewModelScope.launch {
                repository.updateProjectProfile(pId, UpdateProjectProfileRequest(mapProfileType(profile)))
            }
        }
        onSuccess()
    }

    fun loadProjectReport(projectId: String) {
        currentProjectId = projectId
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getProjectReport(projectId).onSuccess { report ->
                reportResponse = report
                projectName = report.projectName
                contactInfo = report.contactInformation ?: ""
                streetAddress = report.projectAddress ?: ""
                selectedProfile = when (report.selectedProfile) {
                    "MM60" -> "60mm"
                    "MM65" -> "65mm"
                    else -> "40mm"
                }
                _addedWindows.clear()
                report.windows.forEachIndexed { idx, win ->
                    _addedWindows.add(
                        WindowItem(
                            id = idx + 1,
                            apiId = win.id,
                            heightDisplay = "${win.height}\"",
                            widthDisplay = "${win.width}\"",
                            decimalHeight = win.height,
                            decimalWidth = win.width,
                            track = win.trackType,
                            qty = win.quantity.toString(),
                            calculation = win.calculation
                        )
                    )
                }
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun fetchReportOnBackend() {
        val pId = currentProjectId ?: return
        viewModelScope.launch {
            isLoading = true
            repository.getProjectReport(pId).onSuccess { report ->
                reportResponse = report
                if (report.windows.isNotEmpty()) {
                    _addedWindows.clear()
                    report.windows.forEachIndexed { idx, win ->
                        _addedWindows.add(
                            WindowItem(
                                id = idx + 1,
                                apiId = win.id,
                                heightDisplay = "${win.height}\"",
                                widthDisplay = "${win.width}\"",
                                decimalHeight = win.height,
                                decimalWidth = win.width,
                                track = win.trackType,
                                qty = win.quantity.toString(),
                                calculation = win.calculation
                            )
                        )
                    }
                }
            }.onFailure { err ->
                errorMessage = err.localizedMessage
            }
            isLoading = false
        }
    }

    fun fetchProjectsList() {
        viewModelScope.launch {
            isLoading = true
            repository.getProjects().onSuccess { list ->
                projectsList = list
            }
            isLoading = false
        }
    }

    fun fetchRecentProjects() {
        viewModelScope.launch {
            isLoading = true
            repository.getRecentProjects().onSuccess { list ->
                recentProjectsList = list
            }
            isLoading = false
        }
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            repository.getMe().onSuccess { res ->
                currentUser = res.user
            }
        }
    }

    fun updateLanguage(lang: String, onResult: (Boolean) -> Unit) {
        currentLanguage = lang
        viewModelScope.launch {
            repository.updateLanguage(UpdateLanguageRequest(lang)).onSuccess {
                onResult(true)
            }.onFailure {
                onResult(true) // Local state fallback
            }
        }
    }

    fun resetProject() {
        projectName = ""
        contactInfo = ""
        streetAddress = ""
        selectedProfile = "40mm"
        currentProjectId = null
        reportResponse = null
        _addedWindows.clear()
    }

    private fun mapProfileType(profile: String): String {
        return when (profile) {
            "60mm" -> "MM60"
            "65mm" -> "MM65"
            else -> "MM40"
        }
    }

    private fun mapTrackType(track: String): String {
        return when (track) {
            "3T" -> "TRACK_THREE"
            "4T" -> "TRACK_FOUR"
            else -> "TRACK_TWO"
        }
    }
}
