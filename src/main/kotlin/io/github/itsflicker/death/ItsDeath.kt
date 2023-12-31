package io.github.itsflicker.death

import io.github.itsflicker.death.core.DeathChestManager
import io.github.itsflicker.death.data.DeathChest
import io.github.itsflicker.death.nms.NMS
import taboolib.common.io.runningClassesWithoutLibrary
import taboolib.common.platform.Plugin
import taboolib.expansion.ioc.IOCReader
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.nms.nmsProxy

object ItsDeath : Plugin() {

    @Config
    lateinit var conf: Configuration
        private set

    @Config(value = "messages.yml")
    lateinit var messages: Configuration
        private set

    override fun onEnable() {
        nmsProxy<NMS>()
        IOCReader.readRegister(runningClassesWithoutLibrary)
    }

    override fun onActive() {
        DeathChestManager.deathChests.forEachIOC {
            val chest = second as DeathChest
            chest.init()
        }
    }
}