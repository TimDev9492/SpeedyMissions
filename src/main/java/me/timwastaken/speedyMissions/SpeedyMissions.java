package me.timwastaken.speedyMissions;

import dev.jorel.commandapi.CommandAPI;
import me.timwastaken.speedyMissions.commands.SkipCommand;
import me.timwastaken.speedyMissions.commands.StartGameCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedyMissions extends JavaPlugin {
    private static SpeedyMissions self;

    @Override
    public void onEnable() {
        // Plugin startup logic
        self = this;

        // register plugin commands
        CommandAPI.registerCommand(StartGameCommand.class);
        CommandAPI.registerCommand(SkipCommand.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Unloading Plugin...");
    }

    public static SpeedyMissions getInstance() {
        return self;
    }
}
