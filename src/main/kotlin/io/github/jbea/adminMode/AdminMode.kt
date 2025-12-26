package io.github.jbea.adminMode

import org.bukkit.plugin.java.JavaPlugin

class AdminMode : JavaPlugin() {
    companion object {
        lateinit var Instance: AdminMode;
        const val NAMESPACE: String = "admin_mode"
        const val PERMISSION_NAMESPACE: String = "AdminMode"
    }

    fun log(message: String) = logger.info(message)

    override fun onEnable() {
        // Plugin startup logic
        Instance = this

        log("Plugin enabled")

        registerCommand("admin", AdminModeCommand())
        registerCommand("vanish", VanishCommand())
        registerCommand("adminChat", AdminChatCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
