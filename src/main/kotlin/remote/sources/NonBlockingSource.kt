package remote.sources

import EntriesByProf
import Entry
import Instructor
import School
import remote.EntriesFromFileSource
import remote.ExtraDataSource
import remote.SchoolMapSource

interface NonBlockingSource : EntriesFromFileSource, SchoolMapSource, ExtraDataSource {
    fun getEntriesLocal(school: String, dept: String, folderNum: Int): List<Entry>
    override suspend fun getEntries(school: String, dept: String, folderNum: Int): List<Entry> =
        getEntriesLocal(school, dept, folderNum)

    fun getEntriesByProfLocal(school: String, dept: String, folderNum: Int): EntriesByProf
    override suspend fun getEntriesByProf(school: String, dept: String, folderNum: Int): EntriesByProf =
        getEntriesByProfLocal(school, dept, folderNum)

    fun getSchoolMapLocal(): Map<String, School>
    override suspend fun getSchoolMap(): Map<String, School> = getSchoolMapLocal()

    fun getSchoolMapLocal(dataDir: String): Map<String, School>
    override suspend fun getSchoolMap(dataDir: String): Map<String, School> = getSchoolMapLocal(dataDir)

    @Deprecated(
        "Data is now stored in separate files by dept. This function is kept to get F22 data.",
        ReplaceWith("getTeachingData"),
    )
    fun getLatestInstructorsLocal(term: String): Map<String, List<String>>

    @Suppress("DEPRECATION")
    @Deprecated(
        "Data is now stored in separate files by dept. This function is kept to get F22 data.",
        ReplaceWith("getTeachingData"),
    )
    override suspend fun getLatestInstructors(term: String): Map<String, List<String>> = getLatestInstructorsLocal(term)

    fun getTeachingDataLocal(school: String, dept: String, term: String): Map<String, List<String>>
    override suspend fun getTeachingData(school: String, dept: String, term: String): Map<String, List<String>> =
        getTeachingDataLocal(school, dept, term)

    fun getDeptMapLocal(): Map<String, String>
    override suspend fun getDeptMap(): Map<String, String> = getDeptMapLocal()

    fun getAllInstructorsLocal(dir: String): Map<String, List<Instructor>>
    override suspend fun getAllInstructors(dir: String): Map<String, List<Instructor>> = getAllInstructorsLocal(dir)

    fun getCourseNamesLocal(school: String, dept: String): Map<String, String>
    override suspend fun getCourseNames(school: String, dept: String): Map<String, String> =
        getCourseNamesLocal(school, dept)
}