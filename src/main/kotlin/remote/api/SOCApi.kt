package remote.api

import general.Campus
import general.DefaultParams
import general.SemYear
import io.ktor.client.call.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.substringAfterBefore
import remote.dto.Course
import remote.dto.soc.SOCData
import remote.interfaces.Api

private const val SOC_BASE_URL = "https://sis.rutgers.edu/soc"

class SOCApi : Api {
    suspend fun getCourses(
        semYear: SemYear = DefaultParams.semYear,
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
            parameter("term", semYear.semester.num)
            parameter("year", semYear.year)
            parameter("campus", campus)
        }.body()
    }

    suspend fun getSOCData(): SOCData {
        return client.get(SOC_BASE_URL).body<String>()
            .substringAfterBefore("<div id=\"initJsonData\" style=\"display:none;\">", "</div>")
            .let { Json.decodeFromString(it) }
    }
}

