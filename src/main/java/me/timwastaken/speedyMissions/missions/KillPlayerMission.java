package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.List;

public class KillPlayerMission extends TickEventMission {
    public KillPlayerMission(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void startMission() {
        super.startMission();
        this.startTask(() -> {
            for (Iterator<Player> it = gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player active = it.next();
                active.addPotionEffect(new PotionEffect(
                        PotionEffectType.GLOWING,
                        20,
                        0,
                        true,
                        false
                ));
                // find nearest player
                Player closest = null;
                double closestDistanceSquared = Double.MAX_VALUE;
                for (Iterator<Player> iter = gameManager.getActivePlayerIterator(); iter.hasNext(); ) {
                    Player other = iter.next();
                    if (other.equals(active)) continue;
                    double distSquared = active.getLocation().distanceSquared(other.getLocation());
                    if (distSquared < closestDistanceSquared) {
                        closestDistanceSquared = distSquared;
                        closest = other;
                    }
                }
                active.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                        Notifications.getNearestPlayerActionbarTitle(closest, Math.sqrt(closestDistanceSquared))
                ));
            }
        });
    }

    @Override
    public void stopMission() {
        super.stopMission();
    }

    @Override
    public String getMissionDescription() {
        return "Kill a player";
    }

    @Override
    public long getGameTickDuration() {
        return 3 * GameManager.MINUTES;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            this.playerFinishCallback.accept(List.of(event.getEntity().getKiller()));
        }
    }
}
