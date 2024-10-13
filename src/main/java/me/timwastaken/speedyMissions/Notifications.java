package me.timwastaken.speedyMissions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Notifications {
    private static final String PREFIX = String.format(
            "%s[%sSpeedyMissions%s] %s",
            ChatColor.GRAY,
            ChatColor.RED,
            ChatColor.GRAY,
            ChatColor.RESET
    );

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
        return Arrays.stream(mat.name().toLowerCase().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
