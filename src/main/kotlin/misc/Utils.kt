package misc

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

fun String.substringAfterBefore(after: String, before: String): String = substringAfter(after).substringBefore(before)

fun Double.roundToDecimal(dec: Int): Double = (this * 10.0.pow(dec)).roundToInt() / 10.0.pow(dec)

//Stolen from te interwebs
suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B, C> Map<A,B>.pmap(f: suspend (Map.Entry<A,B>) -> C): List<C> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

fun File.walkDirectory(): Sequence<File> = walk().drop(1)

fun makeFileAndDir(filename: String): File = File(filename).apply { parentFile.mkdirs() }
