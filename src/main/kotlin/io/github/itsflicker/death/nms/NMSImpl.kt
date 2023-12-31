package io.github.itsflicker.death.nms

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.IChatMutableComponent
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.damagesource.CombatTracker
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.invokeMethod

class NMSImpl : NMS() {

    fun getCombatTracker(player: Player): CombatTracker {
        return (player as CraftPlayer).handle.invokeMethod<CombatTracker>("getCombatTracker")!!
    }

    override fun getDeathMessageKey(player: Player): String {
        val contents = getCombatTracker(player).deathMessage.contents as TranslatableContents
        return contents.key
    }

    override fun getDeathMessageArgs(player: Player): List<Component> {
        val contents = getCombatTracker(player).deathMessage.contents as TranslatableContents
        return contents.args.mapNotNull {
            if (it is IChatMutableComponent) {
//                if (it.style.hoverEvent?.action == ChatHoverable.EnumHoverAction.SHOW_ENTITY) {
//                    it.style = it.style.withHoverEvent(null)
//                }
                PaperAdventure.asAdventure(it)
            } else {
                null
            }
        }
    }

}