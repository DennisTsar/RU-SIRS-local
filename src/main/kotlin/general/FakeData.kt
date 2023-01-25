@file:Suppress("unused", "UNUSED_VARIABLE")

package general

import Instructor
import InstructorStats
import SchoolDeptsMap
import Semester
import SemesterType
import combine
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mapEachDept
import remote.GithubSource
import remote.WebsitePaths
import remote.getAllData
import remote.sources.LocalFileSource
import remote.sources.LocalWebsiteSource
import java.io.File
import kotlin.random.Random

private val fakeLocalSource = LocalFileSource(
    sitePaths = WebsitePaths(
        baseDir = "../fake-data",
        allInstructorsFile = "../fake-data/data-9-by-prof-stats/allInstructors.json", // will be removed soon
    )
)

fun generateFakeStatsData(localSource: LocalFileSource = LocalFileSource()): Map<String, InstructorStats> {
    val limitedSchools = localSource.getSchoolMap().toList()
        .shuffled().take(5)
        .map { (_, school) -> school.copy(depts = school.depts.shuffled().take(5).toSet()) }

    val fakeData: SchoolDeptsMap<Map<String, InstructorStats>> = limitedSchools.associate { school ->
        school.code to school.depts.associateWith { dept ->
            val courses = localSource.getCourseNames(school.code, dept).keys.shuffled().take(5)
                .ifEmpty { listOf("111") }
            println(courses)

            val profs = fakeNames.shuffled().take(Random.nextInt(1, 20))
                .map { it.split(" ").let { (first, last) -> "$last, $first".uppercase() } }

            profs.associateWith {
                val courseStats = courses.shuffled().take(Random.nextInt(1, 5))
                    .associateWith {
                        List(9) {
                            List(5) { Random.nextInt(from = 0, until = 10) }
                        }
                    }
                InstructorStats(
                    lastSem = Random.nextInt(
                        from = Semester(SemesterType.Spring, 2019).numValue,
                        until = Semester(SemesterType.Spring, 2021).numValue
                    ),
                    overallStats = courseStats.values.combine(),
                    courseStats = courseStats
                )
            }
        }
    }.writeToDir("../fake-data/data-9-by-prof-stats")
    return emptyMap()
}

fun generateFakeExtraData(localSource: LocalFileSource = fakeLocalSource) {
    val allStats = localSource.getAllStatsByProf()
    val schoolMap = localSource.getSchoolMap()

    val limitedCourseNames: SchoolDeptsMap<Map<String, String>> = schoolMap.toList()
        .associate { (code, school) ->
            code to school.depts.associateWith { localSource.getCourseNames(code, it) }
        }.writeToDir("../fake-data/extra-data/courseNames", writeSchoolMap = false)

    val teachingMap: SchoolDeptsMap<Map<String, List<String>>> = allStats.mapEachDept { _, _, statsByProf ->
        val activeCourses = statsByProf.flatMap { it.value.courseStats.keys }.shuffled().take(3)

        val courseToProfs = activeCourses.associateWith {
            val profs = statsByProf.keys.shuffled().take(Random.nextInt(1, 3))
            profs
        }

        val profToCourses = courseToProfs.flatMap { (course, profs) ->
            profs.map { it to course }
        }.groupBy({ it.first }, { it.second })

        courseToProfs + profToCourses
    }.writeToDir("../fake-data/extra-data/S23-teaching", writeSchoolMap = false)

    val allInstructors: Map<String, List<Instructor>> = allStats.mapValues { (schoolCode, deptMap) ->
        deptMap.flatMap { (deptCode, statsByProf) ->
            statsByProf.map { (profName, stats) ->
                Instructor(
                    name = profName,
                    school = schoolCode,
                    dept = deptCode,
                    latestSem = Semester(stats.lastSem),
                )
            }
        }
    }
    val file = File("../fake-data/data-9-by-prof-stats/allInstructors.json")
    file.writeText(Json.encodeToString(allInstructors))

    val deptNames = localSource.getDeptMap()
    val file2 = File("../fake-data/extra-data/deptNameMap.json")
    file2.writeText(Json.encodeToString(deptNames))
}

// make sure this doesn't crash
fun getAllFakeData(localSource: LocalFileSource = fakeLocalSource, print: Boolean = true) {
    localSource.getAllStatsByProf().also { if (print) println(it) }
    runBlocking { LocalWebsiteSource(localSource).getAllData(school = "04", dept = "189", print = print) }
}

fun getFakeDataFromGH() {
    val fakeGHSource = GithubSource(
        repoPath = "/DennisTsar/RU-SIRS/master/",
        paths = WebsitePaths(
            baseDir = "fake-data",
            allInstructorsFile = "fake-data/data-9-by-prof-stats/allInstructors.json" // will not be required soon
        ),
    )
    runBlocking { fakeGHSource.getAllData(school = "04", dept = "189", print = true) }
}

// Names from https://catonmat.net/tools/generate-random-names
private val fakeNames = listOf(
    "Dillon Kenyon",
    "Louis Cheney",
    "Arman Roush",
    "Jazmyn Liles",
    "Frank Everhart",
    "Jamaal Araujo",
    "Kailey Carlos",
    "Shemar Lay",
    "Hali Maloney",
    "Denzel Dobson",
    "Baylie Saucedo",
    "Lynette Reiter",
    "Scarlett Burt",
    "Madison Feldman",
    "Hayden Mercado",
    "Deion Cotton",
    "Jacquelin Stacey",
    "Alliyah Hartley",
    "Caylee Borges",
    "Jazlynn Wilkins",
    "Moshe Cherry",
    "Isidro McGee",
    "Marina Barragan",
    "Aracely Scholl",
    "Keyana Lindstrom",
    "Asya Amaya",
    "Shantel Hardman",
    "Augustine Larsen",
    "Brycen Whitlock",
    "Alex Villanueva",
    "Shawn Bonds",
    "Bret Heinz",
    "Shreya Held",
    "Brisa Haines",
    "Trevor Wentz",
    "Jacquez Eubanks",
    "Liberty Crowell",
    "Gino Duggan",
    "Glen Hayes",
    "Lianna Mark",
    "Monserrat Cady",
    "Monika Lovell",
    "Alina Kendrick",
    "Audrey Robins",
    "Candy Novak",
    "Neal Jeter",
    "Peter Applegate",
    "Jakayla Logan",
    "Donald Person",
    "Russell Simms",
    "Donnell White",
    "Kaylie Woo",
    "Jayde Hennessy",
    "Jaydon Sauceda",
    "Gabriela Gregory",
    "Sequoia Coburn",
    "Glenn Triplett",
    "Ricardo Conley",
    "Triston Boucher",
    "Kayley Schmidt",
    "Maximillian Houston",
    "Bree Stine",
    "Kim Padgett",
    "Sommer Rushing",
    "Asa Boothe",
    "Sarahi Cole",
    "Leland See",
    "Kirstin Pruitt",
    "Ashanti Anderson",
    "Breonna Rader",
    "Korbin Correia",
    "Gerardo Kohn",
    "Dakota Cabral",
    "Armando Fagan",
    "Sarai Gallegos",
    "Salvatore Block",
    "Rivka McCain",
    "Roberto Nunez",
    "Latrell Hamlin",
    "Bianca Linares",
    "Kennedy Slater",
    "Dameon Arenas",
    "Anisha Jorgensen",
    "Messiah Vergara",
    "Kamron Levin",
    "Dominique MacDonald",
    "Clifton Hooks",
    "Josue Earl",
    "Aiyana Shultz",
    "Dillan Keeney",
    "Sterling Cameron",
    "Corrina Ewing",
    "Denzel Galloway",
    "Roberto Cavazos",
    "Keenan Dwyer",
    "Ezequiel Brooks",
    "Rayshawn Brady",
    "Gia Demers",
    "Isis Green",
    "Elle Hurst",
)