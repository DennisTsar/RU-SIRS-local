package misc

import java.io.File

fun File.walkDirectory(): Sequence<File> = walk().drop(1)

fun makeFileAndDir(filename: String): File = File(filename).apply { parentFile.mkdirs() }

fun <T> List<T>.firstIfLone(): T? = if (size == 1) first() else null