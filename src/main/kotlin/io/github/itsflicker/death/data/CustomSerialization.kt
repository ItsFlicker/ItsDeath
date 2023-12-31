package io.github.itsflicker.death.data

import com.comphenix.protocol.utility.StreamSerializer
import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.expansion.ioc.serialization.SerializationManager
import taboolib.expansion.ioc.serialization.impl.SerializationFunctionGson

val gson: Gson = GsonBuilder().apply {
    excludeFieldsWithoutExposeAnnotation()
    registerTypeAdapter(
        ItemStack::class.java,
        JsonSerializer<ItemStack> { src, _, _ ->
            JsonPrimitive(StreamSerializer.getDefault().serializeItemStack(src))
        }
    )
    registerTypeAdapter(
        ItemStack::class.java,
        JsonDeserializer { json, _, _ ->
            StreamSerializer.getDefault().deserializeItemStack(json.asString)
        }
    )
    registerTypeAdapter(
        Location::class.java,
        JsonSerializer<Location> { a, _, _ ->
            JsonPrimitive(fromLocation(a))
        }
    )
    registerTypeAdapter(
        Location::class.java,
        JsonDeserializer { a, _, _ ->
            toLocation(a.asString)
        }
    )
}.create()

fun toLocation(source: String): Location {
    return source.replace("__", ".").split(",").run {
        Location(
            Bukkit.getWorld(get(0)),
            getOrElse(1) { "0" }.toDouble(),
            getOrElse(2) { "0" }.toDouble(),
            getOrElse(3) { "0" }.toDouble()
        )
    }
}

fun fromLocation(location: Location): String {
    return "${location.world?.name},${location.x},${location.y},${location.z}".replace(".", "__")
}

@Awake(LifeCycle.INIT)
fun init() {
    (SerializationManager.function["Gson"] as SerializationFunctionGson).gson = gson
}