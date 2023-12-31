package io.github.itsflicker.death.core

import io.github.itsflicker.death.data.DeathChest
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.asList
import taboolib.expansion.ioc.linker.linkedIOCList
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.ConfigNodeTransfer
import java.util.*

object DeathChestManager {

    @ConfigNode(value = "events.after-retrieve", bind = "config.yml")
    val eventAfterRetrieve = ConfigNodeTransfer<Any, List<String>> { asList() }

    val deathChests = linkedIOCList<DeathChest>()

    fun createDeathChest(
        player: Player,
        message: String,
        drops: List<ItemStack>,
        exp: Int,
        expire: Long
    ) {
        val chest = DeathChest(
            UUID.randomUUID(),
            player.uniqueId,
            player.displayName,
            player.location,
            message,
            drops,
            exp,
            System.currentTimeMillis() + expire
        )
        chest.init()
        deathChests.add(chest)
    }

}