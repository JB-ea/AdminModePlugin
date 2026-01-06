package io.github.jbea.adminMode

import io.github.jbea.adminMode.commands.*
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.PrefixNode
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AdminMode : JavaPlugin() {
    companion object {
        lateinit var Instance: AdminMode
        lateinit var PluginConfig: YamlConfiguration
        const val NAMESPACE: String = "admin_mode"
        const val PERMISSION_NAMESPACE: String = "AdminMode"

        object LP {
            var Instance: LuckPerms? = null

            var adminGroup: String = PluginConfig.getString("luck-perms-integration.adminGroup") ?: "admin"
            var permsGroup: String = PluginConfig.getString("luck-perms-integration.permsGroup") ?: "perms"
        }
        fun sendMessage(player: Player, message: String) = player.sendMessage(MiniMessage.miniMessage().deserialize("[<red>AdminMode</red>] $message"))
    }

    fun log(message: String) = logger.info(message)

    override fun onEnable() {
        // Plugin startup logic
        Instance = this
        PluginConfig = loadConfig()

        // luck perms integration
        if(PluginConfig.getBoolean("luck-perms-integration.enabled")) luckPermsSetUp()

        // events
        server.pluginManager.registerEvents(JoinListener(), this)
        server.pluginManager.registerEvents(LeaveListener(), this)

        log("Plugin enabled")

        registerCommand("requesthelp", listOf("rh", "messageadmin", "ma"), RequestHelpCommand())

        registerCommand("admin", listOf("a"), AdminModeCommand())
        registerCommand("vanish", listOf("v"), VanishCommand())
        registerCommand("adminChat", listOf("ac"), AdminChatCommand())
        registerCommand("joinMode", listOf("jm"), AdminJoinModeCommand())
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
        val provider: RegisteredServiceProvider<LuckPerms?>? = Bukkit.getServicesManager().getRegistration<LuckPerms?>(LuckPerms::class.java)
        if (provider != null) LP.Instance = provider.getProvider()

        LP.Instance ?: return

        if(PluginConfig.getBoolean("luck-perms-integration.createGroups")) {
            // admin group
            if(LP.Instance!!.groupManager.getGroup(LP.adminGroup) == null) {
                log("Create Admin Group: ${LP.adminGroup}")
                LP.Instance!!.groupManager.modifyGroup(LP.adminGroup) {
                    it.data().add(Node.builder("adminmode").value(true).build())
                    it.data().add(Node.builder("adminmode.*").value(true).build())
                    it.data().add(PrefixNode.builder("&4[Admin]&r ", 1).build())
                }
            }

            // perms group
            if(LP.Instance!!.groupManager.getGroup(LP.permsGroup) == null) {
                log("Create Perms Group: ${LP.permsGroup}")
                LP.Instance!!.groupManager.modifyGroup(LP.permsGroup) {
                    it.data().add(Node.builder("*").value(true).build())
                }
            }
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
