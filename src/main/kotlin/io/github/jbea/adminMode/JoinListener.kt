package io.github.jbea.adminMode

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent?) {
        if(event?.player != null) AdminMode.Instance.server.onlinePlayers.forEach {
            if(
                AdminModes.getPDCVal(it) == AdminModes.Modes.VANISH ||
                event.player.hasPermission("${AdminMode.PERMISSION_NAMESPACE}.See.Vanish")
            ) event.player.hidePlayer(AdminMode.Instance, it)
        }
    }
}