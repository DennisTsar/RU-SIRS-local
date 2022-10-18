package misc

import Entry
import EntriesMap
import remote.sources.LocalSource

fun validateMapOfEntries(
    map1: EntriesMap = LocalSource().getAllEntriesInDir(),
    map2: EntriesMap,
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

fun compareDataDirs(dir1: String, dir2: String, compareSizes: Boolean = true) {
    val localSource = LocalSource()
    val map1 = localSource.getAllEntriesInDir<Entry>(dir1)
    val map2 = localSource.getAllEntriesInDir<Entry>(dir2)

    // region silliness
    val compareSchoolsMap: Map<*, Map<*, List<*>>>.(Map<*, *>, String, String) -> Unit =
        { otherMap, dir, otherDir ->
            filterKeys { it !in otherMap.keys }
                .takeIf { it.isNotEmpty() }
                ?.let { schoolsMap ->
                    println("Schools in $dir but not in $otherDir:")
                    schoolsMap.forEach { (k, v) -> println("$k: ${v.map { "${it.key}=${it.value.size}" }}") }
                }
        }
    val compareDeptsMap: Map<*, List<*>>.(Map<*, *>, String, String, String) -> Unit =
        { otherMap, dir, otherDir, school ->
            filterKeys { it !in otherMap.keys }
                .takeIf { it.isNotEmpty() }
                ?.let {
                    println("Depts in $dir but not in $otherDir (School=$school):")
                    it.forEach { (k, v) -> println("$k: ${v.size}") }
                }
        }
    // endregion

    map1.compareSchoolsMap(map2, dir1, dir2)
    map2.compareSchoolsMap(map1, dir2, dir1)

    val sharedSchools = map1.keys.filter { it in map2.keys }

    sharedSchools.forEach { school ->
        val deptMap1 = map1[school]!!
        val deptMap2 = map2[school]!!

        deptMap1.compareDeptsMap(deptMap2, dir1, dir2, school)
        deptMap2.compareDeptsMap(deptMap1, dir2, dir1, school)

        if (!compareSizes)
            return@forEach

        val sharedDepts = deptMap1.keys.filter { it in deptMap2.keys }

        sharedDepts.forEach { dept ->
            val map1Course = deptMap1[dept]!!
            val map2Course = deptMap2[dept]!!

            if (map1Course.size != map2Course.size)
                println("$school-$dept: $dir1 size: ${map1Course.size}, $dir2 size: ${map2Course.size}")
        }
    }
}