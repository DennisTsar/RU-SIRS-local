package remote.interfaces

import Entry
import School
import general.EntriesMap
import generateSchoolsMap

interface EntriesSource {
    suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry>

    suspend fun getAllLatestEntries(schools: Collection<School>): EntriesMap =
        schools.generateSchoolsMap { school, dept -> getLatestEntriesInDept(school, dept) }
}

