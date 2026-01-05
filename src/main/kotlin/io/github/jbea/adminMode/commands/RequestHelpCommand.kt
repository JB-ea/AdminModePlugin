package io.github.jbea.adminMode.commands

import io.github.jbea.adminMode.AdminMode
import io.github.jbea.adminMode.AdminModes
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class RequestHelpCommand : BasicCommand {
    override fun execute(source: CommandSourceStack, args: Array<String>) {
        val executor: Entity = source.executor ?: return

        if(executor is Player && executor.isOnline) {

            val receivers: List<Player> = AdminMode.Instance.server.onlinePlayers.filter { (AdminModes.getPDCVal(it) != AdminModes.Modes.NONE || it.hasPermission("${AdminMode.PERMISSION_NAMESPACE}.HelpChat.See")) }
            val audience: Audience = Audience.audience(receivers)

            var visibleAdmins: Int = 0;
            for (player in receivers) if(AdminModes.getPDCVal(player) != AdminModes.Modes.VANISH) visibleAdmins++

            if(visibleAdmins == 0) AdminMode.sendMessage(executor,"There are no Admins online but your Help Request has still been sent")
            else AdminMode.sendMessage(executor,"Your Help Request has been sent")

            val user: String = executor.name;
            var message: String = ""
            args.forEach { message += "$it " }

            val messageComments: Component = MiniMessage.miniMessage()
                .deserialize("<click:run_command:'tp $user'><hover:show_text:'Teleport To Player'><blue><b><red>$user</red> asked for Help ${ if(message.isNotEmpty()) "»" else ""}</b></blue> $message")

            audience.sendMessage(messageComments)
        }
    }

    override fun canUse(sender: CommandSender): Boolean {
        val permission: String? = this.permission()
        return permission == null || sender.hasPermission(permission)
    }

    override fun permission(): String? {
        return "${AdminMode.PERMISSION_NAMESPACE}.HelpChat.Message"
    }
}