package me.timwastaken.speedyMissions;

import dev.jorel.commandapi.CommandAPI;
import me.timwastaken.speedyMissions.commands.PointsCommand;
import me.timwastaken.speedyMissions.commands.SkipCommand;
import me.timwastaken.speedyMissions.commands.StartGameCommand;
import me.timwastaken.speedyMissions.commands.StopGameCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public final class SpeedyMissions extends JavaPlugin {
    private static SpeedyMissions self;

    @Override
    public void onEnable() {
        // Plugin startup logic
        self = this;

        // register plugin commands
        CommandAPI.registerCommand(StartGameCommand.class);
        CommandAPI.registerCommand(StopGameCommand.class);
        CommandAPI.registerCommand(SkipCommand.class);
        CommandAPI.registerCommand(PointsCommand.class);

        // register event listeners
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
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
