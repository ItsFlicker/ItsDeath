package io.github.itsflicker.death.nms

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

abstract class NMS {

    abstract fun getDeathMessageKey(player: Player): String

    abstract fun getDeathMessageArgs(player: Player): List<Component>

}