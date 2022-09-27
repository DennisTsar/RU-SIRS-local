package dto

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val campusCode: String,
    val campusLocations: List<DescriptionHolder>,
    val coreCodes: List<CoreCode>,
    val courseDescription: String,
    val courseNotes: String,
    val courseNumber: String,
    val courseString: String,
    val credits: Double?,
    val creditsObject: DescriptionHolder,
    val expandedTitle: String,
    val level: String,
    val mainCampus: String,
    val offeringUnitCode: String,
    val offeringUnitTitle: String?,
    val openSections: Int,
    val preReqNotes: String,
    val school: DescriptionHolder,
    val sections: List<Section>,
    val subject: String,
    val subjectDescription: String,
    val subjectGroupNotes: String,
    val subjectNotes: String,
    val supplementCode: String,
    val synopsisUrl: String,
    val title: String,
    val unitNotes: String,
)