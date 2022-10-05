package general


private typealias EntriesByProf = Map<String, List<Entry>>
typealias EntriesByProfMap = SchoolDeptsMap<EntriesByProf>

fun EntriesMap.toEntriesByProfMap(): EntriesByProfMap =
    mapEachDept { _, _, entries -> entries.mapByProfs() }

fun List<Entry>.mapByProfs(): EntriesByProf {
    val filtered = filterNot { entry ->
        listOf("Do not use", "ERROR").any { it in entry.instructor }
                || listOf("", "TA").any { it == entry.instructor }
    }
    val adjustedNames = filtered.getNameAdjustments()

    return filtered.groupBy { entry ->
        entry.formatFullName20().let { adjustedNames.getOrDefault(it, it) }
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
// 3e. At this point, we also incorporate into the map the names with hyphens, preferring them if they exist
// 4. Finally, we add a mapping of all hyphened names by their non-hyphened version, in case they weren't already added
// If you made it this far, good luck. I hope this helps. :)
// And if you figured out a simpler way to get the same results - please implement it!
fun List<Entry>.getNameAdjustments(): Map<String, String> {
    val names = filter { it.instructor.count { char -> char == ',' } <= 1 } // filter out multiple prof entries - for now
        .map { it.formatFullName20() }

    val hasHyphen = names.filter { "-" in it }
        .associateBy { it.replace("-","") }

    return names.map { it.replace("-","") }
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
                val newSecond = hasHyphen[value] ?: value // use name with dashes if it exists
                val hyphenPair = hasHyphen[key]?.let { it to newSecond } // pair name w/ dash same as name w/o dash
                listOfNotNull(key to newSecond, hyphenPair)
            }
        }.toMap() + hasHyphen
}

fun Entry.formatFullName20(): String {
    return instructor
        .replace(" \\(.*\\)|,|\\.".toRegex(), "") // removes stuff in parentheses + removes commas & periods
        .split(" ")
        .let { parts ->
            parts[0] + (parts.getOrNull(1)?.let { ", $it" } ?: "") // Adds first initial if present
        }.uppercase()
        .fixTypo(code)
}

private fun String.fixTypo(code: String): String {
    return when (code.split(":").take(2).joinToString(":")) {
        "01:014" -> when (this) {
            "RAMACHANDRANA, ANITHA" -> "RAMACHANDRAN, ANITHA"
            "WHITNEYIII, JAMES", "WHITNEY, JANES" -> "WHITNEY, JAMES"
            "JACKSONBREWER, KARLA" -> "JACKSON-BREWER, KARLA"
            "PRICE, MELANYE" -> "PRICE, MELANIE"
            "CADENAJ, JESENIA" -> "CADENA, JESENIA"
            else -> this
        }
        "01:050" -> when (this) {
            "CHANMALIK, SYLVIA" -> "CHAN-MALIK, SYLVIA"
            else -> this
        }
        "01:070" -> when (this) {
            "GHASSEM-FACHAN, PARVIS" -> "GHASSEM-FACHANDI, PARVIS"
            else -> this
        }
        "01:078" -> when (this) {
            "VASILIAN, ASBED" -> "VASSILIAN, ASBED"
            else -> this
        }
        "01:160" -> when (this) {
            "SHANKAR, NIRMILA" -> "SHANKAR, NIRMALA"
            "PRAMINIK, SANHITA" -> "PRAMANIK, SANHITA"
            "RABEONY, MANSES" -> "RABEONY, MANESE"
            "ROYCHOWDUHURY, LIPIKA", "ROYCHOWDURY, LIPIKA" -> "ROYCHOWDHURY, LIPIKA"
            "MARVASTI, SATAREH" -> "MARVASTI, SETAREH"
            "JIMINEZ, LESLIE" -> "JIMENEZ, LESLIE"
            "SOUNDARAJAN, NACHIMUTHU" -> "SOUNDARARAJAN, NACHIMUTHU"
            "KROGH-JESPERSE, KARSTEN" -> "KROGH-JESPERSEN, KARSTEN"
            "YORK, DARREN" -> "YORK, DARRIN"
            "MARCOTRIGIANO, JOSEPH" -> "MARCOTRIGIANO, JOESEPH"
            "OLSON, WILIMA" -> "OLSON, WILMA"
            "ROMSTED, LAWRENCE" -> "ROMSTED, LAURENCE"
            else -> this
        }
        "01:355" -> when (this) {
            "BASS, JQNATHAN" -> "BASS, JONATHAN"
            "BORIEHOLTZ, DEBRA" -> "BORIEHOLTZ, DEBBIE"
            "CHOWDHURY, NANDINCH" -> "CHOWDHURY, NANDINI"
            "DUFFY, MIKE" -> "DUFFY, MICHAEL"
            "FOLEM, SEAN" -> "FOLEY, SEAN"
            "GILMARTIN, VGILMAR" -> "GILMARTIN, VIRGINIA"
            "GOELLER, ANGIESZKA" -> "GOELLER, AGNIESZKA"
            "HAMLET, BMENDA" -> "HAMLET, BRENDA"
            else -> this
        }
        "01:750" -> when (this) {
            "HARMON, S" -> "HARMAN, S" // this is a guess
            "KIRYUKIN, VALERY" -> "KIRYUKHIN, VALERY"
            "RASTOGI, T" -> "RASTOGI, A"
            "DIACONSECU, E" -> "DIACONESCU, E"
            else -> this
        }
        "14:332" -> when (this) {
            "DANA, KRISTEN" -> "DANA, KRISTIN"
            "SUBRAMANIAN, NAGI" -> "SUBRAMANIAN, NAGANATHAN" // guess
            "SPASOJEVIC, PREDRAY" -> "SPASOJEVIC, PREDRAG"
            "GAGGIANO, MICHAEL" -> "CAGGIANO, MICHAEL"
            "KARIMINI, NAGHMEH" -> "KARIMI, NAHHMEH"
            "JHA, SHANTANU" -> "JHA, SHANTENU"
            "PARAKEVAKOS, IOANNIS" -> "PARASKEVAKOS, IOANNIS"
            else -> this
        }
        else -> this
    }

}
