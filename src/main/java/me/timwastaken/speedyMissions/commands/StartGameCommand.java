package me.timwastaken.speedyMissions.commands;

import dev.jorel.commandapi.annotations.*;
import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command("start")
public class StartGameCommand {
    @Default
    public static void startGame(Player p) {
        GameManager manager = GameManager.getInstance();
        if (manager.getGameRunning()) {
            p.sendMessage(ChatColor.RED + "The game is already running!");
            return;
        }
        manager.setRegisteredPlayers(Bukkit.getOnlinePlayers());
        manager.startGame(p.getWorld());
    }
}
