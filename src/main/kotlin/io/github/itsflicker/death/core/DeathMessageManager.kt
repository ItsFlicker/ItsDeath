package io.github.itsflicker.death.core

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import taboolib.module.configuration.ConfigNode

object DeathMessageManager {

    @ConfigNode(value = "unknown", bind = "messages.yml")
    var unknownMessages: List<String> = emptyList()
        private set

    @ConfigNode(value = "cause", bind = "messages.yml")
    var messageByCause: Map<String, List<String>> = emptyMap()
        private set

    @ConfigNode(value = "entity", bind = "messages.yml")
    var messageByEntity: Map<String, List<String>> = emptyMap()
        private set

    fun sendDeathMessage(player: Player, cause: DamageCause): String {

    }

    fun sendDeathMessage(player: Player, entity: Entity): String {

    }
}