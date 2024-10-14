package me.timwastaken.speedyMissions.commands;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * This class represents a command that can
 * only be run while the game is running.
 */
public class InGameCommand {
    /**
     * Check if the game is running. If not,
     * notify the player and abort.
     * @return true if the game is running, false otherwise
     */
    protected static boolean checkGameState(CommandSender sender) {
        if (GameManager.getInstance().getGameRunning()) return true;
        sender.sendMessage(Notifications.getCommandFailedBecauseGameNotRunningNotification());
        return false;
    }
}
