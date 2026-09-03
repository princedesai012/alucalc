package com.alucalc.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val businessName: String,
    val phoneNumber: String,
    val password: String,
    val street: String,
    val city: String,
    val state: String
)

@Serializable
data class LoginRequest(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val userId: Long,
    val name: String
)

@Serializable
data class ProjectSummary(
    val id: Long,
    val name: String,
    val estimatorName: String? = null,
    val createdDate: String? = null,
    val totalWindows: Int = 0
)

@Serializable
data class CreateProjectRequest(
    val name: String,
    val contactInfo: String,
    val street: String
)

@Serializable
data class ProjectDetail(
    val id: Long,
    val name: String,
    val contactInfo: String? = null,
    val street: String? = null,
    val windows: List<WindowItem> = emptyList()
)

enum class ProfileType {
    @SerialName("REGULAR_40") REGULAR_40,
    @SerialName("REGULAR_60") REGULAR_60,
    @SerialName("SLIM_65") SLIM_65
}

enum class TrackType {
    @SerialName("2T") TWO_T,
    @SerialName("3T") THREE_T,
    @SerialName("4T") FOUR_T
}

@Serializable
data class WindowItem(
    val id: Long? = null,
    val profileType: String,
    val trackType: String,
    val height: Int,
    val width: Int,
    val quantity: Int
)

@Serializable
data class MaterialBreakdownRow(
    val serial: Int,
    val profile: String,
    val height: Int,
    val width: Int,
    val trackType: String,
    val quantity: Int
)

@Serializable
data class ReportResponse(
    val projectId: Long,
    val projectName: String,
    val reportCode: String? = null,
    val estimatorName: String? = null,
    val createdDate: String? = null,
    val totalWindows: Int,
    val materialBreakdown: List<MaterialBreakdownRow>,
    val totalAluminumMeters: Double,
    val totalGlassSqm: Double,
    val pdfUrl: String? = null
)
