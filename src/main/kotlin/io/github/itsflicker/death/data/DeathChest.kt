package io.github.itsflicker.death.data

import com.google.gson.annotations.Expose
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.AdyeshachHologram
import ink.ptms.adyeshach.core.bukkit.BukkitPose
import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.entity.type.AdyHuman
import io.github.itsflicker.death.KetherHandler
import io.github.itsflicker.death.core.DeathChestManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.expansion.geek.giveTotalExperiences
import taboolib.expansion.ioc.annotation.Component
import taboolib.platform.util.giveItem
import taboolib.platform.util.sendInfoMessage
import java.util.*

@Component(index = "id")
class DeathChest(
    @Expose val id: UUID,
    @Expose val owner: UUID,
    @Expose val name: String,
    @Expose val location: Location,
    @Expose val deathMessage: String,
    @Expose val items: List<ItemStack>,
    @Expose val exp: Int,
    @Expose val expireTime: Long
) {
    lateinit var entity: AdyHuman
        private set
    lateinit var hologram: AdyeshachHologram
        private set

    fun init() {
        entity = Adyeshach.api().getPublicEntityManager().create(EntityTypes.PLAYER, location.clone().also { it.y -= 0.2 }) {
            it as AdyHuman
            it.isNitwit = true
            it.setName("")
            it.setTexture(name)
            it.setPose(BukkitPose.SWIMMING)
        } as AdyHuman
        hologram = Adyeshach.api().getHologramHandler().createHologram(location.clone().also { it.y += 0.4 }, listOf(deathMessage))
    }

    fun retrieve(player: Player) {
        if (items.isNotEmpty()) {
            player.giveItem(items)
        }
        if (exp > 0) {
            player.giveTotalExperiences(exp)
        }
        KetherHandler.eval(DeathChestManager.eventAfterRetrieve.get(), player)
        discard()
    }

    fun expire() {
        Bukkit.getPlayer(owner)?.sendInfoMessage("你的死亡盒过期了!")
        discard()
    }

    fun discard() {
        entity.remove()
        hologram.remove()
    }
}