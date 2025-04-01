package dev.mim1q.structuredev.item

import dev.mim1q.access.ServerPlayerMessageRedirectAccess
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.world.World

private typealias CommandListGetter = (previousCommands: List<String>) -> List<String>
private typealias SingleCommandGetter = (previousCommands: List<String>) -> String

abstract class CommandRunnerItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        return if (runCommands(
                ItemUsageContext(
                    world, user, hand, stack,
                    BlockHitResult.createMissed(user.pos, Direction.UP, user.blockPos)
                )
            )
        ) TypedActionResult.success(stack)
        else TypedActionResult.fail(stack)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        return if (runCommands(context)) ActionResult.SUCCESS else ActionResult.FAIL
    }

    private fun runCommands(context: ItemUsageContext): Boolean {
        return CommandRunner().apply { setupCommands(context) }.runCommands(context)
    }

    abstract fun CommandRunner.setupCommands(context: ItemUsageContext)

    class CommandRunner {

        private val previousResults: MutableList<String> = mutableListOf()
        private val addedCommands: MutableList<Pair<RunnerCommandSettings, CommandListGetter>> =
            mutableListOf()

        fun addCommand(settings: RunnerCommandSettings = RunnerCommandSettings(), command: SingleCommandGetter) =
            addedCommands.add(settings to { listOf(command(it)) })

        fun addCommands(settings: RunnerCommandSettings = RunnerCommandSettings(), commands: CommandListGetter) =
            addedCommands.add(settings to commands)

        internal fun runCommands(context: ItemUsageContext): Boolean {
            if (addedCommands.isEmpty()) {
                return false
            }

            val player = context.player as? ServerPlayerEntity ?: return false
            val output = object : CommandOutput {
                override fun sendMessage(message: Text) {
                    previousResults.add(message.string)
                    println(previousResults)
                    //player.sendMessageToClient(message, false)
                }

                override fun shouldReceiveFeedback(): Boolean = true
                override fun shouldTrackOutput(): Boolean = true
                override fun shouldBroadcastConsoleToOps() = false
            }
            val source = ServerCommandSource(
                output,
                player.pos,
                player.rotationClient,
                player.world as ServerWorld,
                player.server.getPermissionLevel(player.gameProfile),
                player.name.string,
                player.displayName,
                player.world.server,
                player
            )

            addedCommands.forEach {
                val (settings, commandListGetter) = it
                val commandList = commandListGetter.invoke(previousResults)

                if (commandList.isNotEmpty()) {
                    if (settings.redirectFromPlayerOutput) {
                        (player as ServerPlayerMessageRedirectAccess).`structuredev$redirectMessagesTo`(output)
                    }

                    commandList.forEach runSingle@{ command ->
                        if (command.isBlank()) return@runSingle
                        player.server.commandManager.executeWithPrefix(source, command)
                    }

                    if (settings.redirectFromPlayerOutput) {
                        (player as ServerPlayerMessageRedirectAccess).`structuredev$stopRedirectingMessages`()
                    }
                }
            }

            return true
        }
    }

    data class RunnerCommandSettings(
        val redirectFromPlayerOutput: Boolean = false
    )
}