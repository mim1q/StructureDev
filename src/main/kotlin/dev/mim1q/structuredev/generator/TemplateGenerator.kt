package dev.mim1q.structuredev.generator

import dev.mim1q.structuredev.IdentifierTree
import dev.mim1q.structuredev.StructureDev
import net.minecraft.block.Blocks
import net.minecraft.block.StructureBlock
import net.minecraft.block.entity.StructureBlockBlockEntity
import net.minecraft.block.enums.StructureBlockMode
import net.minecraft.server.world.ServerWorld
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ServerWorldAccess

const val X_GAP = 2
const val Z_GAP = 2

class TemplateGenerator(
    private val world: ServerWorld,
    private val startPos: BlockPos,
) {

    private val allTemplates = world.structureTemplateManager.streamTemplates().toList().associate {
        it.toString() to world.structureTemplateManager.getTemplateOrBlank(it)
    }

    fun generateWithPrefix(prefix: String) {
        var xOffset = 0
        var nextXOffset = 0
        var zOffset = 0

        val tree = IdentifierTree(allTemplates.filter { it.key.startsWith(prefix) })

        tree.traverse(
            { _, subtree ->
                zOffset = 0
                xOffset += nextXOffset

                nextXOffset = subtree.streamImmediateElements().mapToInt { (_, value) ->
                    value.size.x
                }.max().orElse(0) + X_GAP
            },
            { key, value ->
                val pos = startPos.add(xOffset, 0, zOffset)
                StructureDev.LOGGER.info(
                    "Placing $key at [${pos.x}, ${pos.y}, ${pos.z}] with size [${value.size.x}x${value.size.y}x${value.size.z}]"
                )
                val structureBlockPos = pos.add(-1, -1, -1)
                world.setBlockState(
                    structureBlockPos,
                    Blocks.STRUCTURE_BLOCK.defaultState.with(StructureBlock.MODE, StructureBlockMode.SAVE)
                )
                (world.getBlockEntity(structureBlockPos) as? StructureBlockBlockEntity)?.let {
                    it.templateName = key
                    it.offset = BlockPos(1, 1, 1)
                    it.size = value.size

                    it.place(
                        world,
                        true,
                        value
                    )
                }

                BlockPos.iterate(
                    pos,
                    pos.add(value.size.x - 1, value.size.y - 1, value.size.z - 1)
                ).forEach {
                    world.setBlockState(it, Blocks.STRUCTURE_VOID.defaultState)
                }

                zOffset += value.size.z + Z_GAP
            }
        )
    }
}