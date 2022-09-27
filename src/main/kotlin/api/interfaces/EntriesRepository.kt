package api.interfaces

import general.Entry
import general.School
import misc.pmap

interface EntriesRepository {
    suspend fun getEntries(school: String, dept: String, folderNum: Int = 7): List<Entry>

    suspend fun getAllEntries(schools: Collection<School>): Map<String, Map<String, List<Entry>>> {
        return schools.pmap { school ->
            school.code to school.depts.associateWith { getEntries(school.code, it) }
        }.toMap()
    }
}

