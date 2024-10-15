package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class KillMobMission extends EventMission {
    private final EntityType entityType;

    public KillMobMission(GameManager gameManager, EntityType entityType) {
        super(gameManager);
        this.entityType = entityType;
    }

    @Override
    public String getMissionDescription() {
        return String.format(
                "Kill a%s %s",
                "AEIOUaeiou".indexOf(this.entityType.name().charAt(0)) >= 0 ? "n" : "",
                Notifications.formatEnumName(this.entityType.name())
        );
    }

    @Override
    public long getGameTickDuration() {
        return GameManager.MINUTES * 4;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        if (event.getEntity().getType() == this.entityType) {
            Player killer = event.getEntity().getKiller();
            if (killer == null || !this.gameManager.isPlaying(killer)) return;
            this.playerFinishCallback.accept(List.of(killer));
        }
    }
}
