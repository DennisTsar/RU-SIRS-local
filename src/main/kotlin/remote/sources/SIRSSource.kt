package remote.sources

import Entry
import School
import Semester
import data.sirs_courseFilter.SIRSCourseFilterResult
import general.DefaultParams
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import pmap
import remote.EntriesSource
import remote.RemoteApi
import remote.SchoolMapSource
import substringAfterBefore

class SIRSSource(private val API_KEY: String) : RemoteApi, SchoolMapSource, EntriesSource {
    private val sirsClient = client.config {
        defaultRequest {
            header("Cookie", API_KEY)
            url("https://sirs.ctaar.rutgers.edu")
        }
    }

    suspend fun getEntriesByDeptOrCourse(
        semester: Semester,
        school: String,
        dept: String,
        course: String? = null,
    ): List<Entry> {
        return sirsClient.get("/index.php") {
            parameter("survey[semester]", semester.type)
            parameter("survey[year]", semester.year)
            parameter("survey[school]", school)
            parameter("survey[dept]", dept)
            parameter("survey[course]", course)
            parameter("mode", "course")
        }.mapSIRSPageToEntries()
    }

    suspend fun getByLastName(
        lastname: String,
        semester: Semester? = null,
        school: String? = null,
        dept: String? = null,
    ): List<List<String>> {
        return sirsClient.get("/index.php") {
            parameter("survey[lastname]", lastname)
            parameter("survey[semester]", semester?.type)
            parameter("survey[year]", semester?.year)
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
        semester: Semester = DefaultParams.lastSirsSem,
        school: String = "",
    ): SIRSCourseFilterResult {
        return sirsClient.config {
            install(ContentNegotiation) {
                serialization(ContentType.Text.Html, Json)
            }
        }.get("/courseFilter.php") {
            parameter("survey[semester]", semester.type)
            parameter("survey[year]", semester.year)
            parameter("survey[school]", school)
//            parameter("mode", "course") // sent by default but doesn't do anything?
        }.body()
    }

    suspend fun getSpecificSchoolMap(semester: Semester): Map<String, School> {
        return getSchoolsOrDepts(semester).schools.pmap { (code, name) -> // this works as each sublist is always length 2 w/ this format
            val depts = getSchoolsOrDepts(semester, code).depts.toSet()
            code to School(code, name, depts, emptySet())
        }.toMap()
    }

    private suspend fun HttpResponse.mapSIRSPageToEntries(): List<Entry> =
        body<String>().split("\t\t<strong>  ").drop(1).map(::Entry)

    suspend fun getSchoolMapUsingSOC(
        socSource: SOCSource = SOCSource(),
        semesters: List<Semester> = DefaultParams.sirsRange,
    ): Map<String, School> {
        val sirsRes = getSchoolsOrDepts(semesters.last()) // schools not here don't get counted
        // codes of schools with non-empty names
        val sirsSchools = sirsRes.schools.filter { it[1].isNotEmpty() }.map { it[0] }

        return socSource.getSOCData().schools
            .filter { it.code in sirsSchools }
            .pmap { school ->
                val depts = semesters.flatMap {
                    getSchoolsOrDepts(Semester(it.type, it.year), school.code).depts
                }.filter { it.isNotEmpty() }.toSortedSet()

                School(
                    school.code,
                    school.description
                        .replace("(UGrad)", "(U)")
                        .replace("(Grad)", "(G)")
                        .replace("(UG)", "(U)"),
                    depts,
                    school.campuses,
                )
            }.associateBy { it.code }
    }

    suspend fun getCompleteSchoolMap(semesters: List<Semester> = DefaultParams.sirsRange): Map<String, School> {
        return semesters
            .pmap { getSpecificSchoolMap(it) }
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
        semesters: List<Semester> = DefaultParams.sirsRange,
    ): List<Entry> {
        return semesters.pmap { getEntriesByDeptOrCourse(it, school, dept) }.flatten()
    }

    override suspend fun getSchoolMap(): Map<String, School> = getSchoolMapUsingSOC()

    override suspend fun getLatestEntries(school: String, dept: String): List<Entry> =
        getEntriesOverSems(school, dept)
}