package remote.interfaces

import general.Entry

interface EntriesFromFileRepository : EntriesRepository {
    suspend fun getEntriesFromDir(school: String, dept: String, folderNum: Int = 9): List<Entry>

    override suspend fun getLatestEntriesInDept(school: String, dept: String): List<Entry> =
        getEntriesFromDir(school, dept)
}