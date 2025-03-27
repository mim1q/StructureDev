package dev.mim1q.structuredev.generator

import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class TemplateGenerator(
    private val world: ServerWorld,
    private val startPos: BlockPos,
) {
    private var xOffset = 0
    private val allPools = world.registryManager.get(RegistryKeys.TEMPLATE_POOL).entrySet.associate { it ->
        it.key.toString() to it.value
    }
    private val allTemplates = world.structureTemplateManager.streamTemplates().toList().associate { it ->
        it.toString() to world.structureTemplateManager.getTemplateOrBlank(it)
    }

    fun generateTemplatesRecursively(prefix: String) {

    }


}