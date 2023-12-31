package io.github.itsflicker.death.listener

import io.github.itsflicker.death.ItsDeath
import io.github.itsflicker.death.KetherHandler
import io.github.itsflicker.death.core.DeathChestManager
import io.github.itsflicker.death.core.DeathMessageManager
import io.github.itsflicker.death.data.DeathData
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.common5.util.parseMillis
import taboolib.expansion.geek.getTotalExperiences
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer
import taboolib.platform.util.killer
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

object ListenerDeath {

    @ConfigNode(value = "events.pre-process", bind = "config.yml")
    val eventPreProcess = ConfigNodeTransfer<Any, List<String>> { asList() }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onDeath(e: PlayerDeathEvent) {
        val player = e.entity
        val world = player.world.name
        val worldConf = ItsDeath.conf.getConfigurationSection("worlds.$world") ?: return

        val result = KetherHandler.eval(eventPreProcess.get(), player).runCatching {
            get(200, TimeUnit.MILLISECONDS)
        }.getOrDefault(false)

        // 传递死前数据
        DeathData.foodLevel[player.uniqueId] = player.foodLevel
        DeathData.potionEffects[player.uniqueId] = player.activePotionEffects

        // 消息处理
        e.deathMessage = null
        val cause = player.lastDamageCause as? EntityDamageByEntityEvent
        val message = DeathMessageManager.sendDeathMessage(player, e.killer ?: cause?.damager)

        // 不掉落条件
        if (result.cbool) {
            e.keepLevel = true
            e.keepInventory = true
            e.setShouldDropExperience(false)
            e.drops.clear()
            return
        }

        // 经验处理
        val expDrop = worldConf.getDouble("exp-drop")
        if (expDrop == 0.toDouble()) {
            e.keepLevel = true
            e.setShouldDropExperience(false)
        }
        val exp = if (expDrop > 0) {
            e.setShouldDropExperience(false)
            (player.getTotalExperiences() * expDrop).roundToInt()
        } else {
            0
        }

        // 物品处理
        val drops = when (worldConf["item-drop"]?.toString()?.lowercase()) {
            "all" -> {
                e.keepInventory = false
                ArrayList(e.drops).also { e.drops.clear() }
            }
            "keep" -> {
                e.keepInventory = true
                e.drops.clear()
                emptyList()
            }
            else -> emptyList()
        }
        DeathChestManager.createDeathChest(
            player,
            message,
            drops,
            exp,
            worldConf.getString("expire", "3h")!!.parseMillis()
        )
    }

}