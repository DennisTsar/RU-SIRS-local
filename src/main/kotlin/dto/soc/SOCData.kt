package dto.soc


import kotlinx.serialization.Serializable

@Serializable
data class SOCData(
    val buildings: List<Building>,
    val coreCodes: List<CoreCode>,
    val currentTermDate: CurrentTermDate,
    val subjects: List<Subject>,
    val units: List<Unit>
)