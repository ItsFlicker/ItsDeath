package io.github.itsflicker.death.data

import com.google.gson.annotations.Expose
import ink.ptms.adyeshach.core.Adyeshach
import io.github.itsflicker.death.ItsDeath
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import taboolib.expansion.ioc.annotation.Component
import taboolib.library.xseries.XSkull
import java.util.UUID

@Component(index = "id")
class DeathChest(
    @Expose val id: UUID,
    @Expose val owner: UUID,
    @Expose val location: Location,
    @Expose val deathMessage: String,
    @Expose val items: List<ItemStack>
) {
    val content = ItsDeath.conf.getStringList("death-chest.content").map {
        if (it.equals("{head}", ignoreCase = true)) {
            return@map XSkull.getSkull(owner)
        }
        else it.replace("{message}", deathMessage)
    }

    val entity = Adyeshach.api().getHologramHandler().createHologram(location, content)
}