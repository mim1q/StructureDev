package dev.mim1q.structuredev.mixindelegates.client

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object BlockItemMixinDelegate {
    fun getName(stack: ItemStack, previous: Text): Text? {
        val lootTable = (stack.nbt?.get("BlockEntityTag") as? NbtCompound)?.getString("LootTable")

        if (lootTable === null || lootTable.isEmpty()) return null

        previous.siblings.add(Text.literal(" [$lootTable]").styled { it.withColor(Formatting.RED) })
        return previous
    }
}