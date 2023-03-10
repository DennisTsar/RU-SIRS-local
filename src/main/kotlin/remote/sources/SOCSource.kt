package remote.sources

import data.Campus
import data.Semester
import data.soc.Course
import data.soc.SOCData
import data.substringAfterBefore
import general.DefaultParams
import io.ktor.client.call.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import remote.RemoteApi

private const val SOC_BASE_URL = "https://sis.rutgers.edu/soc"

class SOCSource : RemoteApi {
    suspend fun getCourses(
        semester: Semester = DefaultParams.semester,
        campus: Campus = DefaultParams.campus,
    ): List<Course> {
        return client.config {
            install(ContentEncoding) {
                gzip(1.0F)
            }
            install(ContentNegotiation) {
                json()
            }
        }.get("$SOC_BASE_URL/api/courses.json") {
            parameter("term", semester.type.num)
            parameter("year", semester.year)
            parameter("campus", campus)
        }.body()
    }

    suspend fun getSOCData(): SOCData {
        return client.get(SOC_BASE_URL).body<String>()
            .substringAfterBefore("<div id=\"initJsonData\" style=\"display:none;\">", "</div>")
            .let { Json.decodeFromString(it) }
    }
}