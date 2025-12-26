package io.github.jbea.adminMode

import io.github.jbea.adminMode.commands.AdminChatCommand
import io.github.jbea.adminMode.commands.AdminJoinModeCommand
import io.github.jbea.adminMode.commands.AdminModeCommand
import io.github.jbea.adminMode.commands.VanishCommand
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AdminMode : JavaPlugin() {
    companion object {
        lateinit var Instance: AdminMode
        var LuckPermsPresent: Boolean = false
        lateinit var PluginConfig: YamlConfiguration
        const val NAMESPACE: String = "admin_mode"
        const val PERMISSION_NAMESPACE: String = "AdminMode"
    }

    fun log(message: String) = logger.info(message)

    override fun onEnable() {
        // Plugin startup logic
        Instance = this
        PluginConfig = loadConfig()

        // luck perms integration
        if(PluginConfig.getBoolean("luck-perms-integration.enabled")) {
            LuckPermsPresent = server.pluginManager.getPlugin("LuckPerms") != null
            log((if (LuckPermsPresent) "Found" else "Did NOT Find") + " Luck Perms")

            if(PluginConfig.getBoolean("luck-perms-integration.createRoles")) luckPermsSetUp()
        } else LuckPermsPresent = false

        // events
        server.pluginManager.registerEvents(JoinListener(), this)
        server.pluginManager.registerEvents(LeaveListener(), this)

        log("Plugin enabled")

        registerCommand("admin", AdminModeCommand())
        registerCommand("vanish", VanishCommand())
        registerCommand("adminChat", AdminChatCommand())
        registerCommand("joinMode", AdminJoinModeCommand())
    }

    fun loadConfig(): YamlConfiguration {
        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            saveResource("config.yml", true)
        }
        val config = YamlConfiguration.loadConfiguration(configFile)

        val pluginManager = Bukkit.getPluginManager()
        if (!config.getBoolean("enabled")) {
            pluginManager.disablePlugin(this)
        }
        return config
    }

    fun luckPermsSetUp() {
        val adminNoPerms: String = PluginConfig.getString("luck-perms-integration.adminRole") ?: "admin-no-perms"
        val adminWithPerms: String = PluginConfig.getString("luck-perms-integration.adminPermsRole") ?: "admin-with-perms"

        // No Perms Group
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp creategroup $adminNoPerms")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group $adminNoPerms permission set adminmode.admin true")

        // With Perms Group
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp creategroup $adminWithPerms")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group $adminWithPerms permission set * true")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
