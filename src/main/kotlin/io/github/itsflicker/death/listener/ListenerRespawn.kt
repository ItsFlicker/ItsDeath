package io.github.itsflicker.death.listener

import io.github.itsflicker.death.KetherHandler
import org.bukkit.event.player.PlayerRespawnEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.asList
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer

object ListenerRespawn {

    @ConfigNode(value = "events.after-respawn", bind = "config.yml")
    val eventAfterRespawn = ConfigNodeTransfer<Any, List<String>> { asList() }

    @SubscribeEvent
    fun onRespawn(e: PlayerRespawnEvent) {
        KetherHandler.eval(eventAfterRespawn.get(), e.player)
    }

}