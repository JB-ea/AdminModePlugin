package io.github.jbea.adminMode

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
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

        val luckPermsCommand: String = "lp user ${player.name} parent set"
        val adminNoPermsCommand: String = luckPermsCommand + (AdminMode.PluginConfig.getString("luck-perms-integration.adminRole") ?: "admin-no-perms")
        val adminWithPermsCommand: String = luckPermsCommand + (AdminMode.PluginConfig.getString("luck-perms-integration.adminPermsRole") ?: "admin-with-perms")

        when(newMode) {
            Modes.NONE -> {
                player.gameMode = GameMode.SURVIVAL
                vanish(player, false)
                if(oldMode != Modes.NONE) swapInventory(player)
                // luck perms integration
                if(AdminMode.LuckPermsPresent) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), adminNoPermsCommand)
            }
            Modes.ADMIN -> {
                player.gameMode = GameMode.CREATIVE
                vanish(player, false)
                if(oldMode != Modes.VANISH) swapInventory(player)
                // luck perms integration
                if(AdminMode.LuckPermsPresent) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), adminWithPermsCommand)
            }
            Modes.VANISH -> {
                player.gameMode = GameMode.SPECTATOR
                vanish(player, true)
                if(oldMode != Modes.ADMIN) swapInventory(player)
                // luck perms integration
                if(AdminMode.LuckPermsPresent) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), adminWithPermsCommand)
            }
        }
    }

    fun join(player: Player, event: PlayerJoinEvent?) {
        val joinMode = getJoinMode(player)
        if (event != null && joinMode == Modes.VANISH) event.joinMessage(Component.empty())
        if (joinMode != getPDCVal(player)) setMode(player, joinMode)
    }

    private fun vanish(player: Player, enable: Boolean = true) {
        if (enable) {
            // hide player
            AdminMode.Instance.server.onlinePlayers.forEach {
                if (
                    !player.hasPermission("${AdminMode.PERMISSION_NAMESPACE}.See.Vanish") ||
                    getPDCVal(it) == Modes.NONE
                ) it.hidePlayer(AdminMode.Instance, player)
            }
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

        val savedInventoryContents: List<ItemStack?>? = container.get(NamespacedKey(AdminMode.NAMESPACE, "inventory"), PersistentDataType.LIST.listTypeFrom(ItemStackDataType.INSTANCE))

        // apply data
        if(savedInventoryContents != null) inventory.contents = savedInventoryContents.toTypedArray()
        else inventory.contents = emptyArray<ItemStack>()

        container.set(NamespacedKey(AdminMode.NAMESPACE, "inventory"), PersistentDataType.LIST.listTypeFrom(ItemStackDataType.INSTANCE), contents)
    }

    fun getPDCVal(player: Player): Modes = player.persistentDataContainer.get(NamespacedKey(AdminMode.NAMESPACE, "admin_mode"), AdminModesDataType.INSTANCE) ?: Modes.NONE

    private fun setPDCVal(player: Player, value: Modes) = player.persistentDataContainer.set(NamespacedKey(AdminMode.NAMESPACE, "admin_mode"), AdminModesDataType.INSTANCE, value)

    fun getJoinMode(player: Player) = player.persistentDataContainer.get(NamespacedKey(AdminMode.NAMESPACE, "admin_join_mode"), AdminModesDataType.INSTANCE) ?: Modes.NONE

    fun setJoinMode(player: Player, value: Modes) = player.persistentDataContainer.set(NamespacedKey(AdminMode.NAMESPACE, "admin_join_mode"), AdminModesDataType.INSTANCE, value)

//    private fun updatePDCVal(player: Player, value: Modes): Modes {
//        val old = getPDCVal(player)
//        setPDCVal(player, value)
//        return old
//    }
}