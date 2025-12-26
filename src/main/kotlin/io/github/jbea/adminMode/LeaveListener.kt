package io.github.jbea.adminMode

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LeaveListener : Listener {

    @EventHandler
    fun onQuitJoin(event: PlayerQuitEvent?) {
        if(event?.player != null) {
            val player = event.player

            if (AdminModes.getPDCVal(player) == AdminModes.Modes.VANISH) event.quitMessage(Component.empty())
        }
    }
}