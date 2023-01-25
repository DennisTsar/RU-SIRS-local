package misc

import data.Entry
import data.old_data.EntryOld1
import data.old_data.EntryOld2
import data.old_data.EntryOld3
import general.writeToDir
import remote.sources.LocalFileSource


inline fun <reified T> overwriteDir(
    dirNum: Int,
    localSource: LocalFileSource,
    toEntry: T.() -> Entry,
) {
    localSource.getAllGenericEntries<T>("old-json/json-data-$dirNum/")
        .mapValues { (_, deptsMap) ->
            deptsMap.mapValues { (_, oldEntries) ->
                oldEntries.map { it.toEntry() }
            }
        }.writeToDir("json-data-$dirNum")
}

fun completeOverwrite(overwriteRange: List<Int> = (1..6).toList()) {
    val localSource = LocalFileSource()
    for (i in overwriteRange) {
        when (i) {
            1 -> overwriteDir<EntryOld1>(i, localSource) {
                Entry(
                    instructor = instructor,
                    term = term,
                    code = code,
                    courseName = courseName,
                    indexNum = indexNum,
                    note = "UNKNOWN",
                    enrolled = enrolled,
                    responses = responses,
                    scores = scores,
                    questions = listOf("UNKNOWN"),
                )
            }

            2, 3 -> overwriteDir<EntryOld2>(i, localSource) {
                Entry(
                    instructor = instructor,
                    term = term,
                    code = code,
                    courseName = courseName,
                    indexNum = indexNum,
                    note = note,
                    enrolled = enrolled,
                    responses = responses,
                    scores = scores,
                    questions = listOf("UNKNOWN"),
                )
            }

            4 -> overwriteDir<EntryOld3>(i, localSource) {
                Entry(
                    instructor = instructor,
                    term = term,
                    code = code,
                    courseName = courseName,
                    indexNum = indexNum,
                    note = note,
                    enrolled = enrolled,
                    responses = responses,
                    scores = scores,
                    questions = listOf("UNKNOWN_BUT_EXTRA_QS") + extraQs,
                )
            }

            5, 6 -> overwriteDir<Entry>(i, localSource) { this } // just to change to .json
        }
    }
}