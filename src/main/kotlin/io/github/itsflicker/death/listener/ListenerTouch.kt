package io.github.itsflicker.death.listener

import ink.ptms.adyeshach.core.entity.EntityInstance
import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import io.github.itsflicker.death.core.DeathChestManager
import io.github.itsflicker.death.data.DeathChest
import taboolib.common.platform.event.SubscribeEvent

object ListenerTouch {

    @SubscribeEvent
    fun onTouch(e: AdyeshachEntityInteractEvent) {
        val chest = getChest(e.entity) ?: return
        if (chest.owner == e.player.uniqueId) {
            chest.retrieve(e.player)
            DeathChestManager.deathChests.remove(chest)
        }
    }

    private fun getChest(clicked: EntityInstance): DeathChest? {
        val iterator = DeathChestManager.deathChests.iteratorIOC()
        if (clicked.entityType == EntityTypes.PLAYER) {
            while (iterator.hasNext()) {
                val chest = iterator.next() as DeathChest
                if (System.currentTimeMillis() > chest.expireTime) {
                    chest.expire()
                    iterator.remove()
                    continue
                }
                if (chest.entity == clicked) {
                    return chest
                }
            }
            return null
        } else {
            return null
        }
    }
}