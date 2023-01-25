package remote.sources

import Instructor
import InstructorStats
import School
import remote.WebsiteDataSource

class LocalWebsiteSource(private val completeSource: LocalFileSource = LocalFileSource()) : WebsiteDataSource {
    override suspend fun getStatsByProf(school: String, dept: String): Map<String, InstructorStats> =
        completeSource.getStatsByProf(school, dept)

    override suspend fun getCourseNames(school: String, dept: String): Map<String, String> =
        completeSource.getCourseNames(school, dept)

    override suspend fun getTeachingData(school: String, dept: String): Map<String, List<String>> =
        completeSource.getTeachingData(school, dept)

    override suspend fun getAllInstructors(): Map<String, List<Instructor>> = completeSource.getAllInstructors()

    override suspend fun getDeptMap(): Map<String, String> = completeSource.getDeptMap()

    override suspend fun getSchoolMap(): Map<String, School> = completeSource.getSchoolMap()
}