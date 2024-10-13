package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.SpeedyMissions;
import org.bukkit.Bukkit;

/**
 * A mission that runs a background routine every game tick.
 */
public abstract class TickMission extends Mission {
    private int taskId = -1;

    public TickMission(GameManager gameManager) {
        super(gameManager);
    }

    protected void startTask(Runnable code) {
        if (this.taskId != -1) throw new IllegalStateException(
                "Cannot start a new mission task. This mission already runs in the background!"
        );
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                SpeedyMissions.getInstance(),
                code,
                0L,
                1L
        );
    }

    @Override
    public void stopMission() {
        // stop the background task
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
