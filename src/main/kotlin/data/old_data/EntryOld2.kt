package data.old_data

import data.substringAfterBefore
import kotlinx.serialization.Serializable

@Serializable
class EntryOld2(
    val instructor: String,
    val term: String,
    val code: String,
    val courseName: String,
    val indexNum: String?,
    val note: String?,
    val enrolled: Int,
    val responses: Int,
    val scores: List<Double>,
) {
    constructor(s: String) : this(
        instructor = s.substringBefore("  "),
        term = s.substringAfterBefore("<br> ", "\n"),
        code = s.substringAfterBefore("<br>  ", " "),
        courseName = s.substringAfterBefore("<q>", "<").replace("&amp;", "&"),
        //not always present - generally, but not always, corresponds to class name containing "(Lecture)"
        indexNum = s.substringAfter("index #", "").substringBefore(")").ifBlank { null },
        note = s.substringAfterBefore("<q>", "<br><a")
            .substringAfter("index #")
            .substringAfter("<br>")
            .substringAfter("(")
            .substringBefore(")", "").ifBlank { null },
        enrolled = s.substringAfterBefore("Enrollment=  ", ",").toInt(),
        responses = s.substringAfterBefore("Responses= ", " ").toInt(),
        scores = s.split("<td  class=\"mono").drop(1)
            .map {
                it.substringAfterBefore(">", "<").toDouble()
            }//indices 0-99 are all the numbers for one entry, row by row
    )
//    val shortTerm = term.first()+term.takeLast(2)

    override fun toString(): String {
        return "General.Entry(\"$instructor\",\"$term\",\"$code\",\"$courseName\",\"$indexNum\",$enrolled,$responses,listOf(${
            scores.joinToString(
                ","
            )
        }))"
    }
}