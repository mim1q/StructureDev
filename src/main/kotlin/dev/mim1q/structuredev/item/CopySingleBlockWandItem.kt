package dev.mim1q.structuredev.item

import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class CopySingleBlockWandItem(settings: Settings) : CommandRunnerItem(settings) {

    override fun CommandRunner.setupCommands(context: ItemUsageContext) {
        val player = context.player as? ServerPlayerEntity ?: return
        val playerPos = player.pos

        addCommand(
            RunnerCommandSettings(
                redirectFromPlayerOutput = true
            )
        ) { "//size" }
        addCommand { "//hpos1" }
        addCommand { "//hpos2" }
        addCommand { "//copy" }

        addCommands { previous ->
            println(previous)
            if (previous.isEmpty()) {
                return@addCommands emptyList()
            }

            val result = previous.subList(1, 3)
                .map { it.substringAfter('(').substringBefore(')') }
                .map {
                    it.split(", ")
                        .map { xyz -> xyz.toInt() }
                        .let { pos -> BlockPos(pos[0], pos[1], pos[2]) }
                }

            listOf(
                "/tp @s ${result[0].x} ${result[0].y} ${result[0].z}",
                "//pos1",
                "/tp @s ${result[1].x} ${result[1].y} ${result[1].z}",
                "//pos2",
                "/tp @s ${playerPos.x} ${playerPos.y} ${playerPos.z}",
            )
        }
    }
}