package dev.mim1q.structuredev.util

import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.util.Identifier
import java.util.stream.Stream

fun <S> RequiredArgumentBuilder<S, String>.suggestsSubPaths(
    idStream: (CommandContext<S>) -> Stream<Identifier>
): RequiredArgumentBuilder<S, String> = suggests { ctx, builder ->

    idStream(ctx)
        .map { it.toString() }
        .streamSubPaths()
        .map { "\"$it\"" }
        .forEach(builder::suggest)

    return@suggests builder.buildFuture()
}