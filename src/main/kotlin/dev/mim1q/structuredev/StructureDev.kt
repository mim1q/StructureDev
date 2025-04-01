package dev.mim1q.structuredev

import dev.mim1q.structuredev.command.registerGenerateTemplatesCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object StructureDev : ModInitializer {
    const val ID: String = "structuredev"
    val LOGGER: Logger = LoggerFactory.getLogger(ID)

    override fun onInitialize() {
        LOGGER.info("Initializing StructureDev")

        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.registerGenerateTemplatesCommand()
        })
    }

    fun id(path: String): Identifier = Identifier(ID, path)
}