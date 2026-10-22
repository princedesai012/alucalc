package com.softellix.alucalc.data.model

import kotlinx.serialization.SerialName
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
data class RegistrationOtpResponse(
    val verified: Boolean? = null,
    val message: String? = null
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
    val windowType: String? = null,
    @SerialName("created_at") val createdAtSnake: String? = null,
    @SerialName("createdAt") val createdAtCamel: String? = null,
    @SerialName("createdDate") val createdDateCamel: String? = null,
    @SerialName("created_date") val createdDateSnake: String? = null,
    @SerialName("updatedAt") val updatedAtCamel: String? = null,
    @SerialName("updated_at") val updatedAtSnake: String? = null,
    @SerialName("date") val dateField: String? = null
) {
    val rawDate: String?
        get() = createdAtSnake ?: createdAtCamel ?: updatedAtCamel ?: updatedAtSnake ?: createdDateCamel ?: createdDateSnake ?: dateField
}

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
    val projectNumber: Int? = null,
    val projectAddress: String? = null,
    val contactInformation: String? = null,
    @SerialName("created_at") val createdAtSnake: String? = null,
    @SerialName("createdAt") val createdAtCamel: String? = null,
    @SerialName("createdDate") val createdDateCamel: String? = null,
    @SerialName("created_date") val createdDateSnake: String? = null,
    @SerialName("updatedAt") val updatedAtCamel: String? = null,
    @SerialName("updated_at") val updatedAtSnake: String? = null,
    val selectedProfile: String? = null,
    val windows: List<WindowCalculationResponse> = emptyList()
) {
    val createdDate: String?
        get() = createdAtSnake ?: createdAtCamel ?: updatedAtCamel ?: updatedAtSnake ?: createdDateCamel ?: createdDateSnake
}

@Serializable
data class InvoiceItemRequest(
    val projectId: String? = null,
    val description: String? = null,
    val squareFeet: Double? = null,
    val pricePerSquareFeet: Double? = null,
    val amount: Double? = null
)

@Serializable
data class InvoiceRequest(
    val partyName: String,
    val invoiceDate: String? = null,
    val terms: String? = null,
    val note: String? = null,
    val discountType: String? = null, // PERCENTAGE, FIXED
    val discountValue: Double? = null,
    val items: List<InvoiceItemRequest>
)

@Serializable
data class InvoiceUser(
    val name: String? = null,
    val businessName: String? = null,
    val phone: String? = null,
    val address: String? = null
)

@Serializable
data class InvoiceItemResponse(
    val id: String? = null,
    val projectId: String? = null,
    val projectName: String? = null,
    val description: String? = null,
    val squareFeet: Double? = null,
    val pricePerSquareFeet: Double? = null,
    val amount: Double? = null
)

@Serializable
data class InvoiceResponse(
    val id: String,
    val invoiceNumber: String? = null,
    val partyName: String,
    val invoiceDate: String? = null,
    val terms: String? = null,
    val subtotal: Double? = null,
    val discountType: String? = null,
    val discountValue: Double? = null,
    val discountAmount: Double? = null,
    val finalTotal: Double? = null,
    val note: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val items: List<InvoiceItemResponse> = emptyList(),
    val user: InvoiceUser? = null
)
