package dto

import kotlinx.serialization.Serializable

@Serializable
data class UnitMajor(
    val majorCode: String,
    val unitCode: String
)