package me.timwastaken.speedyMissions;

import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedyMissions extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("Plugin loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Unloading Plugin...");
    }
}
