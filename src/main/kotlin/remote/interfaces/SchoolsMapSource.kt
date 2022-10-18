package remote.interfaces

import School

interface SchoolsMapSource {
    suspend fun getSchoolsMap(): Map<String, School>
}