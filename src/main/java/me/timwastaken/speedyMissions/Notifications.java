package me.timwastaken.speedyMissions;

import me.timwastaken.speedyMissions.missions.Mission;
import me.timwastaken.speedyMissions.missions.ObtainItemMission;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class Notifications {
    private static final String PREFIX = String.format(
            "%s[%sSpeedyMissions%s] %s",
            ChatColor.GRAY,
            ChatColor.RED,
            ChatColor.GRAY,
            ChatColor.RESET
    );

    public static String getScoreboardTitle() {
        return String.format(
                "%s%sSpeedyMissions",
                ChatColor.RED,
                ChatColor.BOLD
        );
    }

    public static String getPlayerScoreLine(Player p, int score, boolean finished) {
        return String.format(
                "%s%d %s%s %s",
                ChatColor.GRAY,
                score,
                ChatColor.LIGHT_PURPLE,
                p.getName(),
                finished ? ChatColor.GREEN + " ✔" : ChatColor.RED + " ✘"
        );
    }

    public static String getPlayerFinishedNotification(Player p, int delta) {
        return String.format(
                "%s%s%s%s%s %sfinished the mission %s[+%d]",
                PREFIX,
                ChatColor.BLUE,
                ChatColor.ITALIC,
                p.getName(),
                ChatColor.RESET,
                ChatColor.GRAY,
                ChatColor.GREEN,
                delta
        );
    }

    public static String getNearestPlayerActionbarTitle(Player nearest, double distance) {
        if (nearest == null) return String.format(
                "%sThere is no nearest player in your dimension!",
                ChatColor.GRAY
        );
        return String.format(
                "%sNearest player: %s%s%s%s   %sDistance: %s%.1fm",
                ChatColor.GRAY,
                ChatColor.BLUE,
                ChatColor.ITALIC,
                nearest.getName(),
                ChatColor.RESET,
                ChatColor.GRAY,
                ChatColor.GREEN,
                distance
        );
    }

    public static String getMissionTitle() {
        return String.format("%sYour mission:", ChatColor.DARK_PURPLE);
    }

    public static String getItemName(Material mat) {
        return formatEnumName(mat.name());
    }

    public static String formatEnumName(String enumName) {
        return Arrays.stream(enumName.toLowerCase().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static String getMissionDescriptionTitle() {
        return String.format("%s%sCurrent Objective:", ChatColor.GREEN, ChatColor.BOLD);
    }

    public static String getMissionDescription(Mission activeMission) {
        String description = activeMission.getMissionDescription();
        return String.format("%s%s", ChatColor.AQUA,
                description.length() > 38 ? description.substring(0, 35).trim() + "..." : description
        );
    }

    public static String getTimeRemainingTitle() {
        return String.format("%sTime remaining:", ChatColor.GOLD);
    }

    public static String getTimeRemainingLine(long millisRemaining) {
        String timeStr = DurationFormatUtils.formatDuration(Math.max(millisRemaining, 0), "mm:ss");
        return String.format("%s%s", ChatColor.GRAY, timeStr);
    }

    public static String getChatAnnouncement(String missionDescription) {
        return String.format(
                "%s%sNew mission: %s%s",
                PREFIX,
                ChatColor.YELLOW,
                ChatColor.AQUA,
                missionDescription
        );
    }

    public static String getCommandFailedBecauseGameNotRunningNotification() {
        return String.format(
                "%s%s%s",
                PREFIX,
                ChatColor.DARK_RED,
                "You can only run this command while the game is running!"
        );
    }

    public static String getPlayerHasNoScoreErrorNotification(Player p) {
        return String.format(
                "%s%sPlayer '%s' has no in-game score!",
                PREFIX,
                ChatColor.DARK_RED,
                p.getName()
        );
    }

    public static String getPlayerScoreModificationSuccessNotification(Player p, int newScore) {
        return String.format(
                "%s%sSuccessfully changed %s%s%s%s%s's score to %s%s%d",
                PREFIX,
                ChatColor.GREEN,
                ChatColor.BLUE,
                ChatColor.ITALIC,
                p.getName(),
                ChatColor.RESET,
                ChatColor.GREEN,
                ChatColor.GRAY,
                ChatColor.BOLD,
                newScore
        );
    }

    public static String getBlocksPlacedActionbarTitle(int placed, int blockCount) {
        return String.format(
                "%sBlocks placed: %d/%d",
                ChatColor.GRAY,
                placed,
                blockCount
        );
    }

    public static String getWrongDimensionActionbarNotification() {
        return String.format(
                "%sYou are in a different dimension than the one you started in!",
                ChatColor.DARK_GRAY
        );
    }

    public static String getCoverDistanceActionbarNotification(double distance) {
        return String.format(
                "%sDistance covered: %.1fm",
                ChatColor.GRAY,
                distance
        );
    }
}
