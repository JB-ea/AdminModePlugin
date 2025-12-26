package io.github.jbea.adminMode.commands

import io.github.jbea.adminMode.AdminMode
import io.github.jbea.adminMode.AdminModes
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class AdminJoinModeCommand : BasicCommand {
    override fun execute(source: CommandSourceStack, args: Array<String>) {
        val executor: Entity = source.executor ?: return

        if(executor is Player && executor.isOnline)
            AdminModes.setJoinMode(executor, AdminModes.Modes.valueOf(args[0].uppercase()))
    }

    override fun suggest(source: CommandSourceStack, args: Array<String>): MutableCollection<String> {
        if (args.size > 1) return mutableSetOf()
        return AdminModes.Modes.entries.map { it.name.lowercase() }.toMutableSet()
    }

    override fun canUse(sender: CommandSender): Boolean {
        val permission: String? = this.permission()
        return permission == null || sender.hasPermission(permission)
    }

    override fun permission(): String? {
        return "${AdminMode.PERMISSION_NAMESPACE}.Admin.JoinMode"
    }
}