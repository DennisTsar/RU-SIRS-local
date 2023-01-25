package remote.sources

import EntriesByProf
import Entry
import Instructor
import InstructorStats
import School
import SchoolDeptsMap
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.walkDirectory
import remote.WebsitePaths
import java.io.File
import java.io.FileNotFoundException

class LocalFileSource(
    private val sitePaths: WebsitePaths = WebsitePaths(),
    private val entriesDir: String = "json-data/data-9",
    private val entriesByProfDir: String = "$entriesDir-by-prof",
) {
    // maybe make this internal for extensions functions
    private inline fun <reified T> String.decodeFromFile(): T = Json.decodeFromString(File(this).readText())

    // region website source
    fun getStatsByProf(
        school: String,
        dept: String,
        dir: String = sitePaths.statsByProfDir,
    ): Map<String, InstructorStats> = "$dir/$school/$dept.json".decodeFromFile()

    fun getCourseNames(school: String, dept: String, dir: String = sitePaths.courseNamesDir): Map<String, String> {
        return try {
            "$dir/$school/$dept.json".decodeFromFile()
        } catch (e: FileNotFoundException) {
            emptyMap()
        }
    }

    fun getTeachingData(
        school: String,
        dept: String,
        dir: String = sitePaths.teachingDataDir,
    ): Map<String, List<String>> {
        return try {
            "$dir/$school/$dept.json".decodeFromFile()
        } catch (e: FileNotFoundException) {
            emptyMap()
        }
    }

    fun getAllInstructors(path: String = sitePaths.allInstructorsFile): Map<String, List<Instructor>> =
        path.decodeFromFile()

    fun getDeptMap(path: String = sitePaths.deptMapFile): Map<String, String> = path.decodeFromFile()

    fun getSchoolMap(path: String = sitePaths.schoolMapFile): Map<String, School> = path.decodeFromFile()
    // endregion

    private inline fun <reified T> getCompleteSchoolDeptsMap(dir: String): SchoolDeptsMap<T> {
        return File(dir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
//                println(it.absolutePath)
//                println(it.canonicalPath)
                it.nameWithoutExtension to Json.decodeFromString<T>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }

    fun getEntries(school: String, dept: String, dir: String = entriesDir): List<Entry> =
        getGenericEntries(school, dept, dir)

    fun getAllEntries(dir: String = entriesDir): SchoolDeptsMap<List<Entry>> = getCompleteSchoolDeptsMap(dir)

    /** For accessing entries stored in an old format */
    fun <T> getGenericEntries(school: String, dept: String, dir: String = entriesDir): List<T> =
        "$dir/$school/$dept.json".decodeFromFile()

    /** For accessing entries stored in an old format */
    fun <T> getAllGenericEntries(dir: String = entriesDir): SchoolDeptsMap<List<T>> = getCompleteSchoolDeptsMap(dir)

    fun getEntriesByProf(school: String, dept: String, dir: String = entriesByProfDir): EntriesByProf =
        "$dir/$school/$dept.json".decodeFromFile()

    fun getAllEntriesByProf(dir: String = entriesByProfDir): SchoolDeptsMap<EntriesByProf> =
        getCompleteSchoolDeptsMap(dir)

    fun getAllStatsByProf(dir: String = sitePaths.statsByProfDir): SchoolDeptsMap<Map<String, InstructorStats>> =
        getCompleteSchoolDeptsMap(dir)

    /** Access teaching data stored in an old format (All depts in one file) */
    fun getTeachingDataOldFormat(term: String): Map<String, List<String>> =
        "$sitePaths/$term-instructors.json".decodeFromFile()
}