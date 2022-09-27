package api.interfaces

import general.School

interface SchoolsMapRepository {
    suspend fun getSchoolsMap(): Map<String, School>
}