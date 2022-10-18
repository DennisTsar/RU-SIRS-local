package remote.interfaces

import Entry
import data.School
import data.generateSchoolsMap
import general.EntriesMap

interface EntriesSource {
    suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry>

    suspend fun getAllLatestEntries(schools: Collection<School>): EntriesMap =
        schools.generateSchoolsMap { school, dept -> getLatestEntriesInDept(school, dept) }
}

