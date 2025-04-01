package dev.mim1q.structuredev.item

import dev.mim1q.structuredev.StructureDev
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

class StructureDevItems {
    val copySingleBlockWand = registerItem("copy_single_block_wand", CopySingleBlockWandItem(Item.Settings()))

    private fun <I : Item> registerItem(name: String, item: I): I = Registry.register(
        Registries.ITEM,
        StructureDev.id(name),
        item
    )
}