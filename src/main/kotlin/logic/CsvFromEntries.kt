package logic

import general.Entry
import misc.roundToDecimal

fun csvFromEntries(entries: List<Entry>): String? {
    val mapOfProfs = entries.mapByProfs()

    val profRatings = mapOfProfs.filter { it.value.isNotEmpty() }
        .mapValues { (k, v) ->
            v.map { i ->
                i.scores.chunked(10)//grouped by question
                    .map {
                        it.subList(0, 5).flatMapIndexed { index, d ->
                            List(d.toInt()) { index + 1 }
                        }
                    }//maps to all answers as list
                //ex. 2 5s and 3 4s gives [5,5,4,4,4]
                //this allows for keeping total # of responses and average calculation after flattening
            }
                .flatMap { it.withIndex() }
                .groupBy({ it.index }, { it.value }).values
                .map { it.flatten() }
        }

    if (profRatings.isEmpty())
        return null

    val profAves = profRatings.map { (name, value) ->
        val row = value[8]//This is the teaching effectiveness question
        Pair(name, Pair(row.average().roundToDecimal(2), row.size))
    }

    val deptAve = profAves.map { it.second.first }.average().roundToDecimal(2)
    val totalNum = profAves.sumOf { it.second.second }

    val csv = (profAves + Pair("Average", Pair(deptAve, totalNum)))
        .sortedBy { -it.second.first }
        .joinToString("\n") { "${it.first};${it.second.first};${it.second.second}" }
    return "Professor;Rating;Total Responses\n$csv"
}

// example of indeterminate entry
// https://sirs.ctaar.rutgers.edu/index.php?mode=name&survey%5Blastname%5D=TEMKIN&survey%5Bsemester%5D=&survey%5Byear%5D=&survey%5Bschool%5D=&survey%5Bdept%5D=730

private fun Entry.formatName(): String {
    return instructor
        .replace(" \\(.*\\)|,".toRegex(), "") // removes stuff in parentheses & removes commas
        .split(" ")
        .let { parts ->
            parts[0] + (parts.getOrNull(1)?.let { ", ${it[0]}" } ?: "") // Adds first initial if present
        }.uppercase()
}

fun List<Entry>.mapByProfs(): Map<String, List<Entry>> {
    val filtered = filterNot { entry ->
        listOf("Do not use", "ERROR").any { it in entry.instructor }
                || listOf("", "TA").any { it == entry.instructor }
    }
    // This exists so that "Smith" and "Smith, John" are grouped together IFF John is the only Smith in the department
    // note that this 100% combines "Smith, James" and "Smith, John" but the SIRS data is also sketchy about that
    val adjustedNames = filtered
        .map(Entry::formatName)
        .distinct()
        .groupBy { it.substringBefore(",") }
        .filterValues { it.size == 2 } // One for "Smith" and one for "Smith, J"
        .mapValues { (_, names) ->
            names.first { "," in it } // should be guaranteed to exist
        }
    return filtered.groupBy { entry ->
        entry.formatName().let { adjustedNames.getOrDefault(it, it) }
    }
}

fun List<Entry>.mapByProfs2(): Map<String, List<Entry>> {
    val filtered = filterNot { entry ->
        listOf("Do not use", "ERROR").any { it in entry.instructor }
                || listOf("", "TA").any { it == entry.instructor }
    }
    // This exists so that "Smith" and "Smith, John" are grouped together IFF John is the only Smith in the department
    // note that this 100% combines "Smith, James" and "Smith, John" but the SIRS data is also sketchy about that
    val adjustedNames = filtered
        .map(Entry::formatName)
//        .distinct()
        .groupBy { it.substringBefore(",") }
//        .filterValues { it.size == 2 } // One for "Smith" and one for "Smith, J"
        .mapValues { (_, names) ->
            names.find { "," in it }  ?: names[0]// should be guaranteed to exist
        }
    return filtered.groupBy { entry ->
        entry.formatName().let { adjustedNames.getOrDefault(it, it) }
    }
}