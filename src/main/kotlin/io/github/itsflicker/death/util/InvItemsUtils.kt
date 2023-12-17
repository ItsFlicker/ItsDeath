package io.github.itsflicker.death.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.platform.util.isNotAir
import java.util.*

/**
 * @author Arasple
 */
object InvItemsUtils {
    /**
     * 将一些物品添加到一个容器中, 如果容器满了则掉落在指定坐标
     *
     * @param location   坐标
     * @param inventory  目标容器
     * @param itemStacks 添加的物品
     * @return 容器是否已满 / 是否有掉落
     */
    fun addToInventory(location: Location, inventory: PlayerInventory, itemStacks: Array<ItemStack>): Boolean {
        var drop = false
        if (location.world == null) {
            return false
        }
        for (item in itemStacks) {
            if (!isInvFull(inventory)) {
                addItemToInv(inventory, item)
            } else {
                location.world!!.dropItemNaturally(location, item)
                //老老实实写true不就行了 非要写bug - a39
                drop = true
            }
        }
        return drop
    }

    /**
     * 强制将一些物品添加到容器中, 若容量不足则一个都不操作
     *
     * @param inventory  目标容器
     * @param itemStacks 添加的物品
     * @return 是否成功
     */
    fun addToInventoryForce(inventory: PlayerInventory, itemStacks: Array<ItemStack>): Boolean {
        return if (getFreeSlot(inventory) < itemStacks.size) {
            false
        } else {
            addItemToInv(inventory, *itemStacks)
            true
        }
    }

    private fun addItemToInv(inventory: PlayerInventory, vararg itemStacks: ItemStack) {
        val armors: List<ItemStack> = ArrayList(inventory.addItem(*itemStacks).values)
        armors.forEach { itemStack ->
            val type = itemStack.type.name.lowercase(Locale.getDefault())
            if (type.endsWith("helmet")) {
                inventory.helmet = itemStack
            } else if (type.endsWith("chestplate")) {
                inventory.chestplate = itemStack
            } else if (type.endsWith("leggings")) {
                inventory.leggings = itemStack
            } else if (type.endsWith("boots")) {
                inventory.boots = itemStack
            } else if (inventory.itemInMainHand.type == Material.AIR) {
                inventory.setItemInMainHand(itemStack)
            } else if (inventory.itemInOffHand.type == Material.AIR) {
                inventory.setItemInOffHand(itemStack)
            }
        }
    }

    /**
     * 剔除一个 ItemStack[] 对象中的 null/空气
     *
     * @param itemStacks 剔除对象
     * @return 整理后的对象
     */
    fun skipEmpty(itemStacks: Array<ItemStack>): Array<ItemStack> {
        val items: MutableList<ItemStack> = ArrayList()
        for (stack in itemStacks) {
            if (stack.isNotAir()) {
                items.add(stack)
            }
        }
        return items.toTypedArray()
    }

    /**
     * 取得一个容器中剩余的空槽位
     *
     * @param inventory 目标容器
     * @return 是否已满
     */
    private fun getFreeSlot(inventory: PlayerInventory): Int {
        var free = 0
        for (item in inventory.contents) {
            if (item == null) {
                free++
            }
        }
        return free
    }

    /**
     * 判断一个容器是否槽位已满
     *
     * @param inventory 目标容器
     * @return 是否已满
     */
    private fun isInvFull(inventory: PlayerInventory): Boolean {
        return getFreeSlot(inventory) == 0
    }
}