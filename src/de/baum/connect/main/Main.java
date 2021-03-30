package de.baum.connect.main;

import de.baum.connect.commands.ConnectRequest;
import de.baum.connect.commands.ReloadCommand;
import de.baum.connect.files.Data;
import de.baum.connect.listener.Connect;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Main plugin;

    @Override
    public void onEnable() {
        getLogger().info("Fully loaded.");

        plugin = this;

        this.saveDefaultConfig();

        //Data.setup();
        //Data.save();
        getCommand("connectreload").setExecutor(new ReloadCommand());
        getCommand("4connect").setExecutor(new ConnectRequest());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Connect(), this);

    }

    public static Main getPlugin() {
        return plugin;
    }
}
