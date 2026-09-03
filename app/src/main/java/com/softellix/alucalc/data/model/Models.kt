package com.softellix.alucalc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String? = null,
    val city: String? = null,
    val state: String? = null
)

@Serializable
data class RegisterRequest(
    val name: String,
    val phone: String,
    val businessName: String,
    val password: String,
    val confirmPassword: String,
    val address: Address? = null
)

@Serializable
data class LoginRequest(
    val phone: String,
    val password: String
)

@Serializable
data class User(
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val businessName: String? = null
)

@Serializable
data class LoginResponse(
    val message: String? = null,
    val accessToken: String? = null,
    val user: User? = null
)

@Serializable
data class RefreshTokenRequest(
    val userUuid: String
)

@Serializable
data class LogoutRequest(
    val userUuid: String
)

@Serializable
data class ForgotPasswordRequest(
    val phone: String
)

@Serializable
data class VerifyOtpRequest(
    val phone: String,
    val otp: String
)

@Serializable
data class OtpResponse(
    val message: String? = null,
    val resetToken: String? = null
)

@Serializable
data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String,
    val confirmPassword: String
)

@Serializable
data class UpdateLanguageRequest(
    val language: String
)

@Serializable
data class CreateProjectRequest(
    val projectName: String,
    val projectAddress: String? = null,
    val contactInformation: String? = null,
    val profileType: String? = null, // Options: MM40, MM60, MM65
    val windowType: String? = null  // Options: REGULAR, SLIM
)

@Serializable
data class ProjectResponse(
    val id: String,
    val projectName: String,
    val projectNumber: Int? = null,
    val projectAddress: String? = null,
    val contactInformation: String? = null,
    val profileType: String? = null,
    val windowType: String? = null
)

@Serializable
data class UpdateProjectProfileRequest(
    val profileType: String // Options: MM40, MM60, MM65
)

@Serializable
data class AddWindowRequest(
    val width: Double,      // Always decimal inches (e.g. 36.0)
    val height: Double,     // Always decimal inches (e.g. 48.0)
    val quantity: Int,
    val trackType: String   // Options: TRACK_TWO, TRACK_THREE, TRACK_FOUR
)

@Serializable
data class CalculationPiece(
    val name: String? = null,
    val value: Double? = null,
    val pieces: Int? = null,
    val totalPieces: Int? = null
)

@Serializable
data class CalculationDetail(
    val handleHeight: CalculationPiece? = null,
    val interlockHeight: CalculationPiece? = null,
    val topAndSide: CalculationPiece? = null,
    val parts: List<CalculationPiece> = emptyList()
)

@Serializable
data class WindowCalculationResponse(
    val id: String? = null,
    val windowNumber: Int? = null,
    val width: Double,
    val height: Double,
    val quantity: Int,
    val trackType: String,
    val calculation: CalculationDetail? = null
)

@Serializable
data class ProjectReportResponse(
    val projectId: String,
    val projectName: String,
    val projectAddress: String? = null,
    val contactInformation: String? = null,
    val createdDate: String? = null,
    val selectedProfile: String? = null,
    val windows: List<WindowCalculationResponse> = emptyList()
)
