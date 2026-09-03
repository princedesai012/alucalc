package com.alucalc.app.data.repository

import com.alucalc.app.data.model.*
import com.alucalc.app.data.remote.RetrofitClient

// Thin wrapper around ApiService. Each call returns Result so screens can show
// errors without try/catch scattered everywhere.
class AluRepository {

    private val api = RetrofitClient.apiService

    private suspend fun <T> safeCall(block: suspend () -> retrofit2.Response<T>): Result<T> {
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

    suspend fun register(req: RegisterRequest) = safeCall { api.register(req) }
    suspend fun login(req: LoginRequest) = safeCall { api.login(req) }

    suspend fun getProjects() = safeCall { api.getProjects() }
    suspend fun createProject(req: CreateProjectRequest) = safeCall { api.createProject(req) }
    suspend fun getProject(id: Long) = safeCall { api.getProject(id) }

    suspend fun addWindow(projectId: Long, item: WindowItem) = safeCall { api.addWindow(projectId, item) }
    suspend fun deleteWindow(projectId: Long, windowId: Long) = safeCall { api.deleteWindow(projectId, windowId) }

    suspend fun calculate(projectId: Long) = safeCall { api.calculate(projectId) }
    suspend fun getReport(projectId: Long) = safeCall { api.getReport(projectId) }
    suspend fun getReportHistory() = safeCall { api.getReportHistory() }
}
