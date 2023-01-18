package remote.sources

import EntriesByProf
import EntriesByProfMap
import Entry
import Instructor
import School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import remote.InstructorStats
import java.io.File
import java.io.FileNotFoundException

class LocalFileSource(
    val mainJsonDir: String = "json-data/data-9",
    private val extraJsonDir: String = "json-data/extra-data",
) : NonBlockingSource {
    override fun getEntriesLocal(school: String, dept: String, folderNum: Int): List<Entry> =
        Json.decodeFromString(File("json-data/data-$folderNum/$school/$dept.json").readText())

    inline fun <reified T> getAllEntries(readDir: String = mainJsonDir): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    override fun getEntriesByProfLocal(school: String, dept: String, folderNum: Int): EntriesByProf =
        Json.decodeFromString(File("json-data/data-$folderNum-by-prof/$school/$dept.json").readText())

    fun getAllEntriesByProf(readDir: String = "$mainJsonDir-by-prof"): EntriesByProfMap {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<EntriesByProf>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    override fun getStatsByProfLocal(school: String, dept: String): Map<String, InstructorStats> =
        Json.decodeFromString(File("$mainJsonDir-by-prof-stats/$school/$dept.json").readText())

    override fun getSchoolMapLocal(): Map<String, School> =
        Json.decodeFromString(File("$extraJsonDir/schoolMap.json").readText())

    @Deprecated(
        "Data is now stored in separate files by dept. This function is kept to get F22 data.",
        replaceWith = ReplaceWith("getTeachingDataLocal"),
    )
    override fun getLatestInstructorsLocal(term: String): Map<String, List<String>> =
        Json.decodeFromString(File("$extraJsonDir/$term-instructors.json").readText())


    override fun getTeachingDataLocal(school: String, dept: String, term: String): Map<String, List<String>> {
        return try {
            Json.decodeFromString(File("$extraJsonDir/$term-teaching/$school/$dept.json").readText())
        } catch (e: FileNotFoundException) {
            emptyMap()
        }
    }

    override fun getCourseNamesLocal(school: String, dept: String): Map<String, String> {
        return try {
            Json.decodeFromString(File("$extraJsonDir/courseNames/$school/$dept.json").readText())
        } catch (e: FileNotFoundException) {
            emptyMap()
        }
    }

    override fun getDeptMapLocal(): Map<String, String> =
        Json.decodeFromString(File("$extraJsonDir/deptNameMap.json").readText())

    override fun getAllInstructorsLocal(dir: String): Map<String, List<Instructor>> =
        Json.decodeFromString(File("json-data/$dir/allInstructors.json").readText())


    override fun getSchoolMapLocal(dataDir: String): Map<String, School> =
        Json.decodeFromString(File("json-data/$dataDir/schoolMap.json").readText())
}