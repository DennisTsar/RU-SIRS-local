package data.soc


import kotlinx.serialization.Serializable

@Serializable
data class SOCData(
    val buildings: List<Building>,
    val coreCodes: List<CoreCodeSimple>,
    val currentTermDate: CurrentTermDate,
    val subjects: List<DescriptionHolder>,
    val units: List<Unit>,
)