package remote.interfaces

import data.School

interface SchoolsMapSource {
    suspend fun getSchoolsMap(): Map<String, School>
}