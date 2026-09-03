package com.softellix.alucalc.data.repository

import com.softellix.alucalc.data.model.*
import com.softellix.alucalc.data.remote.RetrofitClient
import retrofit2.Response

class AluRepository {

    private val api = RetrofitClient.apiService

    private suspend fun <T> safeCall(block: suspend () -> Response<T>): Result<T> {
        return try {
            val response = block()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response from server"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string() ?: "request failed"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "could not reach server"}"))
        }
    }

    // 1. Authentication
    suspend fun register(req: RegisterRequest) = safeCall { api.register(req) }
    suspend fun login(req: LoginRequest) = safeCall { api.login(req) }
    suspend fun refreshToken(req: RefreshTokenRequest) = safeCall { api.refreshToken(req) }
    suspend fun logout(req: LogoutRequest) = safeCall { api.logout(req) }
    suspend fun forgotPassword(req: ForgotPasswordRequest) = safeCall { api.forgotPassword(req) }
    suspend fun verifyOtp(req: VerifyOtpRequest) = safeCall { api.verifyOtp(req) }
    suspend fun resetPassword(req: ResetPasswordRequest) = safeCall { api.resetPassword(req) }
    suspend fun getMe() = safeCall { api.getMe() }

    // 2. User Preferences
    suspend fun updateLanguage(req: UpdateLanguageRequest) = safeCall { api.updateLanguage(req) }

    // 3. Projects
    suspend fun createProject(req: CreateProjectRequest) = safeCall { api.createProject(req) }
    suspend fun getProjects() = safeCall { api.getProjects() }
    suspend fun getRecentProjects() = safeCall { api.getRecentProjects() }
    suspend fun searchProjects(query: String) = safeCall { api.searchProjects(query) }
    suspend fun getProject(projectId: String) = safeCall { api.getProject(projectId) }
    suspend fun updateProjectDetails(projectId: String, req: CreateProjectRequest) = safeCall { api.updateProjectDetails(projectId, req) }
    suspend fun updateProjectProfile(projectId: String, req: UpdateProjectProfileRequest) = safeCall { api.updateProjectProfile(projectId, req) }
    suspend fun deleteProject(projectId: String) = safeCall { api.deleteProject(projectId) }
    suspend fun calculateProject(projectId: String) = safeCall { api.calculateProject(projectId) }
    suspend fun getProjectReport(projectId: String) = safeCall { api.getProjectReport(projectId) }

    // 4. Windows
    suspend fun addWindow(projectId: String, req: AddWindowRequest) = safeCall { api.addWindow(projectId, req) }
    suspend fun getWindows(projectId: String) = safeCall { api.getWindows(projectId) }
    suspend fun updateWindow(projectId: String, windowId: String, req: AddWindowRequest) = safeCall { api.updateWindow(projectId, windowId, req) }
    suspend fun deleteWindow(projectId: String, windowId: String) = safeCall { api.deleteWindow(projectId, windowId) }
}
