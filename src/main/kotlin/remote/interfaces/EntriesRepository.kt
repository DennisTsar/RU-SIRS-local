package remote.interfaces

import general.EntriesMap
import general.Entry
import general.School
import general.generateSchoolsMap

interface EntriesRepository {
    suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry>

    suspend fun getAllLatestEntries(schools: Collection<School>): EntriesMap =
        schools.generateSchoolsMap { school, dept -> getLatestEntriesInDept(school, dept) }
}

