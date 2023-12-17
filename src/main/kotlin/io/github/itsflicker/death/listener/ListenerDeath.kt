package io.github.itsflicker.death.listener

import io.github.itsflicker.death.ItsDeath
import io.github.itsflicker.death.KetherHandler
import io.github.itsflicker.death.core.DeathMessageManager
import io.github.itsflicker.death.data.DeathData
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.killer
import kotlin.math.roundToInt

object ListenerDeath {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onDeath(e: PlayerDeathEvent) {
        val player = e.entity
        val world = player.world.name
        val worldConf = ItsDeath.conf.getConfigurationSection("worlds.$world") ?: return

        KetherHandler.eval(ItsDeath.conf.getStringList("events.pre-process"), player)

        // 传递死前数据
        DeathData.foodLevel[player.uniqueId] = player.foodLevel
        DeathData.potionEffects[player.uniqueId] = player.activePotionEffects

        // 消息处理
        e.deathMessage = null
        val cause = player.lastDamageCause
        val message = if (cause is EntityDamageByEntityEvent) {
            DeathMessageManager.sendDeathMessage(player, e.killer ?: cause.damager)
        } else {
            DeathMessageManager.sendDeathMessage(player, cause?.cause ?: EntityDamageEvent.DamageCause.KILL)
        }

        // 经验处理
        e.keepLevel = false
        val expDrop = worldConf.getDouble("exp-drop")
        if (expDrop >= 0) {
            e.droppedExp = (player.exp * expDrop).roundToInt()
        }

        // 物品处理
        e.keepInventory = true


    }

}