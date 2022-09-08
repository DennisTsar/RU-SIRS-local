package dto

import kotlinx.serialization.Serializable

@Serializable
data class Section(
    val campusCode: String,
    val comments: List<Comment>,
    val commentsText: String,
    val crossListedSections: List<CrossListedSections>,
    val crossListedSectionsText: String,
    val examCode: String,
    val examCodeText: String,
    val honorPrograms: List<HonorProgram>,
    val index: String,
    val instructors: List<Instructor>,
    val instructorsText: String,
    val legendKey: String?,//yes
    val majors: List<Major>,
    val meetingTimes: List<MeetingTime>,
    val minors: List<Minor>,
    val number: String,
    val openStatus: Boolean,
    val openStatusText: String,
    val openToText: String,
    val printed: String,
    val sectionCampusLocations: List<SectionCampusLocation>,
    val sectionCourseType: String,
    val sectionEligibility: String,
    val sectionNotes: String,
    val sessionDatePrintIndicator: String,
    val sessionDates: String?,//yes
    val specialPermissionAddCode: String?,
    val specialPermissionAddCodeDescription: String?,
    val specialPermissionDropCode: String?,//yes
    val specialPermissionDropCodeDescription: String?,//yes
    val subtitle: String,
    val subtopic: String,
    val unitMajors: List<UnitMajor>
)