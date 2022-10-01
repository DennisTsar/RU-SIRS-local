package remote.interfaces

import general.EntriesMap
import general.Entry
import general.School
import misc.pmap

interface EntriesRepository {
    suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry>

    suspend fun getAllLatestEntries(schools: Collection<School>): EntriesMap {
        return schools.pmap { school ->
            school.code to school.depts.associateWith { getLatestEntriesInDept(school.code, it) }
        }.toMap()
    }
}

