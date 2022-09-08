package misc

import general.EntryOld
import general.School

fun schoolMapToCode(myMap: Map<String, School> = emptyMap()) {
    val map = myMap.ifEmpty {
        mapOf(
            "01" to School(
                "Name2", "hi",
                setOf(
                    "asf", "zxcvz", "afsda",
                    "asf", "zxcvz", "afsda",
                )
            ),
            "01" to School(
                "Name2", "hi",
                setOf(
                    "asf", "zxcvz", "afsda",
                    "asf", "zxcvz", "afsda",
                )
            ),
        )
    }

    val compilableMap = map.entries.joinToString("\n\t),\n", prefix = "mapOf(\n", postfix = "\n)") { (code, school) ->
        "\t\"${code}\" to School(\"${school.code}\",\"${school.name}\",\n\t\tlistOf(\n\t\t\t\"" +
                school.depts.chunked(20).joinToString(",\n\t\t\t\"", postfix = ",\n\t\t)") {
                    it.joinToString("\",\"", postfix = "\"")
                }
    }
    println(compilableMap)
}

fun schoolDeptsMapToCode(myMap: Map<String, Pair<String, List<String>>> = emptyMap()) {
    val map = myMap.ifEmpty {
        mapOf(
            "01" to Pair(
                "Name2",
                listOf(
                    "asf", "zxcvz", "afsda",
                    "asf", "zxcvz", "afsda",
                )
            ),
            "02" to Pair(
                "Name2",
                listOf("asf", "zxcvz", "afsda")
            ),
        )
    }

    val compilableMap = map.entries.joinToString("\n\t),\n", prefix = "mapOf(\n", postfix = "\n)") { (code, pair) ->
        "\t\"${code}\" to Pair(\"${pair.first}\",\n\t\tlistOf(\n\t\t\t\"" +
                pair.second.chunked(20).joinToString(",\n\t\t\t\"", postfix = ",\n\t\t)") {
                    it.joinToString("\",\"", postfix = "\"")
                }
    }
    println(compilableMap)
}

@Deprecated("Not using this anymore but it does work")
fun entriesMapToCode() {
    val map =
        mapOf(
            "01" to mapOf(
                "198" to listOf(
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                ),
                "199" to listOf(
                    EntryOld("b", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                ),
            ),
            "02" to mapOf(
                "193" to listOf(
                    EntryOld("d", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                ),
                "191" to listOf(
                    EntryOld("x", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                    EntryOld("a", "a", "a", "a", "a", 5, 1, listOf(1.3, 5.4)),
                ),
            ),
        )

    val compilableMap = map.entries.joinToString("\n", postfix = "\n)") { (school, deptMap) ->
        "mapOf(\n\t\"${school}\" to mapOf(\n" +
                deptMap.entries.joinToString(",\n", postfix = ",\n\t),") { (dept, entries) ->
                    "\t\t\"${dept}\" to listOf(\n\t\t\t" +
                            entries.joinToString(",\n\t\t\t", postfix = ",\n\t\t)")
                }
    }
    val compilableFile = makeFileAndDir("src/General.main/kotlin/MyMap.kt")
    compilableFile.writeText("val myMap = \n$compilableMap")
}