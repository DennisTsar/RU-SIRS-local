package misc

import api.LocalApi
import general.Entry

fun validateMapOfEntries(
    map1: Map<String, Map<String, List<Entry>>> = LocalApi().getAllEntriesInDir(),
    map2: Map<String, Map<String, List<Entry>>>,
) {
    map1.forEach { (k, v) ->
        val p = map2.getOrElse(k) {
            println("error: $k")
            return@forEach
        }
        p.forEach two@{ (k2, v2) ->
            val q = p.getOrElse(k2) {
                println("error2: $k $k2")
                return@two
            }
            println(q == v2)
        }
    }
}