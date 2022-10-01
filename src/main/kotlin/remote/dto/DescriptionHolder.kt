package remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DescriptionHolder(
    val code: String,
    val description: String,
)