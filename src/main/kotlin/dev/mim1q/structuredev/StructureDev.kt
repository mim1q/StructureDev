package dev.mim1q.structuredev

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object StructureDev : ModInitializer {
    const val ID: String = "structuredev"
    val LOGGER: Logger = LoggerFactory.getLogger(ID)

    override fun onInitialize() {
        LOGGER.info("Initializing StructureDev")
    }

    fun id(path: String): Identifier = Identifier(ID, path)
}
