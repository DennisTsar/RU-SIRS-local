package general

import api.Repository
import dto.Course
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import logic.GetEntriesMap
import logic.csvFromEntries
import logic.formatName
import logic.parseName
import misc.makeFileAndDir
import misc.pmap

val globalSetOfQs = mutableSetOf<String>()

fun main() {
    val repository = Repository()

//    val schoolDeptsMap =
//        GetSchoolsMap.fromLocalCode()

    getInstructors(repository, writeDir = "extra-json-data/F22-instructors")

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
//        GetEntriesMap.fromLocalFile<Entry>("json-data-6/")
//    map.forEach { (school, deptMap) ->
//        deptMap.map { (dept, entries) ->
////            listOf(school,dept) to (entries.profNumFromEntries() ?: 0)
//            var a: Map<String, List<Entry>> = emptyMap()
//            var b: Map<String, List<Entry>> = emptyMap()
//            val r = measureNanoTime {
//                b = entries.profNumFromEntriesA()
//            }
//            val rr = measureNanoTime {
//                a = entries.profNumFromEntriesB()
//            }
//
//            println("${r>rr}")
//        }
//    }//.sortedBy { -it.second }//.forEach(::println)
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

fun List<Entry>.profNumFromEntries(): Int {
    val names = map { formatName(it.instructor) }
    val mapOfProfs = groupBy { parseName(it.instructor, names) }
        .filterKeys { it.isNotEmpty() && it != "TA" }

    return groupBy {
        parseName(formatName(it.instructor), names)
    }.filterKeys { it.isNotEmpty() && it != "TA" }.size
}

fun List<Entry>.profNumFromEntriesA(): Map<String, List<Entry>> {
    val names = map { formatName(it.instructor) }.distinct().groupBy {
        it.substringBefore(",")
    }.filterValues { it.size == 2 }
        .mapValues {
            it.value.first { j -> j.contains(",") }
        }

    map { formatName(it.instructor) }.toSet().distinctBy { }

    return groupBy {
        parseName2(formatName(it.instructor), names)
    }.filterKeys { it.isNotEmpty() && it != "TA" }
}

fun List<Entry>.profNumFromEntriesB(): Map<String, List<Entry>> {
    val names = map { formatName(it.instructor) }.toSet().groupBy {
        it.substringBefore(",")
    }.filterValues { it.size == 2 }
        .mapValues {
            it.value.first { j -> j.contains(",") }
        }

    map { formatName(it.instructor) }.toSet().distinctBy { }

    return groupBy {
        parseName2(formatName(it.instructor), names)
    }.filterKeys { it.isNotEmpty() && it != "TA" }
}

private fun parseName2(name: String, names: Map<String, String>): String {
//    if (name.contains(','))
//        return name
    return names[name] ?: name
}

// Used to keep old entries that have been removed from website
fun getOldEntriesFromLocal(): Map<String, Map<String, List<Entry>>> {
    return GetEntriesMap.fromLocalFile<EntryOld2>("json-data-4").mapValues { (_, entriesByDept) ->
        entriesByDept.mapValues { (_, oldEntries) ->
            oldEntries.filter { it.term == "Spring  2014" }.map { entry ->
                val questions = entry.extraQs.run {
                    if (entry.scores.size == 100 && equals("I rate the overall quality of the course as"))
                        null
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

fun List<Entry>.encodeEntriesToJSON(): String =
    Json.encodeToString(this)

//To get CSV, pass ::csvFromEntries, to get JSONs, pass ::General.encodeEntriesToJSON
fun parseDeptsFromSIRS(
    repository: Repository,
    schoolDeptsMap: Map<String, School>,
    stringForFile: (List<Entry>) -> String?,
    writeDir: String,
    extraEntries: Map<String, Map<String, List<Entry>>> = emptyMap(),
) {
    schoolDeptsMap.forEach { (school, value) ->
        value.depts.forEach dept@{ dept ->
            //Wanted to have "launch" here for async but that breaks Rutgers servers
            val entries = runBlocking { getDeptEntriesFromSIRS(repository, school, dept) }.ifEmpty { return@dept }
                .plus(extraEntries[school]?.get(dept).orEmpty())
            globalSetOfQs.addAll(entries.map { it.questions ?: emptyList() }.flatten())
            println("banana $globalSetOfQs")

            stringForFile(entries)?.let {
                val file = makeFileAndDir("$writeDir/$school/$dept.txt")
                file.writeText(it)
            }
        }
    }
}

fun parseEntriesFromSIRS(
    repository: Repository,
    schoolDeptsMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    extraEntries: Map<String, Map<String, List<Entry>>> = emptyMap(), // for old entries no longer in SIRS
) {
    schoolDeptsMap
        .forEach { (school, value) ->
            // ensures depts only present in extraEntries are preserved
            val extra = extraEntries[school]?.keys ?: emptyList()
            (value.depts + extra).map { dept ->
                //Wanted to have "launch" here for async but that breaks Rutgers servers
                val entries = runBlocking { getDeptEntriesFromSIRS(repository, school, dept) }
                    .plus(extraEntries[school]?.get(dept).orEmpty())
                    .ifEmpty { return@map null }

                dept to entries
            }.filterNotNull()
                // a few depts have ";" at end for some reason
                .groupBy({ it.first.substringBefore(";") }, { it.second })
                .mapValues { it.value.flatten() }
                .forEach { (dept, entries) ->
                    globalSetOfQs.addAll(entries.map { it.questions ?: emptyList() }.flatten())
                    stringForFile(entries)?.let {
                        val file = makeFileAndDir("$writeDir/$school/$dept.json")
                        file.writeText(it)
                    }
                }
        }
}

fun parseDeptsToCSVsFromEntriesMap(
    writeDir: String = "res/json-inverse",
    entriesMap: Map<String, Map<String, List<Entry>>> = GetEntriesMap.fromLocalFile(),
) {
    entriesMap.forEach { (k, v) ->
        v.forEach {
            val file = makeFileAndDir("$writeDir/$k/${it.key}.txt")
            csvFromEntries(it.value)?.let { s -> file.writeText(s) }
        }
    }
}

suspend fun getDeptEntriesFromSIRS(
    repository: Repository,
    school: String,
    dept: String,
    semesters: List<Int> = (4028..4044).toList(),
): List<Entry> {
    return semesters.pmap { i ->
        repository.getByDeptOrCourse(if (i % 2 == 0) "Spring" else "Fall", i / 2, school, dept)
            .split("\t\t<strong>  ").drop(1)
            .map(::Entry)
//                .filter { it.scores.size==100 }
    }.flatten()
}

fun getInstructors(
    repository: Repository,
    year: String = "2022",
    term: String = "9",
    campuses: List<String> = listOf("NB", "CM", "NK"),
    writeDir: String? = null,
): Map<String, List<String>> {
    val instructorsByCourse = runBlocking {
        campuses.map { campus ->
            val res = repository.getCourses(year, term, campus)
            Json.decodeFromString<List<Course>>(res)
        }.flatten()
            .distinct()
            .groupBy(Course::courseString) { course ->
                course.sections
                    .flatMap { section ->
                        section.instructors.map { it.name }
                    }.distinct()
            }.mapValues { it.value.flatten().sorted() }

    }
    writeDir?.let {
        val file = makeFileAndDir("$writeDir.json")
        file.writeText(Json.encodeToString(instructorsByCourse.filterValues { it.isNotEmpty() }))
    }
    println(instructorsByCourse.size)
    println(instructorsByCourse.filterValues { it.isNotEmpty() }.size)
    println(instructorsByCourse.filterValues { it.size > 2 }.size)
    return instructorsByCourse
}