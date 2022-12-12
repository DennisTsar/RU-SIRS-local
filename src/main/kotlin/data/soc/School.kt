package data.soc


import kotlinx.serialization.Serializable

@Serializable
data class School(
    val campus: String,
    val code: String,
    val description: String,
    val homeCampus: String,
    val level: String,
)