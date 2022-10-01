package remote.api

import remote.interfaces.EntriesFromFileRepository
import remote.interfaces.SchoolsMapRepository
import general.Entry
import general.School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import java.io.File

class LocalApi(
    val mainJsonDir: String = "json-data-9/",
    private val extraJsonDir: String = "extra-json-data",
) : EntriesFromFileRepository, SchoolsMapRepository {
    override suspend fun getEntriesFromDir(school: String, dept: String, folderNum: Int): List<Entry> =
        Json.decodeFromString(File("json-data-$folderNum/$school/$dept.json").readText())

    inline fun <reified T> getAllEntriesInDir(readDir: String = mainJsonDir): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    override suspend fun getSchoolsMap(): Map<String, School> =
        Json.decodeFromString(File("$extraJsonDir/schoolDeptsMap.json").readText())
}