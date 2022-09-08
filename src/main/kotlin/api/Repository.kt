package api

import io.ktor.client.call.*
import io.ktor.client.request.*

private const val SIRS_BASE_URL = "https://sirs.ctaar.rutgers.edu"
private const val GH_BASE_URL = "https://raw.githubusercontent.com/DennisTsar/Rutgers-SIRS/master"

class Repository {
    suspend fun getByDeptOrCourse(
        semester: String,
        year: Int,
        school: String,
        dept: String,
        course: String = "",
    ): String {
        return client.get("$SIRS_BASE_URL/index.php") {
            parameter("survey[semester]", semester)
            parameter("survey[year]", year)
            parameter("survey[school]", school)
            parameter("survey[dept]", dept)
            parameter("survey[course]", course)
            parameter("mode", "course")
        }.body()
    }

    suspend fun getByLastName(lastname: String): String {
        return client.get("$SIRS_BASE_URL/index.php") {
            parameter("survey[lastname]", lastname)
            parameter("mode", "name")
        }.body()
    }

    suspend fun getByID(id: String): String {
        return client.get("$SIRS_BASE_URL/index.php") {
            parameter("survey[record]", id)
            parameter("mode", "name")
        }.body()
    }

    suspend fun getSchoolsOrDepts(semester: String, year: Int, school: String = ""): String {
        return client.get("$SIRS_BASE_URL/courseFilter.php") {
            parameter("survey[semester]", semester)
            parameter("survey[year]", year)
            parameter("survey[school]", school)
//            parameter("mode", "course")
        }.body()
    }

    suspend fun getEntriesFromGit(school: String, dept: String): String {
        return client.get("$GH_BASE_URL/json-data-4/$school/$dept.txt").body()
    }

    suspend fun getSchoolDeptsMapFromGit(): String {
        return client.get("$GH_BASE_URL/json-data/schoolDeptsMap.json").body()
    }

    suspend fun getSubjects(semester: String, campus: String, levelOfStudy: String): String {
        return client.get("https://sims.rutgers.edu/csp/getSubjects.json") {
            headers.clear()
            header(
                "Cookie",
                "JSESSIONID=C9C4E66C557FD6CE7A5C4A5893D7633A.jvm1-tc8; fpestid=glz6WjsOX_3fya5Xd8GI4CUfjWXMH8tS6-La1tuk3qIOJlHZYhoOzxJXvKxknuaEdIX9OQ; _hjid=61d76b2b-4056-48ec-86cd-448a75da616a; _clck=tsu7y|1|eu9|0; _ga_MLYZ1CRP7C=GS1.1.1630132176.2.0.1630132176.60; _ga=GA1.2.1935962065.1629434787; sims-csp=1173101996.20480.0000; sims-webreg=1139547564.20480.0000; EssUserTrk=61710f6f.5d4f357241be8; loginNetId=dkt37; sims-ssra=1173101996.20480.0000; sascas_4_envelope_43=059989100918091648136166; sascas_4_envelope_100=098131807823261649436771; sascas_4_envelope_79=053196800787431649439090; sascas_4_envelope_3=014952709981701649691333; nsoToken=GGwr%2FCVJvoWg2t5Rg0WCwIyiae1R7lGVPkFhjrgp7B4OoyZUdLA2WxmL1bjiEKbp"
            )
            parameter("semester", semester)
            parameter("campus", campus)
            parameter("levelOfStudy", levelOfStudy)
        }.body()
    }

    suspend fun getCourses(year: String, term: String, campus: String): String {
        return client.get("https://sis.rutgers.edu/soc/api/courses.json") {
            headers.clear()
            parameter("year", year)
            parameter("term", term)
            parameter("campus", campus)
        }.body()
    }

    suspend fun getSOCData(): String {
        return client.get("https://sis.rutgers.edu/soc/").body()
    }
}