package me.timwastaken.speedyMissions.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.UUID;

public class PlayerNameComparatorByUUID implements Comparator<UUID> {
    @Override
    public int compare(UUID uuid1, UUID uuid2) {
        Player p1 = Bukkit.getPlayer(uuid1);
        Player p2 = Bukkit.getPlayer(uuid2);

        if (p1 == null || p2 == null) return 0;
        return p1.getName().compareTo(p2.getName());
    }
}
