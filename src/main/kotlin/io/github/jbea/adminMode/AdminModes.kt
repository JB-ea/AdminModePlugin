package io.github.jbea.adminMode

import io.github.jbea.adminMode.AdminMode.Companion.NAMESPACE
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object AdminModes {
    enum class Modes { NONE, ADMIN, VANISH }

    fun setMode(player: Player, mode: Modes) {
        val oldMode: Modes = getPDCVal(player)
        val newMode = if(oldMode == mode) Modes.NONE else mode
        setPDCVal(player, newMode)

        when(newMode) {
            Modes.NONE -> {
                player.gameMode = GameMode.SURVIVAL
                vanish(player, false)
                if(oldMode != Modes.NONE) swapInventory(player)
            }
            Modes.ADMIN -> {
                player.gameMode = GameMode.CREATIVE
                vanish(player, false)
                if(oldMode != Modes.VANISH) swapInventory(player)
            }
            Modes.VANISH -> {
                player.gameMode = GameMode.SPECTATOR
                vanish(player, true)
                if(oldMode != Modes.ADMIN) swapInventory(player)

            }
        }
    }

    private fun vanish(player: Player, enable: Boolean = true) {
        val container: PersistentDataContainer = player.persistentDataContainer;

        if (enable) {
            // hide player
            AdminMode.Instance.server.onlinePlayers.forEach { if (getPDCVal(it) == Modes.NONE) it.hidePlayer(AdminMode.Instance, player) }
        } else {
            // show player
            AdminMode.Instance.server.onlinePlayers.forEach { it.showPlayer(AdminMode.Instance, player) }
        }
    }

    private fun swapInventory(player: Player) {
        // save player data
        val container: PersistentDataContainer = player.persistentDataContainer;
        val inventory: Inventory = player.inventory
        val contents: MutableList<ItemStack> = inventory.contents.map { it ?: ItemStack(Material.AIR) }.toMutableList()

        val savedInventoryContents: List<ItemStack?>? = container.get(NamespacedKey(NAMESPACE, "inventory"), PersistentDataType.LIST.listTypeFrom(ItemStackDataType.INSTANCE))

        // apply data
        if(savedInventoryContents != null) inventory.contents = savedInventoryContents.toTypedArray()
        else inventory.contents = emptyArray<ItemStack>()

        container.set(NamespacedKey(NAMESPACE, "inventory"), PersistentDataType.LIST.listTypeFrom(ItemStackDataType.INSTANCE), contents)
    }

    fun getPDCVal(player: Player): Modes =
        player.persistentDataContainer.get(NamespacedKey(NAMESPACE, "admin_mode"), AdminModesDataType.INSTANCE) ?: Modes.NONE

    private fun setPDCVal(player: Player, value: Modes) =
        player.persistentDataContainer.set(NamespacedKey(NAMESPACE, "admin_mode"), AdminModesDataType.INSTANCE, value)

//    private fun updatePDCVal(player: Player, value: Modes): Modes {
//        val old = getPDCVal(player)
//        setPDCVal(player, value)
//        return old
//    }
}