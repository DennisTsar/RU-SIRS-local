package misc

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundToDecimal(dec: Int): Double = (this * 10.0.pow(dec)).roundToInt() / 10.0.pow(dec)

fun File.walkDirectory(): Sequence<File> = walk().drop(1)

fun makeFileAndDir(filename: String): File = File(filename).apply { parentFile.mkdirs() }
