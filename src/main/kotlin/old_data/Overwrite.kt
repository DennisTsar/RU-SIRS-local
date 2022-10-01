package old_data

import remote.api.LocalApi
import general.Entry
import general.writeToDir


inline fun <reified T> overwriteDir(
    dirNum: Int,
    localEntryApi: LocalApi,
    toEntry: T.() -> Entry,
) {
    localEntryApi.getAllEntriesInDir<T>("old-json/json-data-$dirNum/")
        .mapValues { (_, deptsMap) ->
            deptsMap.mapValues { (_, oldEntries) ->
                oldEntries.map { it.toEntry() }
            }
        }.writeToDir("json-data-$dirNum")
}

fun completeOverwrite(overwriteRange: List<Int> = (1..6).toList()) {
    val localEntryApi = LocalApi()
    for (i in overwriteRange) {
        when (i) {
            1 -> overwriteDir<EntryOld1>(i, localEntryApi) {
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
            2, 3 -> overwriteDir<EntryOld2>(i, localEntryApi) {
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
            4 -> overwriteDir<EntryOld3>(i, localEntryApi) {
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
            5, 6 -> overwriteDir<Entry>(i, localEntryApi) { this } // just to change to .json
        }
    }
}