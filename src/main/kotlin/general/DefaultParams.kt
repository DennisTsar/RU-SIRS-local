package general

@Suppress("MemberVisibilityCanBePrivate")
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

data class SemYear(val semester: Semester, val year: Int)

enum class Semester(val num: Int) {
    Spring(1), Fall(9); // !! Note that this order matters !!

    fun other(): Semester = if (this == Spring) Fall else Spring
}

enum class Campus {
    NB, CM, NK
}

enum class LevelOfStudy {
    U, G
}