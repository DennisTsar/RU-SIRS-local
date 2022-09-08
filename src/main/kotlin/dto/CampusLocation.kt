package dto

import kotlinx.serialization.Serializable

@Serializable
data class CampusLocation(
    val code: String,
    val description: String
)