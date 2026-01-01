package io.github.jbea.adminMode

import io.github.jbea.adminMode.AdminModes
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent?) {
        if(event?.player != null) {
            val player = event.player

            if(player.hasPermission("${AdminMode.PERMISSION_NAMESPACE}.Admin.JoinMode")) AdminModes.join(player, event)

            AdminModes.updateVanish(player)
        }
    }
}