package com.softellix.alucalc.data.remote

import com.softellix.alucalc.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 1. Authentication Endpoints
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body body: RefreshTokenRequest): Response<LoginResponse>

    @POST("api/auth/logout")
    suspend fun logout(@Body body: LogoutRequest): Response<ResponseBody>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<ResponseBody>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): Response<OtpResponse>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<ResponseBody>

    @GET("api/auth/me")
    suspend fun getMe(): Response<LoginResponse>

    // 2. User Preferences
    @PUT("api/users/me/language")
    suspend fun updateLanguage(@Body body: UpdateLanguageRequest): Response<ResponseBody>

    // 3. Projects Endpoints
    @POST("api/projects")
    suspend fun createProject(@Body body: CreateProjectRequest): Response<ProjectResponse>

    @GET("api/projects")
    suspend fun getProjects(): Response<List<ProjectResponse>>

    @GET("api/projects/recent")
    suspend fun getRecentProjects(): Response<List<ProjectResponse>>

    @GET("api/projects/search")
    suspend fun searchProjects(@Query("query") query: String): Response<List<ProjectResponse>>

    @GET("api/projects/{projectId}")
    suspend fun getProject(@Path("projectId") projectId: String): Response<ProjectResponse>

    @PATCH("api/projects/{projectId}")
    suspend fun updateProjectDetails(@Path("projectId") projectId: String, @Body body: CreateProjectRequest): Response<ProjectResponse>

    @PUT("api/projects/{projectId}/profile")
    suspend fun updateProjectProfile(@Path("projectId") projectId: String, @Body body: UpdateProjectProfileRequest): Response<ProjectResponse>

    @DELETE("api/projects/{projectId}")
    suspend fun deleteProject(@Path("projectId") projectId: String): Response<Unit>

    @GET("api/projects/{projectId}/calculate")
    suspend fun calculateProject(@Path("projectId") projectId: String): Response<List<WindowCalculationResponse>>

    @GET("api/projects/{projectId}/report")
    suspend fun getProjectReport(@Path("projectId") projectId: String): Response<ProjectReportResponse>

    // 4. Windows Endpoints
    @POST("api/projects/{projectId}/windows")
    suspend fun addWindow(@Path("projectId") projectId: String, @Body body: AddWindowRequest): Response<WindowCalculationResponse>

    @GET("api/projects/{projectId}/windows")
    suspend fun getWindows(@Path("projectId") projectId: String): Response<List<WindowCalculationResponse>>

    @PUT("api/projects/{projectId}/windows/{windowId}")
    suspend fun updateWindow(@Path("projectId") projectId: String, @Path("windowId") windowId: String, @Body body: AddWindowRequest): Response<WindowCalculationResponse>

    @DELETE("api/projects/{projectId}/windows/{windowId}")
    suspend fun deleteWindow(@Path("projectId") projectId: String, @Path("windowId") windowId: String): Response<Unit>
}
