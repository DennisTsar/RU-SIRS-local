package general

import Entry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import misc.makeFileAndDir

typealias EntriesMap = SchoolDeptsMap<List<Entry>>

fun EntriesMap.writeToDir(dir: String) {
    forEach { (school, deptMap) ->
        deptMap.forEach { (dept, entries) ->
            Json.encodeToString(entries).let {
                val file = makeFileAndDir("$dir/$school/$dept.json")
                file.writeText(it)
            }
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