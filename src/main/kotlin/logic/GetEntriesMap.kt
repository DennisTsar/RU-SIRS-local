package logic

import api.Repository
import general.School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import misc.pmap
import misc.walkDirectory
import java.io.File

object GetEntriesMap {
    // generics to allow choosing which "Entry" class version to do
    suspend inline fun <reified T> fromGit(
        repository: Repository,
        schoolDeptsMap: Map<String, School>,
    ): Map<String, Map<String, List<T>>> {
        return schoolDeptsMap.values.pmap { school ->
            val deptMap = school.depts.associateWith {
                Json.decodeFromString<List<T>>(repository.getEntriesFromGit(school.code, it))
            }
            school.code to deptMap
        }.toMap().filterValues { it.isNotEmpty() }
    }

    inline fun <reified T> fromLocalFile(readDir: String = "json-data/"): Map<String, Map<String, List<T>>> {
        return File(readDir).walkDirectory().associate { file ->
            val deptMap = file.walkDirectory().associate {
                it.nameWithoutExtension to Json.decodeFromString<List<T>>(it.readText())
            }
            file.nameWithoutExtension to deptMap
        }.filterValues { it.isNotEmpty() }
    }
}