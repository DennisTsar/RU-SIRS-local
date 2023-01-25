package misc

import data.EntriesMap
import data.Entry
import data.QsMap
import data.TenQs
import data.old_data.EntryOld3
import general.writeToDir
import remote.sources.LocalFileSource

private fun EntryOld3.toEntry(): Entry {
    return Entry(
        instructor = instructor,
        term = term,
        code = code,
        courseName = courseName,
        indexNum = indexNum,
        note = note,
        enrolled = enrolled,
        responses = responses,
        scores = scores,
        questions = run {
            if (scores.size == 100 && extraQs.firstOrNull() == TenQs.last())
                null
            else if (extraQs.size > 1 && scores.size == 100 + 10 * (extraQs.size - 1)) {
                // note that empty questions seem to always be at the end
                // so if there are more scores than questions, assume they correspond to empty questions
                // similarly, if questions are default when empty is ignored, input will be null
                TenQs.plus(extraQs.drop(1).filter { it.isNotBlank() })
                    .map { QsMap[it] ?: throw IllegalStateException("Unknown question: $it") }
                    .takeIf { list -> list != TenQs.indices.map { "$it" } }
            } else if (extraQs.isEmpty() && scores.isEmpty())
                emptyList()
            else // only a handful of values (for Soring  2014) - 32 by my count
                listOf("UNKNOWN")
        },
    )
}

private fun getSpring2014Map(): EntriesMap {
    return LocalFileSource()
        .getAllGenericEntries<EntryOld3>("old-json/json-data-4") // last compilation of original Spring 2014 data
        .mapValues { (_, deptMap) ->
            deptMap.mapValues { (_, entries) ->
                entries
                    .filter { it.term == "Spring  2014" }
                    .map { it.toEntry() }
                    .filter { it.questions != listOf("UNKNOWN") } // since data is incomplete, it will not be used
            }
        }
}

fun storeRecoveredEntries(dir: String) = getSpring2014Map().writeToDir(dir)