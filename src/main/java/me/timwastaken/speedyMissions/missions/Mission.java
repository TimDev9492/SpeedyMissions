package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Abstract class representing a speedy mission.
 */
public abstract class Mission {
    protected GameManager gameManager;
    protected Consumer<Collection<Player>> playerFinishCallback;

    public Mission(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public abstract void startMission();
    public abstract void stopMission();
    public void onPlayerFinishMission(Consumer<Collection<Player>> callback) {
        this.playerFinishCallback = callback;
    }
    public abstract String getMissionDescription();
    public abstract long getGameTickDuration();
}
