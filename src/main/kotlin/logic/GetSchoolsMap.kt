package logic

import api.Repository
import dto.sirs_courseFilter.SIRSCourseFilterResult
import dto.soc.SOCData
import general.School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.pmap
import misc.substringAfterBefore
import java.io.File

object GetSchoolsMap {
    suspend fun fromSIRS(
        repository: Repository,
        semester: String = "Fall",
        year: Int = 2021,
    ): Map<String, School> {
        return repository.getSchoolsOrDepts(semester, year)
            .substringAfterBefore("\"schools\":[[", "]]").split("],[")
            .pmap {
                val (code, name) = it.removeSurrounding("\"").split("\",\"")
                    .zipWithNext().first()// Fancy (read: bad) way of doing Pair(x[0],x[1])
                val depts = repository.getSchoolsOrDepts(semester, year, code)
                    .substringAfterBefore("\"depts\":[\"", "\"]}").split("\",\"").toSet()
                code to School(code, name, depts)
            }.toMap()
    }

    suspend fun fromSIRSAndSOC(
        repository: Repository,
        semester: String = "Fall",
        year: Int = 2020,
    ): Map<String, School> {
        val sirsRes = repository.getSchoolsOrDepts(semester, year)
        val socRes = repository.getSOCData()
            .substringAfterBefore("<div id=\"initJsonData\" style=\"display:none;\">", "</div>")
        val sirsData = Json.decodeFromString<SIRSCourseFilterResult>(sirsRes)
        val socData = Json.decodeFromString<SOCData>(socRes)

        val sirsSchools = sirsData.schools.filter { it[1].isNotEmpty() }.map { it[0] }

        val requiredParensNames = socData.units
            .map { it.description }
            .groupBy { it.substringBefore(" (") }
            .filterValues { it.size > 1 }
            .values
            .flatten()

        return socData.units.toList()
            .pmap {
                if (it.code !in sirsSchools)
                    return@pmap null
                val depts = (4028..4044).flatMap { num ->
                    val mySemester = if (num % 2 == 0) "Spring" else "Fall"
                    val myYear = num / 2
                    repository.getSchoolsOrDepts(mySemester, myYear, it.code).run {
                        Json.decodeFromString<SIRSCourseFilterResult>(this).depts
                    }
                }.filter { i -> i.isNotEmpty() }
                    .toSortedSet()

                it.code to (it.description to depts)

                val newName = it.description.run {
                    if (this !in requiredParensNames)
                        substringBefore(" (")
                    else this
                }.replace("(UGrad)", "(U)").replace("(Grad)", "(G)")
                it.code to School(it.code, newName, depts)
            }.filterNotNull().toMap()
    }

    suspend fun fromGit(repository: Repository): Map<String, School> =
        Json.decodeFromString(repository.getSchoolDeptsMapFromGit())

    fun fromLocalCode(): Map<String, School> = MASTER_SCHOOL_DEPTS_MAP

    fun fromLocalFile(readDir: String = "extra-json-data/schoolDeptsMap.json"): Map<String, School> =
        Json.decodeFromString(File(readDir).readText())
}