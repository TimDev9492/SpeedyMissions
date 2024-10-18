package me.timwastaken.speedyMissions.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@Command("stop")
public class StopGameCommand extends InGameCommand {
    @Default
    public static void stopGame(CommandSender sender) {
        if (!checkGameState(sender)) return;
        GameManager.getInstance().stopGame();
        Bukkit.broadcastMessage(Notifications.getGameStoppedNotification());
    }
}
