package general

import kotlinx.serialization.Serializable

@Serializable
data class School(
    val code: String,
    val name: String,
    val depts: Set<String>,
)