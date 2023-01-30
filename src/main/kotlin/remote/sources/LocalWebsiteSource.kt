package remote.sources

import data.Instructor
import data.InstructorStats
import data.School
import remote.WebsiteDataSource

class LocalWebsiteSource(private val completeSource: LocalFileSource = LocalFileSource()) : WebsiteDataSource {
    override suspend fun getStatsByProf(school: String, dept: String): Map<String, InstructorStats> =
        completeSource.getStatsByProf(school, dept)

    override suspend fun getCourseNamesOrEmpty(school: String, dept: String): Map<String, String> =
        completeSource.getCourseNamesOrEmpty(school, dept)

    override suspend fun getTeachingDataOrEmpty(school: String, dept: String): Map<String, List<String>> =
        completeSource.getTeachingDataOrEmpty(school, dept)

    override suspend fun getAllInstructors(): Map<String, List<Instructor>> = completeSource.getAllInstructors()

    override suspend fun getDeptMap(): Map<String, String> = completeSource.getDeptMap()

    override suspend fun getSchoolMap(): Map<String, School> = completeSource.getSchoolMap()
}