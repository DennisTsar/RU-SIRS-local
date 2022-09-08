package dto

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val campusCode: String,
    val campusLocations: List<CampusLocation>,
    val coreCodes: List<CoreCode>,
    val courseDescription: String,
    val courseNotes: String,
    val courseNumber: String,
    val courseString: String,
    val credits: Double?,
    val creditsObject: CreditsObject,
    val expandedTitle: String,
    val level: String,
    val mainCampus: String,
    val offeringUnitCode: String,
    val offeringUnitTitle: String?,//yes
    val openSections: Int,
    val preReqNotes: String,
    val school: School,
    val sections: List<Section>,
    val subject: String,
    val subjectDescription: String,
    val subjectGroupNotes: String,
    val subjectNotes: String,
    val supplementCode: String,
    val synopsisUrl: String,
    val title: String,
    val unitNotes: String
)