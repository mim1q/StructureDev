package dev.mim1q.structuredev.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import dev.mim1q.structuredev.util.suggestsSubPaths
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootDataType
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

fun CommandDispatcher<ServerCommandSource>.registerGiveChestsCommand() {
    register(
        literal("structuredev:give_chests")
            .then(
                argument("prefix", StringArgumentType.string())
                    .suggestsSubPaths { ctx ->
                        ctx.source.world.server.lootManager.getIds(LootDataType.LOOT_TABLES).stream()
                    }
                    .executes { ctx ->
                        val prefix = StringArgumentType.getString(ctx, "prefix")
                        ctx.source.world.server.lootManager.getIds(LootDataType.LOOT_TABLES)
                            .filter { it.toString().startsWith(prefix) }
                            .forEach {
                                ctx.source.player?.giveItemStack(ItemStack(Blocks.CHEST).apply {
                                    setSubNbt(
                                        "BlockEntityTag",
                                        NbtCompound().apply {
                                            putString("LootTable", it.toString())
                                        }
                                    )
                                })
                            }
                        return@executes 0
                    }
            )
    )
}