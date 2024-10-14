package me.timwastaken.speedyMissions.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command("skip")
public class SkipCommand extends InGameCommand {
    @Default
    public static void skipMission(CommandSender sender) {
        GameManager manager = GameManager.getInstance();
        if (!checkGameState(sender)) return;
        manager.skipMission();
    }
}
