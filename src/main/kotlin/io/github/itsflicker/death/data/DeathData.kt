package io.github.itsflicker.death.data

import org.bukkit.potion.PotionEffect
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object DeathData {

    val foodLevel = ConcurrentHashMap<UUID, Int>()
    val potionEffects = ConcurrentHashMap<UUID, Collection<PotionEffect>>()
}