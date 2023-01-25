package remote.sources

import data.Campus
import data.LevelOfStudy
import data.SemesterType
import io.ktor.client.call.*
import io.ktor.client.request.*
import remote.RemoteApi

// get API_KEY from "Cookie" header when making this request in browser
// only "JSESSIONID" and "sims-csp" values are needed, but it's fine to include others
class CSPSource(private val API_KEY: String) : RemoteApi {
    constructor(jSessionID: String, simsCSP: String) :
            this("JSESSIONID-$jSessionID; sims-csp=$simsCSP")

    suspend fun getSubjects(semester: SemesterType, year: Int, campus: Campus, levelOfStudy: LevelOfStudy): String {
        return client.get("https://sims.rutgers.edu/csp/getSubjects.json") {
            header("Cookie", API_KEY)
            parameter("semester", "${semester.num}$year")
            parameter("campus", campus)
            parameter("levelOfStudy", levelOfStudy)
        }.body()
    }
}