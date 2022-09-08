package dto.soc


import kotlinx.serialization.Serializable

@Serializable
data class CoreCode(
    val campus: String,
    val code: String,
    val description: String,
    val id: Int,
    val label: String
)