package dto

import kotlinx.serialization.Serializable

@Serializable
data class School(
    val code: String,
    val description: String
)