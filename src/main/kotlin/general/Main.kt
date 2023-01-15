package general

import Campus
import EntriesByProfMap
import EntriesMap
import Entry
import Instructor
import School
import SchoolDeptsMap
import Semester
import flatMapEachDept
import forEachDept
import generateSchoolMap
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mapEachDept
import misc.makeFileAndDir
import misc.similarity
import pmap
import prev
import remote.EntriesSource
import remote.sources.LocalSource
import remote.sources.SIRSSource
import remote.sources.SOCSource
import java.io.File

val globalSetOfQs = mutableSetOf<String>()

fun main(args: Array<String>) {
    if ("-instructor" in args) {
        generateLatestProfCourseMappings()
    }
    if ("-updateByProf" in args) {
        generateEntriesByProfMap(9)
        generateCompleteProfListBySchool("json-data/data-9-by-prof")
    }
}

fun generateCompleteProfListBySchool(dataDir: String, writeToDir: Boolean = true): Map<String, List<Instructor>> {
    val profList = LocalSource().getAllEntriesByProf(dataDir)
        .flatMapEachDept { school, dept, entriesByProf ->
            entriesByProf.map { (name, entries) ->
                Instructor(name, school, dept, entries.last().semester) // entries are sorted so last() works
            }
        }.sortedBy { it.name }
        .groupBy { it.school }
    if (writeToDir) {
        val file = makeFileAndDir("$dataDir/allInstructors.json")
        file.writeText(Json.encodeToString(profList.toSortedMap().toMap()))
    }
    return profList
}

fun generateEntriesByProfMap(folderNum: Int, writeToDir: Boolean = true): EntriesByProfMap {
    if (!File("json-data/data-$folderNum-by-prof").deleteRecursively())
        throw Exception("Failed to delete json-data/data-$folderNum-by-prof")
    val localSource = LocalSource()
    val schoolMap = localSource.getSchoolMapLocal()
    return localSource.getAllEntries<Entry>("json-data/data-$folderNum")
        .toEntriesByProfMap()
        .filterKeys { it in schoolMap.keys }
        .mapValues { (school, entries) ->
            entries
                .filter { (dept, entriesByProf) ->
                    schoolMap[school]?.depts?.contains(dept) == true && entriesByProf.isNotEmpty()
                }.mapValues { (_, entriesByProf) ->
                    entriesByProf
                        .mapValues { (_, entries) -> entries.sortedBy { it.semester } }
                        .toSortedMap().toMap()
                }
        }.also { if (writeToDir) it.writeToDir("json-data/data-$folderNum-by-prof") }
}

fun generateCourseNameMappings(
    socSource: SOCSource = SOCSource(),
    semesters: List<Semester> = listOf(
        DefaultParams.semester.prev(3),
        DefaultParams.semester.prev(2),
        DefaultParams.semester.prev(),
        DefaultParams.semester
    ),
    writeDir: String? = "json-data/extra-data/courseNames",
): SchoolDeptsMap<Map<String, String>> {
    return runBlocking {
        Campus.values().toList().flatMap { campus ->
            semesters.flatMap { semester ->
                socSource.getCourses(semester, campus)
            }
        }
    }.map { it.courseString to it.title }
        .groupBy { it.first.split(":")[0] } // first split by school
        .mapValues { (_, pairs) ->
            pairs
                .groupBy { it.first.split(":")[1] } // then by dept
                .mapValues { (_, pairs) ->
                    pairs.associate { it.first.split(":")[2] to it.second }
                        .toSortedMap().toMap()
                }
        }.also { map ->
            writeDir?.let { map.writeToDir(it, writeSchoolMap = false) }
        }
}

//To get CSV, pass ::csvFromEntries, to get JSONs, { Json.encodeToString(this) }
fun parseDeptsFromSIRS(
    entriesSource: EntriesSource,
    schoolMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    extraEntries: EntriesMap = emptyMap(),
) {
    runBlocking { entriesSource.getAllLatestEntries(schoolMap.values) }
        .addOldEntries(extraEntries)
        .forEach { (school, deptsMap) ->
            deptsMap.forEach dept@{ (dept, entries) ->
                globalSetOfQs.addAll(entries.flatMap { it.questions.orEmpty() })
                println("banana $globalSetOfQs")

                stringForFile(entries)?.let {
                    val file = makeFileAndDir("$writeDir/$school/$dept.json")
                    file.writeText(it)
                }
            }
        }
}

fun parseEntriesFromSIRS(
    sirsSource: SIRSSource,
    schoolDeptsMap: Map<String, School>,
    stringForFile: List<Entry>.() -> String?,
    writeDir: String,
    semesters: List<Semester> = DefaultParams.sirsRange,
    extraEntries: EntriesMap = emptyMap(), // for old entries no longer in SIRS
) {
    schoolDeptsMap.forEach { (schoolCode, school) ->
        // ensures depts only present in extraEntries are preserved
        val extraDepts = extraEntries[schoolCode]?.keys.orEmpty()
        (school.depts + extraDepts).forEach depts@{ dept ->
            val oldEntries = extraEntries[schoolCode]?.get(dept).orEmpty()
            // Wanted to make this more async but that breaks Rutgers servers
            val entries = runBlocking {
                semesters.pmap { sirsSource.getEntriesByDeptOrCourse(it, schoolCode, dept) }
            }.flatten() + oldEntries

            if (entries.isEmpty()) return@depts

            globalSetOfQs.addAll(entries.mapNotNull { it.questions }.flatten())
            stringForFile(entries)?.let {
                val file = makeFileAndDir("$writeDir/$schoolCode/${dept.replace(":", "sc")}.json")
                file.writeText(it)
            }
        }
    }
}

fun generateLatestProfCourseMappings(
    semester: Semester = DefaultParams.semester,
    campuses: List<Campus> = Campus.values().toList(),
    writeDir: String? = "json-data/extra-data/S23-teaching",
): SchoolDeptsMap<Map<String, List<String>>> {
    val localSource = LocalSource()
    val socSource = SOCSource()

    val profsByDept = localSource.getAllEntriesByProf().mapEachDept { _, _, map -> map.keys }

    val courseToProfs = runBlocking { campuses.flatMap { socSource.getCourses(semester, it) } }
        .groupBy(
            keySelector = { it.courseString },
            valueTransform = { courseListing ->
                courseListing.sections.flatMap { section ->
                    // instructorsText seems to be updated sooner than instructors?
                    section.instructors.map { it.name } +
                            section.instructorsText.split(";").map { it.trim() }
                }
            }
        ).mapValues { (key, value) ->
            val existingNames = run {
                val (school, dept, _) = key.split(":")
                profsByDept[school]?.get(dept)
            } ?: return@mapValues emptyList()

            value.flatten().toSet().mapNotNull { originalName ->
                val name = originalName
                    .run { // add comma if missing between first + last name
                        if (" " in this && "," !in this) replace(" ", ", ")
                        else this
                    }.replace(".", "")
                    .let { manualNameAdjustment(it, key.substringBeforeLast(":")) }

                existingNames.singleOrNull { it.startsWith(name) }
                    ?: existingNames.singleOrNull { it == name.take(name.indexOf(",") + 3) }
                    ?: existingNames.singleOrNull { it.startsWith(name.substringBefore(",")) }
            }.sorted()
        }.filterValues { it.isNotEmpty() }
        .also { println("${it.size} courses with profs, ${it.count { (_, v) -> v.size > 1 }} with 2+ profs") }

    val finalMap = runBlocking { // doesn't make network call, but generateSchoolMap is aync
        localSource.getSchoolMapLocal().values
            .generateSchoolMap { school, dept ->
                // not very efficient to do this for every dept, but simple and still quick
                val filteredMap = courseToProfs.filterKeys { it.startsWith("$school:$dept:") }
                val profToCourses = filteredMap.flatMap { (course, profs) ->
                    profs.map { it to course }
                }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
                filteredMap + profToCourses
            }
    }

    val totalSize = finalMap.values.sumOf { subMap ->
        subMap.values.sumOf { it.size }
    }
    println("${totalSize - courseToProfs.size} profs with courses")

    writeDir?.let { finalMap.writeToDir(it) }
    return finalMap
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

//                fun List<Entry>.printExtra() {
//                    with(map { it.semester }) { println("${min()} -> ${max()}") }
//                    println(map { it.course }.toSet().sorted())
//                }
//                profMap[a]?.printExtra()
//                profMap[b]?.printExtra()

//                println("***************")
//                (profMap[a]!!+profMap[b]!!)
//                    .sortedWith(compareBy({ it.semester }, { it.course }))
//                    .forEach { println("\t${it.formatFullName()} ${it.semester} ${it.course}") }
//                println("***************")

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
