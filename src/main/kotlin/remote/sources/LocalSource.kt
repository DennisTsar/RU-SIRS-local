package remote.sources

import EntriesByProf
import EntriesByProfMap
import Entry
import School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import remote.EntriesFromFileSource
import remote.ExtraDataSource
import remote.SchoolMapSource
import java.io.File

class LocalSource(
    val mainJsonDir: String = "json-data/data-9",
    private val extraJsonDir: String = "json-data/extra-data",
) : EntriesFromFileSource, SchoolMapSource, ExtraDataSource {
    fun getEntriesFromDirLocal(school: String, dept: String, folderNum: Int): List<Entry> =
        Json.decodeFromString(File("json-data/data-$folderNum/$school/$dept.json").readText())

    override suspend fun getEntriesFromDir(school: String, dept: String, folderNum: Int): List<Entry> =
        getEntriesFromDirLocal(school, dept, folderNum)

    inline fun <reified T> getAllEntriesInDir(readDir: String = mainJsonDir): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    fun getEntriesByProfFromDirLocal(school: String, dept: String, folderNum: Int): EntriesByProf =
        Json.decodeFromString(File("json-data/data-$folderNum-by-prof/$school/$dept.json").readText())

    override suspend fun getEntriesByProfFromDir(school: String, dept: String, folderNum: Int): EntriesByProf =
        getEntriesByProfFromDirLocal(school, dept, folderNum)

    fun getAllEntriesByProfInDir(readDir: String = "$mainJsonDir-by-prof"): EntriesByProfMap {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<EntriesByProf>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    fun getSchoolMapLocal(): Map<String, School> =
        Json.decodeFromString(File("$extraJsonDir/schoolMap.json").readText())

    override suspend fun getSchoolMap(): Map<String, School> = getSchoolMapLocal()

    fun getInstructorsLocal(term: String): Map<String, List<String>> =
        Json.decodeFromString(File("$extraJsonDir/$term-instructors.json").readText())

    override suspend fun getInstructors(term: String): Map<String, List<String>> = getInstructorsLocal(term)

    fun getDeptMapLocal(): Map<String, String> =
        Json.decodeFromString(File("$extraJsonDir/deptNameMap.json").readText())

    override suspend fun getDeptMap(): Map<String, String> = getDeptMapLocal()

    fun getDirSchoolMapLocal(dataDir: String): Map<String, School> =
        Json.decodeFromString(File("json-data/$dataDir/schoolMap.json").readText())

    override suspend fun getDirSchoolMap(dataDir: String): Map<String, School> = getDirSchoolMapLocal(dataDir)
}