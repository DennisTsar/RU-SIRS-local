package remote.sources

import Entry
import School
import EntriesByProf
import EntriesByProfMap
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import remote.EntriesFromFileSource
import remote.ExtraDataSource
import remote.SchoolsMapSource
import java.io.File

class LocalSource(
    val mainJsonDir: String = "json-data/data-9",
    private val extraJsonDir: String = "json-data/extra-data",
) : EntriesFromFileSource, SchoolsMapSource, ExtraDataSource {
    override suspend fun getEntriesFromDir(school: String, dept: String, folderNum: Int): List<Entry> =
        Json.decodeFromString(File("json-data/data-$folderNum/$school/$dept.json").readText())

    inline fun <reified T> getAllEntriesInDir(readDir: String = mainJsonDir): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    override suspend fun getEntriesByProfFromDir(school: String, dept: String, folderNum: Int): EntriesByProf =
        Json.decodeFromString(File("json-data/data-$folderNum-by-prof/$school/$dept.json").readText())

    fun getAllEntriesByProfInDir(readDir: String = "$mainJsonDir-by-prof"): EntriesByProfMap {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<EntriesByProf>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    override suspend fun getSchoolsMap(): Map<String, School> =
        Json.decodeFromString(File("$extraJsonDir/schoolDeptsMap.json").readText())

    override suspend fun getInstructors(term: String): Map<String, List<String>> =
        Json.decodeFromString(File("$extraJsonDir/$term-instructors.json").readText())

    override suspend fun getDeptMap(): Map<String, String> =
        Json.decodeFromString(File("$extraJsonDir/deptNamesMap.json").readText())
}