package misc

import java.io.File

fun File.walkDirectory(): Sequence<File> = walk().drop(1)

fun makeFileAndDir(filename: String): File = File(filename).apply { parentFile.mkdirs() }