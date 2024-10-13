package me.timwastaken.speedyMissions.commands;

import dev.jorel.commandapi.annotations.*;
import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command("start")
public class StartGameCommand {
    @Default
    public static void startGame(CommandSender sender) {
        GameManager manager = GameManager.getInstance();
        if (manager.getGameRunning()) {
            sender.sendMessage(ChatColor.RED + "The game is already running!");
            return;
        }
        manager.setRegisteredPlayers(Bukkit.getOnlinePlayers());
        manager.startGame();
    }
}
