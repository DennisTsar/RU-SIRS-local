package dto.soc


import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val code: String,
    val description: String
)