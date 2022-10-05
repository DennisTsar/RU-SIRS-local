package logic

import general.Entry
import general.mapByProfs
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

fun List<Entry>.toCsv(): String? {
    val profRatings = mapByProfs()
        .filterValues { it.isNotEmpty() }
        .mapValues { (_, entries) ->
            entries.getTotalRatings().getRatingStats()[8] // This is the teaching effectiveness question
        }.toList() // turning into list so it can be sorted
        .takeIf { it.isNotEmpty() } ?: return null // Not sure if this is needed

    val deptAve = profRatings.map { it.second.first }.average().roundToDecimal(2)
    val totalNum = profRatings.sumOf { it.second.second }

    val csv = (profRatings + Pair("Average", deptAve to totalNum))
        .sortedBy { -it.second.first }
        .joinToString("\n") { (name, stats) -> "$name;${stats.first};${stats.second}" }
    return "Professor;Rating;Total Responses\n$csv"
}

// example of indeterminate entry
// https://sirs.ctaar.rutgers.edu/index.php?mode=name&survey%5Blastname%5D=TEMKIN&survey%5Bsemester%5D=&survey%5Byear%5D=&survey%5Bschool%5D=&survey%5Bdept%5D=730