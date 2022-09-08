package dto

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val code: String,
    val description: String
)