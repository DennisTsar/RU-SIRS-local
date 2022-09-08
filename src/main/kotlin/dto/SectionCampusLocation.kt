package dto

import kotlinx.serialization.Serializable

@Serializable
data class SectionCampusLocation(
    val code: String,
    val description: String
)