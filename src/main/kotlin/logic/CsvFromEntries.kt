package logic

import general.Entry
import misc.roundToDecimal

typealias Ratings = List<List<Int>>

// returns list of (# of 1s, # of 2s, ... # of 5s) for each question
// note that entries must have scores.size>=100 - maybe throw error?
fun List<Entry>.getTotalRatings(): Ratings {
    return map { entry ->
        // group by question (there are 10 nums in table per question)
        // + we only care about first 5 nums per Q (the actual ratings) which are all int amounts
        entry.scores.chunked(10)
            .map { it.subList(0, 5).map(Double::toInt) }
    }.reduce { accByQuestion, responsesByQ ->
        // nums from each entry get zipped with each other, by question
        accByQuestion.zip(responsesByQ) { accRatings, ratings ->
            accRatings.zip(ratings, Int::plus)
        }
    }
}

// returns list of Pair(average, # responses) for each question
fun Ratings.getRatingStats(): List<Pair<Double, Int>> {
    return map { ratingsPerQ ->
        val numResponses = ratingsPerQ.sum()
        val ave = ratingsPerQ.mapIndexed { index, num ->
            (index + 1) * num
        }.sum().toDouble() / numResponses
        ave.roundToDecimal(2) to numResponses
    }
}

fun csvFromEntries(entries: List<Entry>): String? {
    val profRatings = entries.mapByProfs()
        .filterValues(List<Entry>::isNotEmpty)
        .mapValues { (_, entries) ->
            entries.getTotalRatings().getRatingStats()[8] // This is the teaching effectiveness question
        }.toList().takeIf { it.isNotEmpty() } ?: return null // turning into list so it can be sorted

    val deptAve = profRatings.map { it.second.first }.average().roundToDecimal(2)
    val totalNum = profRatings.sumOf { it.second.second }

    val csv = (profRatings + Pair("Average", deptAve to totalNum))
        .toList()
        .sortedBy { -it.second.first }
        .joinToString("\n") { (name, stats) -> "$name;${stats.first};${stats.second}" }
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
                || entry.scores.size < 100 // controversial line - do I want it here?
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
            names.find { "," in it } ?: names[0]// should be guaranteed to exist
        }
    return filtered.groupBy { entry ->
        entry.formatName().let { adjustedNames.getOrDefault(it, it) }
    }
}