package io.github.itsflicker.death.core

import io.github.itsflicker.death.ItsDeath
import io.github.itsflicker.death.nms.NMS
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.util.asList
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.chat.colored
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer
import taboolib.module.configuration.util.mapValue
import taboolib.module.lang.sendInfoMessage
import taboolib.module.nms.getI18nName
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.onlinePlayers

object DeathMessageManager {

    @ConfigNode(value = "unknown", bind = "messages.yml")
    var unknownMessages: List<String> = emptyList()
        private set

    @ConfigNode(value = "cause", bind = "messages.yml")
    val messageByCause = ConfigNodeTransfer<ConfigurationSection, Map<String, List<String>>> {
        mapValue { it.asList() }
    }

    @ConfigNode(value = "entity", bind = "messages.yml")
    val messageByEntity = ConfigNodeTransfer<ConfigurationSection, Map<String, List<String>>> {
        mapValue { it.asList() }
    }

    val entityKeys = listOf("death_attack_mob")

    fun sendDeathMessage(player: Player, entity: Entity?): String {
        val key = nmsProxy<NMS>().getDeathMessageKey(player).replace(".", "_")
        val args = nmsProxy<NMS>().getDeathMessageArgs(player)
        if (key in entityKeys && entity != null) {
            val message = messageByEntity.get()[entity.type.name.lowercase()]
            if (message == null) {
                if (ItsDeath.conf.getBoolean("general.print-unset-entity")) {
                    console().sendInfoMessage("Entity ${entity.type.name.lowercase()} has not been set.")
                }
            } else {
                val component = Component.translatable(message.random().colored(), NamedTextColor.WHITE, args)
                onlinePlayers.forEach {
                    it.sendMessage(component)
                }
                console().sendMessage(LegacyComponentSerializer.legacySection().serialize(component))
                return GsonComponentSerializer.gson().serialize(component)
            }
        }
        var message = messageByCause.get()[key]
        if (message == null) {
            if (ItsDeath.conf.getBoolean("general.print-unset-cause")) {
                console().sendInfoMessage("Cause $key has not been set.")
            }
            message = unknownMessages
        }
        val component = Component.translatable(message.random().colored(), NamedTextColor.WHITE, args)
        onlinePlayers.forEach {
            it.sendMessage(component)
        }
        console().sendMessage(LegacyComponentSerializer.legacySection().serialize(component))
        return GsonComponentSerializer.gson().serialize(component)
    }

    private fun Entity.getShowName(player: Player? = null): String {
        return customName ?: getI18nName(player)
    }
}