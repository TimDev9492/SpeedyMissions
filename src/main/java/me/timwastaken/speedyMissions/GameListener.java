package me.timwastaken.speedyMissions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!GameManager.getInstance().getGameRunning()) return;
        Player p = event.getEntity();
        if (!GameManager.getInstance().isPlaying(p)) return;
        if (!GameManager.getInstance().getGameState().getActiveMission().hasDeathPenalty(p)) return;
        p.sendMessage(Notifications.getDeathNotification());
        p.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
        int score = GameManager.getInstance().getGameState().getPlayerScore(p);
        if (score > 0) GameManager.getInstance().getGameState().setPlayerScore(p, score - 1);
    }
}
