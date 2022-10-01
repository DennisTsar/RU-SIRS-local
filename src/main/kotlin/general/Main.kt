package general

import remote.api.SIRSApi
import remote.api.SOCApi
import remote.interfaces.EntriesRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import misc.makeFileAndDir
import misc.pmap

val globalSetOfQs = mutableSetOf<String>()

fun main() {
    // region json-data-9
//    val oldEntriesMap = LocalApi().getAllEntriesInDir<Entry>("spring-2014-entries")
//    repo.getAllEntriesInDir<Entry>("json-data-8")
//        .addOldEntries(oldEntriesMap)
//        .semicolonCleanup()
//        .writeToDir("json-data-9")
    // endregion

    // region json-data-8
//    val sirsApi = SIRSApi(SIRS_API_KEY)
//    val schoolsMap = runBlocking { sirsApi.getCompleteSchoolsMap() }
//    parseEntriesFromSIRS(
//        sirsApi,
//        schoolsMap,
//        { Json.encodeToString(this) },
//        "json-data-8",
//        DefaultParams.sirsRange,
//    )
    //endregion

    //region testing name issues
//    val depts = runBlocking { LocalApi().getSchoolsMap()["01"]!! }.depts
//
//    for(i in depts){
//        val entries = runBlocking { repo.getEntries("01", i) }
//
//        val a = entries.mapByProfs()
//        val b = entries.mapByProfs2()
//
//        a.forEach { (k, v) ->
//            val s = b[k] ?: run { println("no2: $k $i"); return@forEach }
//            if(s != v)
//                println("no $k $i")
//        }
//    }
    //endregion

//    runBlocking {
//        val socRes = repository.getSOCData()
//            .substringAfterBefore("<div id=\"initJsonData\" style=\"display:none;\">", "</div>")
//        Json.decodeFromString<SOCData>(socRes)
//            .subjects.associate { it.code to it.description }
//            .let {
//                val file = makeFileAndDir("extra-json-data/deptNamesMap.json")
//                file.writeText(Json.encodeToString(it))
//            }
//    }
}

//To get CSV, pass ::csvFromEntries, to get JSONs, { Json.encodeToString(this) }
fun parseDeptsFromSIRS(
    entriesRepository: EntriesRepository,
    schoolDeptsMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    extraEntries: EntriesMap = emptyMap(),
) {
    runBlocking { entriesRepository.getAllLatestEntries(schoolDeptsMap.values) }
        .addOldEntries(extraEntries)
        .forEach { (school, deptsMap) ->
            deptsMap.forEach dept@{ (dept, entries) ->
                globalSetOfQs.addAll(entries.flatMap { it.questions ?: emptyList() })
                println("banana $globalSetOfQs")

                stringForFile(entries)?.let {
                    val file = makeFileAndDir("$writeDir/$school/$dept.json")
                    file.writeText(it)
                }
            }
        }

//    schoolDeptsMap.forEach { (school, value) ->
//        value.depts.forEach dept@{ dept ->
//            // Wanted to have "launch" here for async but that breaks Rutgers servers
//            val entries = runBlocking { entriesRepository.getLatestEntriesInDept(school, dept) }.ifEmpty { return@dept }
//                .plus(extraEntries[school]?.get(dept).orEmpty())
//            globalSetOfQs.addAll(entries.map { it.questions ?: emptyList() }.flatten())
//            println("banana $globalSetOfQs")
//
//            stringForFile(entries)?.let {
//                val file = makeFileAndDir("$writeDir/$school/$dept.txt")
//                file.writeText(it)
//            }
//        }
//    }
}

fun parseEntriesFromSIRS(
    repository: SIRSApi,
    schoolDeptsMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    semesters: List<SemYear> = DefaultParams.sirsRange,
    extraEntries: EntriesMap = emptyMap(), // for old entries no longer in SIRS
) {
    schoolDeptsMap.forEach { (school, value) ->
        // ensures depts only present in extraEntries are preserved
        val extra = extraEntries[school]?.keys ?: emptySet()
        (value.depts + extra).map { dept ->
            //Wanted to have "launch" here for async but that breaks Rutgers servers
            val entries = runBlocking {
                semesters.pmap { repository.getEntriesByDeptOrCourse(it, school, dept) }
            }.flatten()
                .plus(extraEntries[school]?.get(dept).orEmpty())
                .ifEmpty { return@map null }

            dept to entries
        }.filterNotNull()
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.flatten() }
            .forEach { (dept, entries) ->
                globalSetOfQs.addAll(entries.map { it.questions ?: emptyList() }.flatten())
                stringForFile(entries)?.let {
                    val file = makeFileAndDir("$writeDir/$school/${dept.replace(":", "sc")}.json")
                    file.writeText(it)
                }
            }
    }
}

fun getInstructors(
    repository: SOCApi = SOCApi(),
    semYear: SemYear = DefaultParams.semYear,
    campuses: List<Campus> = Campus.values().toList(),
    writeDir: String? = null, // if null, don't write to file
): Map<String, List<String>> {
    return runBlocking {
        campuses
            .flatMap { repository.getCourses(semYear, it) }
            .groupBy(
                keySelector = { it.courseString },
                valueTransform = { courseListing ->
                    courseListing.sections.flatMap { section ->
                        section.instructors.map { it.name }
                    }
                }
            ).mapValues { it.value.flatten().sorted() }
            .filterValues { it.isNotEmpty() } // yes filtering is needed
    }.also { instructorsMap ->
        writeDir?.let {
            val file = makeFileAndDir("$it.json")
            file.writeText(Json.encodeToString(instructorsMap))
        }
        println("Amount of courses with at least one known prof: ${instructorsMap.size}")
        println("Amount w/at least 2 profs: ${instructorsMap.count { it.value.size > 2 }}")
    }
}