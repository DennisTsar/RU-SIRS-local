package old_data

import api.LocalApi
import general.Entry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import misc.makeFileAndDir


inline fun <reified T> overwriteDir(
    dirNum: Int,
    localEntryApi: LocalApi,
    mapper: (T) -> Entry,
) {
    localEntryApi.getAllEntriesInDir<T>("old-json/json-data-$dirNum/")
        .forEach { (school, deptsMap) ->
            deptsMap.forEach { (dept, oldEntries) ->
                val newList = oldEntries.map(mapper)
                val file = makeFileAndDir("json-data-$dirNum/$school/$dept.json")
                file.writeText(Json.encodeToString(newList))
            }
        }
}

fun completeOverwrite(overwriteRange: IntRange = 1..6) {
    val localEntryApi = LocalApi()
    for (i in overwriteRange) {
        when (i) {
            1 -> overwriteDir<EntryOld1>(i, localEntryApi) { oldEntry ->
                Entry(
                    instructor = oldEntry.instructor,
                    term = oldEntry.term,
                    code = oldEntry.code,
                    courseName = oldEntry.courseName,
                    indexNum = oldEntry.indexNum,
                    note = null,
                    enrolled = oldEntry.enrolled,
                    responses = oldEntry.responses,
                    scores = oldEntry.scores,
                    questions = listOf("UNKNOWN"),
                )
            }
            2, 3 -> overwriteDir<EntryOld2>(i, localEntryApi) { oldEntry ->
                Entry(
                    instructor = oldEntry.instructor,
                    term = oldEntry.term,
                    code = oldEntry.code,
                    courseName = oldEntry.courseName,
                    indexNum = oldEntry.indexNum,
                    note = oldEntry.note,
                    enrolled = oldEntry.enrolled,
                    responses = oldEntry.responses,
                    scores = oldEntry.scores,
                    questions = listOf("UNKNOWN"),
                )
            }
            4 -> overwriteDir<EntryOld3>(i, localEntryApi) { oldEntry ->
                Entry(
                    instructor = oldEntry.instructor,
                    term = oldEntry.term,
                    code = oldEntry.code,
                    courseName = oldEntry.courseName,
                    indexNum = oldEntry.indexNum,
                    note = oldEntry.note,
                    enrolled = oldEntry.enrolled,
                    responses = oldEntry.responses,
                    scores = oldEntry.scores,
                    questions = listOf("UNKNOWN_BUT_EXTRA_QS") + oldEntry.extraQs,
                )
            }
            5, 6 -> overwriteDir<Entry>(i, localEntryApi) { it }
        }
    }
}