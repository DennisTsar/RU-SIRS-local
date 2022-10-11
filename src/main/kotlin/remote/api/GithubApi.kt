package remote.api

import general.Entry
import general.School
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import remote.interfaces.Api
import remote.interfaces.EntriesFromFileRepository
import remote.interfaces.SchoolsMapRepository

class GithubApi : Api, EntriesFromFileRepository, SchoolsMapRepository {
    private val ghClient = client.config {
        install(ContentNegotiation) {
            serialization(ContentType.Text.Plain, Json)
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "raw.githubusercontent.com"
                encodedPath = "/DennisTsar/Rutgers-SIRS/master/"
            }
        }
    }

    override suspend fun getEntriesFromDir(school: String, dept: String, folderNum: Int): List<Entry> =
        ghClient.get("json-data-$folderNum/$school/$dept.json").body()

    override suspend fun getSchoolsMap(): Map<String, School> =
        ghClient.get("extra-json-data/schoolDeptsMap.json").body()
}
