package general

import SemYear
import Semester

//@Suppress("MemberVisibilityCanBePrivate")
object DefaultParams {
    val semYear = SemYear(Semester.Spring, 2023)
    val campus = Campus.NB
    val levelOfStudy = LevelOfStudy.U

    // sirs stuff
    val lastSirsSem = SemYear(Semester.Spring, 2022)
    private val firstSemYear = SemYear(Semester.Fall, 2014)
    val sirsRange = (firstSemYear.year..lastSirsSem.year).flatMap { year ->
        Semester.values().map { sem -> SemYear(sem, year) }
    }.drop(firstSemYear.semester.ordinal).dropLast(lastSirsSem.semester.other().ordinal)
}

enum class Campus {
    NB, CM, NK
}

enum class LevelOfStudy {
    U, G
}