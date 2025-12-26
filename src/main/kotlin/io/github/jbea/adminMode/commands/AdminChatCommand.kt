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

class AdminChatCommand : BasicCommand {
    override fun execute(source: CommandSourceStack, args: Array<String>) {
        if(args.isEmpty()) return
        val executor: Entity = source.executor ?: return

        if(executor is Player && executor.isOnline) {
            if(AdminModes.getPDCVal(executor) == AdminModes.Modes.NONE) {
                executor.sendMessage("You are not in admin mode.")
                return
            }

            val receivers: List<Player> = AdminMode.Instance.server.onlinePlayers.filter { (AdminModes.getPDCVal(it) != AdminModes.Modes.NONE || executor.hasPermission("${AdminMode.PERMISSION_NAMESPACE}.seeChat")) }
            val audience: Audience = Audience.audience(receivers)

            val user: String = executor.name;
            var message: String = ""
            args.forEach { message += "$it " }

            val messageComments: Component = MiniMessage.miniMessage().deserialize("<green><b>Admin Chat: $user »</b></green> $message")

            audience.sendMessage(messageComments)

        }
    }

    override fun canUse(sender: CommandSender): Boolean {
        val permission: String? = this.permission()
        return permission == null || sender.hasPermission(permission)
    }

    override fun permission(): String? {
        return "${AdminMode.PERMISSION_NAMESPACE}.Admin.Chat"
    }
}