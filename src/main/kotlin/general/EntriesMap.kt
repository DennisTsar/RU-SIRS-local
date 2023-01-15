package general

import EntriesMap
import SchoolDeptsMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import misc.makeFileAndDir
import remote.sources.LocalFileSource

inline fun <reified T> SchoolDeptsMap<T>.writeToDir(
    dir: String,
    writeSchoolMap: Boolean = true,
    skipIfEmpty: Boolean = true,
): SchoolDeptsMap<T> {
    if (writeSchoolMap) {
        val dirMap = LocalFileSource().getSchoolMapLocal().mapValues { (code, school) ->
            val filteredDepts = school.depts.filter { this[code]?.keys?.contains(it) == true }
            school.copy(depts = filteredDepts.toSet())
        }.filterValues { it.depts.isNotEmpty() }
        val file = makeFileAndDir("$dir/schoolMap.json")
        file.writeText(Json.encodeToString(dirMap))
    }
    return onEach { (school, deptMap) ->
        deptMap.forEach { (dept, entries) ->
            if (skipIfEmpty) {
                when (entries) {
                    is List<*> -> if (entries.isEmpty()) return@forEach
                    is Map<*, *> -> if (entries.isEmpty()) return@forEach
                }
            }
            val file = makeFileAndDir("$dir/$school/$dept.json")
            file.writeText(Json.encodeToString(entries))
        }
    }
}

// combines all schools & depts that have semicolons into same dirs/files
fun EntriesMap.semicolonCleanup(): EntriesMap {
    return toList()
        .groupBy({ it.first.substringBefore(";") }, { it.second })
        .mapValues { (_, listOfMaps) ->
            listOfMaps.reduce { acc, map ->
                val newDepts = map.filterKeys { it !in acc.keys }
                acc.mapValues internalMap@{ (dept, entries) ->
                    val otherEntries = map[dept] ?: return@internalMap entries
                    entries + otherEntries
                } + newDepts
            }.toList()
                .groupBy({ it.first.substringBefore(";") }, { it.second })
                .mapValues { it.value.flatten() }
        }
}

fun EntriesMap.addOldEntries(oldEntries: EntriesMap): EntriesMap {
    return (this.asSequence() + oldEntries.asSequence())
        .groupBy({ it.key }, { it.value })
        .mapValues { (_, values) ->
            values.flatMap { it.asSequence() }
                .groupBy({ it.key }, { it.value })
                .mapValues { it.value.flatten() }
        }
}