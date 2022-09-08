package dto

import kotlinx.serialization.Serializable

@Serializable
data class CreditsObject(
    val code: String,
    val description: String
)