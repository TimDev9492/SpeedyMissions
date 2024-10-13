package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.SpeedyMissions;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * A mission that combines `TickMission` and `EventMission`.
 */
public abstract class TickEventMission extends TickMission implements Listener {
    public TickEventMission(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void startMission() {
        Bukkit.getPluginManager().registerEvents(this, SpeedyMissions.getInstance());
    }

    @Override
    public void stopMission() {
        super.stopMission();
        HandlerList.unregisterAll(this);
    }
}
