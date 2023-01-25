package general

import data.Entry
import data.getRatingStats
import data.getTotalRatings
import data.roundToDecimal

fun List<Entry>.toCsv(): String? {
    val profRatings = mapByProf()
        .filterValues { it.isNotEmpty() }
        .mapValues { (_, entries) ->
            entries.getTotalRatings()[8].getRatingStats() // This is the teaching effectiveness question
        }.toList() // turning into list so it can be sorted
        .takeIf { it.isNotEmpty() } ?: return null // Not sure if this is needed

    val deptAve = profRatings.map { it.second.first }.average().roundToDecimal(2)
    val totalNum = profRatings.sumOf { it.second.second }

    val csv = (profRatings + Pair("Average", deptAve to totalNum))
        .sortedBy { -it.second.first }
        .joinToString("\n") { (name, stats) -> "$name;${stats.first};${stats.second}" }
    return "Professor;Rating;Total Responses\n$csv"
}