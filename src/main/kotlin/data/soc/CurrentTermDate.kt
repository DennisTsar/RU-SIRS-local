package data.soc


import kotlinx.serialization.Serializable

@Serializable
data class CurrentTermDate(
    val campus: String,
    val date: String,
    val term: Int,
    val year: Int,
)