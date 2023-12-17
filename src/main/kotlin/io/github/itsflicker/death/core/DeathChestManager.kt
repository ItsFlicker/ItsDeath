package io.github.itsflicker.death.core

import io.github.itsflicker.death.data.DeathChest
import taboolib.expansion.ioc.linker.linkedIOCList

object DeathChestManager {

    val deathChests = linkedIOCList<DeathChest>()

    fun createDeathChest() {

    }

}