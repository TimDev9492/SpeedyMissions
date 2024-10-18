package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class KillYourselfMission extends EventMission {
    public KillYourselfMission(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public String getMissionDescription() {
        return "Kill yourself";
    }

    @Override
    public long getGameTickDuration() {
        return GameManager.SECONDS * 30;
    }

    @Override
    public boolean hasDeathPenalty(Player p) {
        return gameManager.getGameState().hasFinished(p);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (!gameManager.isPlaying(p)) return;
        this.playerFinishCallback.accept(List.of(p));
    }
}
