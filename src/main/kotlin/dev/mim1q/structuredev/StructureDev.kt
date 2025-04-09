package dev.mim1q.structuredev

import dev.mim1q.structuredev.command.registerGenerateTemplatesCommand
import dev.mim1q.structuredev.command.registerGiveChestsCommand
import dev.mim1q.structuredev.item.StructureDevItems
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object StructureDev : ModInitializer {
    private const val ID: String = "structuredev"
    val LOGGER: Logger = LoggerFactory.getLogger(ID)
    val ITEMS = StructureDevItems()

    override fun onInitialize() {
        LOGGER.info("Initializing StructureDev")

        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.registerGenerateTemplatesCommand()
            dispatcher.registerGiveChestsCommand()
        })
    }

    fun id(path: String): Identifier = Identifier(ID, path)
}