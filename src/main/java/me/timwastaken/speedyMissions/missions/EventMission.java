package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.SpeedyMissions;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * A mission that listens for in-game events
 */
public abstract class EventMission extends Mission implements Listener {
    public EventMission(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void startMission() {
        Bukkit.getPluginManager().registerEvents(this, SpeedyMissions.getInstance());
    }

    @Override
    public void stopMission() {
        HandlerList.unregisterAll(this);
    }
}
