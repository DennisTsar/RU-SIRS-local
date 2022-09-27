package dto.soc


import dto.DescriptionHolder
import kotlinx.serialization.Serializable

@Serializable
data class SOCData(
    val buildings: List<Building>,
    val coreCodes: List<CoreCode>,
    val currentTermDate: CurrentTermDate,
    val subjects: List<DescriptionHolder>,
    val units: List<Unit>,
)