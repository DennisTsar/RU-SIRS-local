package remote.sources

import EntriesByProf
import EntriesByProfMap
import Entry
import Instructor
import School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import remote.EntriesFromFileSource
import remote.ExtraDataSource
import remote.SchoolMapSource
import java.io.File
import java.io.FileNotFoundException

class LocalSource(
    val mainJsonDir: String = "json-data/data-9",
    private val extraJsonDir: String = "json-data/extra-data",
) : EntriesFromFileSource, SchoolMapSource, ExtraDataSource {
    fun getEntriesLocal(school: String, dept: String, folderNum: Int): List<Entry> =
        Json.decodeFromString(File("json-data/data-$folderNum/$school/$dept.json").readText())

    override suspend fun getEntries(school: String, dept: String, folderNum: Int): List<Entry> =
        getEntriesLocal(school, dept, folderNum)

    inline fun <reified T> getAllEntries(readDir: String = mainJsonDir): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    fun getEntriesByProfLocal(school: String, dept: String, folderNum: Int): EntriesByProf =
        Json.decodeFromString(File("json-data/data-$folderNum-by-prof/$school/$dept.json").readText())

    override suspend fun getEntriesByProf(school: String, dept: String, folderNum: Int): EntriesByProf =
        getEntriesByProfLocal(school, dept, folderNum)

    fun getAllEntriesByProf(readDir: String = "$mainJsonDir-by-prof"): EntriesByProfMap {
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

    @Deprecated(
        "Data is now stored in separate files by dept. This function is kept to get F22 data.",
        replaceWith = ReplaceWith("getTeachingDataLocal"),
    )
    fun getLatestInstructorsLocal(term: String): Map<String, List<String>> =
        Json.decodeFromString(File("$extraJsonDir/$term-instructors.json").readText())

    @Suppress("DEPRECATION")
    @Deprecated(
        "Data is now stored in separate files by dept. This function is kept to get F22 data.",
        replaceWith = ReplaceWith("getTeachingData"),
    )
    override suspend fun getLatestInstructors(term: String): Map<String, List<String>> =
        getLatestInstructorsLocal(term)

    fun getTeachingDataLocal(school: String, dept: String, term: String): Map<String, List<String>> {
        return try {
            Json.decodeFromString(File("$extraJsonDir/$term-instructors/$school/$dept.json").readText())
        } catch (e: FileNotFoundException) {
            emptyMap()
        }
    }

    override suspend fun getTeachingData(school: String, dept: String, term: String): Map<String, List<String>> =
        getTeachingDataLocal(school, dept, term)

    fun getDeptMapLocal(): Map<String, String> =
        Json.decodeFromString(File("$extraJsonDir/deptNameMap.json").readText())

    override suspend fun getDeptMap(): Map<String, String> = getDeptMapLocal()

    fun getAllInstructorsLocal(): List<Instructor> =
        Json.decodeFromString(File("$extraJsonDir/allInstructors.json").readText())

    override suspend fun getAllInstructors(): List<Instructor> = getAllInstructorsLocal()

    fun getSchoolMapLocal(dataDir: String): Map<String, School> =
        Json.decodeFromString(File("json-data/$dataDir/schoolMap.json").readText())

    override suspend fun getSchoolMap(dataDir: String): Map<String, School> = getSchoolMapLocal(dataDir)
}