package general

import api.LocalApi
import api.SIRSApi
import api.SOCApi
import api.interfaces.EntriesRepository
import dto.Course
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import misc.makeFileAndDir
import misc.pmap
import old_data.EntryOld3

val globalSetOfQs = mutableSetOf<String>()

fun main() {
//    val repo = SIRSApi(SIRS_API_KEY)
//    runBlocking {
//        val a = repo.getSchoolsMapUsingSOC()
////            .forEach { (k,v) -> println(k); println(v)  }
//        val r = a.toList().sortedBy { it.first }.toMap()
//            .let {
//                val file = makeFileAndDir("extra-json-data/schoolDeptsMap4.json")
//                file.writeText(Json.encodeToString(it))
//            }
//        println(Json.encodeToString(r))
////        println(SOCApi().getSOCData().units.map { it.code })
//    }

//    completeOverwrite()

    val repo = LocalApi()

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

//    getInstructors(SOCApi(), writeDir = "extra-json-data/F22-instructors2")

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

    //region schools map & entries to files
//    runBlocking {
//        val schoolDeptsMap = GetSchoolsMap.fromSIRSAndSOC(repository)
//        val newDir = "json-data-8"
//        parseEntriesFromSIRS(
//            repository,
//            schoolDeptsMap,
//            { encodeEntriesToJSON() },
//            newDir,
//            getOldEntriesFromLocal()
//        )
//        val schoolDeptsMapFromEntries = GetEntriesMap.fromLocalFile<Entry>(newDir)
//            .mapValues { (_, dept) ->
//                dept.mapValues {
//                    it.value.ifEmpty { null }
//                }.filterValues { it != null }
//                    .keys
//            }
//       schoolDeptsMap
//           .mapValues { (_, school) ->
//                schoolDeptsMapFromEntries[school.code]?.let {
//                    if(school.depts == it)
//                        school
//                    else
//                        school.copy(depts = it)
//                }
//            }.filterValues { it != null }
//           .toList().sortedBy { it.first }.toMap()
//           .let {
//                val file = makeFileAndDir("extra-json-data/schoolDeptsMap4.json")
//                file.writeText(Json.encodeToString(it))
//            }
//    }
    // endregion

//    val file = makeFileAndDir("json-data/schoolDeptsMap.txt")
//    file.writeText(Json.encodeToString(schoolDeptsMap))

//    schoolMapToCode(Json.decodeFromString(File("json-data/schoolDeptsMap.txt").readText()))

//    val map =
//        GetEntriesMap.fromLocalFile<Entry>("json-data-7/")
//    map.map { (school, deptMap) ->
//        deptMap.map { (dept, entries) ->
//            var z: Map<String, List<Entry>> = emptyMap()
//            var zz: Map<String, List<Entry>> = emptyMap()
////            val b = measureNanoTime {
////                zz = entries.mapByProfs2()
////            }
//            measureNanoTime {
//                z = entries.mapByProfs2()
//            }
//        }.sum()
//    }.run { println(sum()) }//.sortedBy { -it.second }//.forEach(::println)
//        runBlocking { GetEntriesMap.fromGit<EntryOld2>(repository,schoolDeptsMap) }


//    val missingEntries = getOldEntriesFromLocal()
    // This is how you get the JSONs of entries
//    val k = schoolDeptsMap.toSortedMap().headMap("05").toMap()
//    println(k.keys)
//    parseDeptsFromSIRS(repository, schoolDeptsMap, ::encodeEntriesToJSON,"json-data-5", missingEntries)


    // converting full-text questions to numbers and validating - still partially manual in Entry.kt
//    val set3 = mutableSetOf<String>()
//    GetEntriesMap.fromLocalFile<Entry>("json-data-5").mapValues { (school, deptMap) ->
//        deptMap.mapValues { (dept, entries) ->
//            val newEntries = entries.filter { it.term.split(" ").last().toInt()>2018 }.map { entry ->
//                if (entry.questions?.contains("What grade do you expect to receive in this class?") == true)
//                    println(entry)
//                Entry(
//                    instructor = entry.instructor,
//                    term = entry.term,
//                    code = entry.code,
//                    courseName = entry.courseName,
//                    indexNum = entry.indexNum,
//                    note = entry.note,
//                    enrolled = entry.enrolled,
//                    responses = entry.responses,
//                    scores = entry.scores,
//                    questions = entry.questions?.map {
//                        if(!QsMap.containsKey(it) && !(0..9).toList().contains(it.toIntOrNull()))
//                            println(it)
//                        set3.add(it)
//                        QsMap[it] ?: it
//                     },
//                )
//            }
////            newEntries.encodeEntriesToJSON().let {
////                val file = makeFileAndDir("json-data-6/$school/$dept.txt")
////                file.writeText(it)
////            }
//        }
//    }
////    println(QsMap.keys.minus(TenQs).minus(set3))
//    set3.forEach(::println)

    // seems to be looking at which depts have non-100 length entries
//    schoolDeptsMap.forEach { (school, value) ->
//    val less = mutableListOf<String>()
//    val more = mutableListOf<String>()
//        schoolDeptsMap["01"]?.depts?.forEach dept@{dept ->
//            //Wanted to have "launch" here for async but that breaks Rutgers servers
//            val entries = runBlocking { getDeptEntriesFromSIRS(repository,"01", dept, listOf(4035,4038)) }.ifEmpty { return@dept }
//            val j = entries.filter { it.scores.size>100 }
//            if(j.isNotEmpty()) {
//                more.add(dept)
//                println("hey ${j.first().code} ${j.first().term} ${j.map { it.scores.size }.toSet()}")
//            }
//            val k = entries.filter { it.scores.size<100 }
//            if(k.isNotEmpty()) {
//                less.add(dept)
//                println("hi ${k.first().code} ${k.first().term} ${k.map { it.scores.size }.toSet()}")
//            }
//        }
//    println(less)
//    println(more)
//    }
}

// Used to keep old entries that have been removed from website
fun getOldEntriesFromLocal(): Map<String, Map<String, List<Entry>>> {
    return LocalApi().getAllEntriesInDir<EntryOld3>("json-data-4").mapValues { (_, entriesByDept) ->
        entriesByDept.mapValues { (_, oldEntries) ->
            oldEntries.filter { it.term == "Spring  2014" }.map { entry ->
                val questions = entry.extraQs.run {
                    if (entry.scores.size == 100 && equals("I rate the overall quality of the course as")) null
                    else if (entry.scores.size == 100 + 10 * (size - 1)) {
                        // note that empty questions seem to always be at the end
                        val mappedQs = TenQs.plus(drop(1).filter { it != "" }).map { QsMap.getOrElse(it) { it } }
                        if (mappedQs == QsMap.values.toList()) null else mappedQs
                    } else { // only a handful of values (for Soring  2014)
                        listOf("Unknown")
                    }
                }
                Entry(
                    instructor = entry.instructor,
                    term = entry.term,
                    code = entry.code,
                    courseName = entry.courseName,
                    indexNum = entry.indexNum,
                    note = entry.note,
                    enrolled = entry.enrolled,
                    responses = entry.responses,
                    scores = entry.scores,
                    questions = questions,
                )
            }
        }
    }
}

fun List<Entry>.encodeEntriesToJSON(): String = Json.encodeToString(this)

//To get CSV, pass ::csvFromEntries, to get JSONs, pass ::General.encodeEntriesToJSON
fun parseDeptsFromSIRS(
    entriesRepository: EntriesRepository,
    schoolDeptsMap: Map<String, School>,
    stringForFile: (List<Entry>) -> String?,
    writeDir: String,
    extraEntries: Map<String, Map<String, List<Entry>>> = emptyMap(),
) {
    val entriesMap = runBlocking { entriesRepository.getAllEntries(schoolDeptsMap.values) }
    entriesMap.forEach { (school, deptsMap) ->
        deptsMap.forEach dept@{ (dept, entries) ->
            val allEntries = entries.plus(extraEntries[school]?.get(dept).orEmpty()).ifEmpty { return@dept }
            globalSetOfQs.addAll(allEntries.flatMap { it.questions ?: emptyList() })
            println("banana $globalSetOfQs")

            stringForFile(allEntries)?.let {
                val file = makeFileAndDir("$writeDir/$school/$dept.json")
                file.writeText(it)
            }
        }
    }

//    schoolDeptsMap.forEach { (school, value) ->
//        value.depts.forEach dept@{ dept ->
//            // Wanted to have "launch" here for async but that breaks Rutgers servers
//            val entries = runBlocking { getDeptEntriesFromSIRS(repository, school, dept) }.ifEmpty { return@dept }
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
    extraEntries: Map<String, Map<String, List<Entry>>> = emptyMap(), // for old entries no longer in SIRS
) {
    schoolDeptsMap.forEach { (school, value) ->
        // ensures depts only present in extraEntries are preserved
        val extra = extraEntries[school]?.keys ?: emptyList()
        (value.depts + extra).map { dept ->
            //Wanted to have "launch" here for async but that breaks Rutgers servers
            val entries = runBlocking {
                getDeptEntriesFromSIRS(
                    repository,
                    school,
                    dept
                )
            }.plus(extraEntries[school]?.get(dept).orEmpty()).ifEmpty { return@map null }

            dept to entries
        }.filterNotNull()
            // a few depts have ";" at end for some reason
            .groupBy({ it.first.substringBefore(";") }, { it.second }).mapValues { it.value.flatten() }
            .forEach { (dept, entries) ->
                globalSetOfQs.addAll(entries.map { it.questions ?: emptyList() }.flatten())
                stringForFile(entries)?.let {
                    val file = makeFileAndDir("$writeDir/$school/$dept.json")
                    file.writeText(it)
                }
            }
    }
}

suspend fun getDeptEntriesFromSIRS(
    repository: SIRSApi,
    school: String,
    dept: String,
    semesters: List<SemYear> = DefaultParams.sirsRange,
): List<Entry> {
    return semesters.pmap {
        repository.getEntriesByDeptOrCourse(it, school, dept)
//                .filter { it.scores.size==100 }
    }.flatten()
}

fun getInstructors(
    repository: SOCApi = SOCApi(),
    semYear: SemYear = DefaultParams.semYear,
    campuses: List<Campus> = Campus.values().toList(),
    writeDir: String? = null, // if null, don't write to file
): Map<String, List<String>> {
    return runBlocking {
        campuses.flatMap { repository.getCourses(semYear, it) }.groupBy(Course::courseString) { courseListing ->
            courseListing.sections.flatMap { section ->
                section.instructors.map { it.name }
            }
        }.mapValues { it.value.flatten().sorted() }.filterValues { it.isNotEmpty() } // yes filtering is needed
    }.also { instructorsMap ->
        writeDir?.let {
            val file = makeFileAndDir("$it.json")
            file.writeText(Json.encodeToString(instructorsMap))
        }
        println("Amount of courses with at least one known prof: ${instructorsMap.size}")
        println("Amount w/at least 2 profs: ${instructorsMap.count { it.value.size > 2 }}")
    }
}