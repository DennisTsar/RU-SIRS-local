package general

import EntriesByProf
import EntriesByProfMap
import EntriesMap
import Entry
import mapEachDept

fun EntriesMap.toEntriesByProfMap(): EntriesByProfMap =
    mapEachDept { _, _, entries ->
        entries.filterNot { entry ->
            val prof = entry.instructor
            listOf("do not use", "error", "faculty", "proctortrack", "instructor").any { it in prof.lowercase() }
                    || listOf("", ",", "-", "--", "TA", "(Recitation)").any { it == prof }
                    || prof.likelyMultipleProfs()
                    || entry.scores.size < 100 // for now
        }.mapByProfs()
    }

private fun String.likelyMultipleProfs(): Boolean {
    val listOfAccepted = setOf(
        "der", "de", "da", "del", "la", "uz", "el", "van",  // also used in formatFullName() (forwards + backwards)
        "ii", "iii", "iv", "col", // also used in formatFullName() (only backwards)
        "ed.d.", "ezzat", "dagracia", "desilva", "sai", "rui", "graca", "mai", "n", "fache"
    )
    val altered = replace(" \\(.*\\)|-".toRegex(), "") // replace content in parentheses & removes dashes
        .replace("&quot;", "\"")
        .lowercase()

    val filtered = altered.split(" ", ",").filter { it.isNotBlank() } - listOfAccepted
    return ";" in altered || filtered.size > 3
}

fun List<Entry>.mapByProfs(): EntriesByProf {
    val adjustedNames = autoNameAdjustments()

    return groupBy { entry ->
        entry.formatFullName().let { adjustedNames.getOrDefault(it, it) }
    }
}

// This function is pretty complex, so I'm going to try to explain it here
// It's purpose is to figure out what different names actually refer to the same person
// Ex. "Smith", "Smith, John", and "Smith, J" are likely all the same person
// (as long as there is no "Smith, Jane" or something like that)
// To do this, it takes quite a few steps
// 1. Format instructor names to be "Last, First" (or "Last, First Initial" or just "Last", depending on given info)
// 2. Then, group the names by last name. Now, for each grouping:
// 3a. We start with the last name and a list of all the first names (or first name initials) that go with it
// 3b. We then group the first names by first initial
// 3c. For each grouping of first names by first initial:
// 3c-i. We check if all the non-initial names match (i.e. all start in same way - "Ron/Ronald", "Ken/Kenneth", etc.)
// 3c-ii. If they do, we map all of them to the longest name
// 3c-iii. If not, we just leave an empty list
// 3c-iv. However, if there are no non-initial names, we map the initial name to itself - to not have an empty list
// 3d. Because, if the resulting mappings exist, we also add the map of the lone last name to the mapped value
// 3e. At this point, we also incorporate into the map the names with special chars, preferring them if they exist
// 4. Finally, we add a mapping of special char names by their non-special version, in case they weren't already added
// If you made it this far, good luck. I hope this helps. :)
// And if you figured out a simpler way to get the same results - please implement it!
private fun List<Entry>.autoNameAdjustments(): Map<String, String> {
    val names = map { it.formatFullName() }

    val specialChars = listOf('-', '\'')
    val removeSpecialChars: String.() -> String = {
        specialChars.fold(this) { acc, char -> acc.filter { it != char } }
    }

    val specialCharMap = names.filter { name -> specialChars.any { it in name } }
        .associateBy { it.removeSpecialChars() }

    val nameMappings = names
        .map { it.removeSpecialChars() }
        .distinct()
        .groupBy(
            keySelector = { it.substringBefore(",") },
            valueTransform = { it.substringAfter(", ", "") },
        ).flatMap { (lastName, commonLast) ->
            val byFirstInitial = commonLast
                .minus("") // remove string with only last name
                .groupBy { it[0].toString() } // group by 1st initial - since some entries only have 1st initial

            byFirstInitial.flatMap { (initial, commonInitial) ->
                val fullFirsts = commonInitial
                    .minus(initial) // remove string with only initial
                    .sortedBy { it.length } // sorted to check if shortest name matches longer & to use the longest name
                    .takeIf { it.isNotEmpty() } // when only initial is present -> need to differentiate from false
                // treat names like "Ron/Ronald", "Ken/Kenneth", ... as same name
                when (fullFirsts?.all { it.startsWith(fullFirsts[0]) }) {
                    true -> commonInitial.associateWith { fullFirsts.last() }
                    false -> emptyMap()
                    null -> initial.associateWith { it } // so that  run{} below adds last name to map
                }.map { "$lastName, ${it.key}" to "$lastName, ${it.value}" }
            }.let {
                if (byFirstInitial.size == 1 && it.isNotEmpty())
                    it.plus(lastName to it[0].second) // pair "Smith" with first name if only one exists
                else it
            }.flatMap { (key, value) ->
                val newSecond = specialCharMap[value] ?: value // use name with dashes if it exists
                val specialPair = specialCharMap[key]?.let { it to newSecond } // duplicate normal pair for special name
                listOfNotNull(key to newSecond, specialPair)
            }
        }.toMap()
    return specialCharMap + nameMappings // not that this order is important as keys from first are overwritten
}

private fun Entry.formatFullName(): String {
    return instructor
        .trim()
        .replace(" \\(.*\\)|,|\\.".toRegex(), "") // removes stuff in parentheses + removes commas & periods
        .split(" ")
        .let { split ->
            // un-separate the specials from other parts of the name
            // first combine them forwards, then backwards
            val forwards = setOf("der", "de", "da", "del", "la", "uz", "el", "van")
            val backwards = forwards + setOf("ii", "iii", "iv", "col")
            split.fold(emptyList<String>()) { acc, s ->
                acc.lastOrNull()
                    ?.takeIf { it.split(" ").last().lowercase() in forwards }
                    ?.let { acc.dropLast(1) + "$it $s" } ?: (acc + s)
            }.foldRight(emptyList<String>()) { s, acc ->
                acc.firstOrNull()
                    ?.takeIf { it.split(" ").first().lowercase() in backwards }
                    ?.let { listOf("$s $it") + acc.drop(1) } ?: (listOf(s) + acc)
            }
        }.let { parts ->
            parts[0] + (parts.getOrNull(1)?.let { ", $it" } ?: "") // Adds first initial if present
        }.uppercase()
        .let { manualNameAdjustment(it, code) }
}