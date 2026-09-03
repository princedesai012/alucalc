package com.softellix.alucalc.data.remote

import com.softellix.alucalc.data.model.AuthResponse
import com.softellix.alucalc.data.model.CreateProjectRequest
import com.softellix.alucalc.data.model.LoginRequest
import com.softellix.alucalc.data.model.ProjectDetail
import com.softellix.alucalc.data.model.ProjectSummary
import com.softellix.alucalc.data.model.RegisterRequest
import com.softellix.alucalc.data.model.ReportResponse
import com.softellix.alucalc.data.model.WindowItem
import retrofit2.Response
import retrofit2.http.*

// This interface mirrors the proposed Spring Boot contract.
// If your actual controller paths differ, update the @GET/@POST paths below only —
// nothing else in the app needs to change.
interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @GET("api/projects")
    suspend fun getProjects(): Response<List<ProjectSummary>>

    @POST("api/projects")
    suspend fun createProject(@Body body: CreateProjectRequest): Response<ProjectDetail>

    @GET("api/projects/{id}")
    suspend fun getProject(@Path("id") id: Long): Response<ProjectDetail>

    @POST("api/projects/{id}/windows")
    suspend fun addWindow(@Path("id") projectId: Long, @Body body: WindowItem): Response<WindowItem>

    @DELETE("api/projects/{id}/windows/{windowId}")
    suspend fun deleteWindow(@Path("id") projectId: Long, @Path("windowId") windowId: Long): Response<Unit>

    @POST("api/projects/{id}/calculate")
    suspend fun calculate(@Path("id") projectId: Long): Response<ReportResponse>

    @GET("api/projects/{id}/report")
    suspend fun getReport(@Path("id") projectId: Long): Response<ReportResponse>

    @GET("api/reports")
    suspend fun getReportHistory(): Response<List<ReportResponse>>
}
