package me.timwastaken.speedyMissions.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.GameState;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.management.Notification;

@Command("points")
@Alias("score")
public class PointsCommand extends InGameCommand {
    @Default
    public static void points(CommandSender sender) {
        sender.sendMessage("--- Points help ---");
        sender.sendMessage("/points - Show this help");
        sender.sendMessage("/points set <player> <score> - Set a player's score");
        sender.sendMessage("/points add <player> <score> - Add points to a player's score");
    }

    @Subcommand("set")
    public static void setPoints(
            CommandSender sender,
            @AEntitySelectorArgument.OnePlayer Player player,
            @AIntegerArgument int score
    ) {
        if (!checkGameState(sender)) return;
        try {
            GameManager.getInstance().getGameState().setPlayerScore(player, score);
            sender.sendMessage(Notifications.getPlayerScoreModificationSuccessNotification(player, score));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Notifications.getPlayerHasNoScoreErrorNotification(player));
        }
    }

    @Subcommand("add")
    public static void addPoints(
            CommandSender sender,
            @AEntitySelectorArgument.OnePlayer Player player,
            @AIntegerArgument int score
    ) {
        if (!checkGameState(sender)) return;
        try {
            GameState state = GameManager.getInstance().getGameState();
            state.addPlayerScore(player, score);
            sender.sendMessage(Notifications.getPlayerScoreModificationSuccessNotification(player, state.getPlayerScore(player)));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Notifications.getPlayerHasNoScoreErrorNotification(player));
        }
    }
}
