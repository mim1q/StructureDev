package dev.mim1q.structuredev.util

import java.util.stream.Stream
import kotlin.streams.asSequence

fun String.streamSubPaths(): Sequence<String> {
    var subPath = this
    return generateSequence {
        if (subPath.isEmpty()) null
        else {
            val temp = subPath
            val index = subPath.lastIndexOf('/')
            subPath = if (index == -1) "" else subPath.removeRange(index, subPath.length)
            return@generateSequence temp
        }
    }
}

fun Stream<String>.streamSubPaths(): Sequence<String> = this.asSequence().flatMap { it.streamSubPaths() }