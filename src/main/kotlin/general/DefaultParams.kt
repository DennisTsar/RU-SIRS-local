package general

import Campus
import LevelOfStudy
import Semester
import SemesterType

object DefaultParams {
    val semester = Semester(SemesterType.Spring, 2023)
    val campus = Campus.NB
    val levelOfStudy = LevelOfStudy.U

    // sirs stuff
    val lastSirsSem = Semester(SemesterType.Spring, 2022)
    private val firstSemester = Semester(SemesterType.Fall, 2014)
    val sirsRange = (firstSemester.year..lastSirsSem.year).flatMap { year ->
        SemesterType.values().map { sem -> Semester(sem, year) }
    }.drop(firstSemester.type.ordinal).dropLast(lastSirsSem.type.other().ordinal)
}
