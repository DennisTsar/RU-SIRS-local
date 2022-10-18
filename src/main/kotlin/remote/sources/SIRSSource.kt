package remote.sources

import Entry
import School
import data.sirs_courseFilter.SIRSCourseFilterResult
import general.DefaultParams
import general.SemYear
import general.Semester
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import misc.substringAfterBefore
import pmap
import remote.interfaces.EntriesSource
import remote.interfaces.RemoteApi
import remote.interfaces.SchoolsMapSource

class SIRSSource(private val API_KEY: String) : RemoteApi, SchoolsMapSource, EntriesSource {
    private val sirsClient = client.config {
        defaultRequest {
            header("Cookie", API_KEY)
            url("https://sirs.ctaar.rutgers.edu")
        }
    }

    suspend fun getEntriesByDeptOrCourse(
        semYear: SemYear,
        school: String,
        dept: String,
        course: String? = null,
    ): List<Entry> {
        return sirsClient.get("/index.php") {
            parameter("survey[semester]", semYear.semester)
            parameter("survey[year]", semYear.year)
            parameter("survey[school]", school)
            parameter("survey[dept]", dept)
            parameter("survey[course]", course)
            parameter("mode", "course")
        }.mapSIRSPageToEntries()
    }

    suspend fun getByLastName(
        lastname: String,
        semester: Semester? = null,
        year: Int? = null,
        school: String? = null,
        dept: String? = null,
    ): List<List<String>> {
        return sirsClient.get("/index.php") {
            parameter("survey[lastname]", lastname)
            parameter("survey[semester]", semester)
            parameter("survey[year]", year)
            parameter("survey[school]", school)
            parameter("survey[dept]", dept)
            parameter("mode", "name")
        }.body<String>().substringAfterBefore("</strong></li><li>", "</a></li></ul>").split("</a></li><li>")
            .map { // this should be an object but no use for it rn
                val id = it.substringAfterBefore("record%5D=", "'")
                val (sem, course, prof) = it.substringAfter(">").split(" &mdash; ")
                listOf(id, sem, course, prof)
            }
    }

    suspend fun getByID(id: String): List<Entry> {
        return sirsClient.get("/index.php") {
            parameter("survey[record]", id)
            parameter("mode", "name")
        }.mapSIRSPageToEntries()
    }

    suspend fun getSchoolsOrDepts(
        semYear: SemYear = DefaultParams.lastSirsSem,
        school: String = "",
    ): SIRSCourseFilterResult {
        return sirsClient.config {
            install(ContentNegotiation) {
                serialization(ContentType.Text.Html, Json)
            }
        }.get("/courseFilter.php") {
            parameter("survey[semester]", semYear.semester)
            parameter("survey[year]", semYear.year)
            parameter("survey[school]", school)
//            parameter("mode", "course") // sent by default but doesn't do anything?
        }.body()
    }

    suspend fun getSpecificSchoolsMap(semYear: SemYear): Map<String, School> {
        return getSchoolsOrDepts(semYear).schools.pmap { (code, name) -> // this works as each sublist is always length 2 w/ this format
            val depts = getSchoolsOrDepts(semYear, code).depts.toSet()
            code to School(code, name, depts)
        }.toMap()
    }

    private suspend fun HttpResponse.mapSIRSPageToEntries(): List<Entry> =
        body<String>().split("\t\t<strong>  ").drop(1).map(::Entry)

    suspend fun getSchoolsMapUsingSOC(
        socSource: SOCSource = SOCSource(),
        semesters: List<SemYear> = DefaultParams.sirsRange,
    ): Map<String, School> {
        val sirsRes = getSchoolsOrDepts(semesters.last()) // schools not here don't get counted
        // codes of schools with non-empty names
        val sirsSchools = sirsRes.schools.filter { it[1].isNotEmpty() }.map { it[0] }

        return socSource.getSOCData().units
            .filter { it.code in sirsSchools }
            .pmap { school ->
                val depts = semesters.flatMap {
                    getSchoolsOrDepts(SemYear(it.semester, it.year), school.code).depts
                }.filter { it.isNotEmpty() }.toSortedSet()

                School(
                    school.code,
                    school.description
                        .replace("(UGrad)", "(U)")
                        .replace("(Grad)", "(G)")
                        .replace("(UG)", "(U)"),
                    depts,
                )
            }.associateBy { it.code }
    }

    suspend fun getCompleteSchoolsMap(semesters: List<SemYear> = DefaultParams.sirsRange): Map<String, School> {
        return semesters
            .pmap { getSpecificSchoolsMap(it) }
            .reduce { acc, map ->
                val newSchools = map.filterKeys { it !in acc.keys }
                acc.mapValues { (code, school) ->
                    val otherSchool = map[code] ?: return@mapValues school
                    school.copy(depts = (school.depts + otherSchool.depts).sorted().toSet())
                } + newSchools
            }
    }

    suspend fun getEntriesOverSems(
        school: String,
        dept: String,
        semesters: List<SemYear> = DefaultParams.sirsRange,
    ): List<Entry> {
        return semesters.pmap { getEntriesByDeptOrCourse(it, school, dept) }.flatten()
    }

    override suspend fun getSchoolsMap(): Map<String, School> = getSchoolsMapUsingSOC()

    override suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry> =
        getEntriesOverSems(school, dept)
}