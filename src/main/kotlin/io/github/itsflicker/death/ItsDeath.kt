package io.github.itsflicker.death

import taboolib.common.io.runningClassesWithoutLibrary
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.expansion.ioc.IOCReader
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ItsDeath : Plugin() {

    @Config
    lateinit var conf: Configuration
        private set

    @Config(value = "messages.yml")
    lateinit var messages: Configuration
        private set

    override fun onEnable() {
        IOCReader.readRegister(runningClassesWithoutLibrary)
    }
}