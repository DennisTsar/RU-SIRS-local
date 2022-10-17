package data.soc


import kotlinx.serialization.Serializable

@Serializable
data class Unit(
    val campus: String,
    val code: String,
    val description: String,
    val homeCampus: String,
    val level: String,
)