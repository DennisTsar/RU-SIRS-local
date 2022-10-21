package general

import EntriesByProfMap
import EntriesMap
import Entry
import School
import forEachDept
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mapEachDept
import misc.firstIfLone
import misc.makeFileAndDir
import misc.similarity
import pmap
import remote.EntriesSource
import remote.sources.LocalSource
import remote.sources.SIRSSource
import remote.sources.SOCSource

val globalSetOfQs = mutableSetOf<String>()

fun main(args: Array<String>) {
    if (args.firstOrNull() == "-instructor") {
        getInstructors(writeDir = "json-data/extra-data/S23-instructors")
        return
    }
    // region json-data-9
//    val localSource = LocalSource()
//    val oldEntriesMap = localSource.getAllEntriesInDir<Entry>("spring-2014-entries")
//    localSource.getAllEntriesInDir<Entry>("json-data-8")
//        .addOldEntries(oldEntriesMap)
//        .semicolonCleanup()
//        .writeToDir("json-data-9")
    // endregion

    // region json-data-8
//    val sirsSource = SIRSSource(SIRS_API_KEY)
//    val schoolsMap = runBlocking { sirsSource.getCompleteSchoolsMap() }
//    parseEntriesFromSIRS(
//        sirsSource,
//        schoolsMap,
//        { Json.encodeToString(this) },
//        "json-data-8",
//        DefaultParams.sirsRange,
//    )
    //endregion

//    val entriesMap = LocalSource().getAllEntriesInDir<Entry>("json-data-9")
//    entriesMap.toEntriesByProfMap().printPossibleNameAdjustments()

//    runBlocking {
//        val socRes = socSource.getSOCData()
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
    entriesSource: EntriesSource,
    schoolDeptsMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    extraEntries: EntriesMap = emptyMap(),
) {
    runBlocking { entriesSource.getAllLatestEntries(schoolDeptsMap.values) }.addOldEntries(extraEntries)
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
//            val entries = runBlocking { entriesSource.getLatestEntriesInDept(school, dept) }.ifEmpty { return@dept }
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
    sirsSource: SIRSSource,
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
                semesters.pmap { sirsSource.getEntriesByDeptOrCourse(it, school, dept) }
            }.flatten().plus(extraEntries[school]?.get(dept).orEmpty()).ifEmpty { return@map null }

            dept to entries
        }.filterNotNull().groupBy({ it.first }, { it.second }).mapValues { it.value.flatten() }
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
    socSource: SOCSource = SOCSource(),
    semYear: SemYear = DefaultParams.semYear,
    campuses: List<Campus> = Campus.values().toList(),
    writeDir: String? = null, // if null, don't write to file
): Map<String, List<String>> {
    return runBlocking {
        val entries = LocalSource().getAllEntriesByProfInDir().mapEachDept { _, _, map -> map.keys }

        val courseToProfs = campuses.flatMap { socSource.getCourses(semYear, it) }
            .groupBy(
                keySelector = { it.courseString },
                valueTransform = { courseListing ->
                    courseListing.sections.flatMap { section ->
                        section.instructors.map { it.name }
                    }
                }
            ).mapValues { (key, value) ->
                val existingNames = run {
                    val (school, dept, _) = key.split(":")
                    entries[school]?.get(dept)
                } ?: return@mapValues emptyList()
                value.flatten().distinct().mapNotNull { originalName ->
                    val name = originalName.run {
                        if (" " in this && "," !in this) replace(" ", ", ")
                        else this
                    }
                    existingNames.filter { it.startsWith(name) }.firstIfLone()
                        ?: existingNames.filter { it == name.take(name.indexOf(",") + 3) }.firstIfLone()
                        ?: existingNames.filter { it.startsWith(name.substringBefore(",")) }.firstIfLone()
                }.sorted()
            }.filterValues { it.isNotEmpty() }
            .also { println("${it.size} courses with profs, ${it.count { (_,v) -> v.size>1 }} with 2+ profs") }

        val profToCourses = courseToProfs.flatMap { (key, value) ->
            value.map { "$it ${key.substringBeforeLast(":")}" to key }
        }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .also { println("${it.size} profs with courses") }

        (profToCourses + courseToProfs).toSortedMap().toMap()
    }.also { instructorsMap ->
        writeDir?.let {
            val file = makeFileAndDir("$it.json")
            file.writeText(Json.encodeToString(instructorsMap))
        }
    }
}

fun EntriesByProfMap.printPossibleNameAdjustments(printURL: Boolean = true) {
    forEachDept { school, dept, profMap ->
        val names = profMap.keys.sorted()
        val pairs = names.mapIndexed { index, s ->
            if (s.substringBefore(",").length == 1)
                println("swapped? ($school-$dept): $s")
            names.drop(index + 1).map { Pair(s, it) }
        }.flatten()

        pairs.filter { (first, second) ->
            val a = first.split(" ", ",").filter { it.isNotBlank() }
            val b = second.split(" ", ",").filter { it.isNotBlank() }
            similarity(a[0], b[0]) > .75
                    || (similarity(a[0], b[0]) > .33 && similarity(a.getOrNull(1), b.getOrNull(1)) > .75)
                    // checks for flipped first/last names
                    || (similarity(a[0], b.getOrNull(1)) > .75 && similarity(a.getOrNull(1), b[0]) > .75)
        }.ifEmpty { null }?.let { filtered ->
            println("\n\"$school:$dept\" -> when (prof) {")
            filtered.sortedBy { it.second }.forEach { (a, b) ->
                print("\t\"$b\" -> \"$a\"")
                val common = b.zip(a).takeWhile { (x, y) -> x == y }
                    .map { it.first }.joinToString("").split(",")[0]
                    .ifBlank { // go based on first names if last names different
                        b.substringAfter(", ").zip(a.substringAfter(", "))
                            .takeWhile { (x, y) -> x == y }
                            .map { it.first }.joinToString("")
                    }
                if (printURL)
                    println(
                        " https://sirs.ctaar.rutgers.edu/index.php?mode=name&survey%5Blastname%5D=$common" +
                                "&survey%5Bsemester%5D=&survey%5Byear%5D=&survey%5Bschool%5D=&survey%5Bdept%5D=$dept)"
                    )
                else println()
            }
            println("\telse -> prof\n}")
        }
    }
}
