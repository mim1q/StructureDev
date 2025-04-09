package dev.mim1q.structuredev.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import dev.mim1q.structuredev.generator.TemplateGenerator
import dev.mim1q.structuredev.util.suggestsSubPaths
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.math.BlockPos

fun CommandDispatcher<ServerCommandSource>.registerGenerateTemplatesCommand() {
    register(
        literal("structuredev:generate_templates")
            .then(
                argument("prefix", StringArgumentType.string())
                    .suggestsSubPaths { ctx-> ctx.source.server.structureTemplateManager.streamTemplates() }
                    .executes { ctx ->
                        val prefix = StringArgumentType.getString(ctx, "prefix")
                        TemplateGenerator(
                            ctx.source.world,
                            ctx.source.entity?.blockPos ?: BlockPos.ORIGIN
                        ).generateWithPrefix(prefix)
                        return@executes 0
                    }
            )
    )
}