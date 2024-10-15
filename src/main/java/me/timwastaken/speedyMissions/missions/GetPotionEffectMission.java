package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.List;

public class GetPotionEffectMission extends TickMission {
    private final PotionEffectType potionEffectType;

    public GetPotionEffectMission(GameManager gameManager, PotionEffectType potionEffectType) {
        super(gameManager);
        this.potionEffectType = potionEffectType;
    }

    @Override
    public void startMission() {
        this.startTask(() -> {
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                if (p.hasPotionEffect(this.potionEffectType)) {
                    this.playerFinishCallback.accept(List.of(p));
                }
            }
        });
    }

    @Override
    public String getMissionDescription() {
        return String.format(
                "Get potion effect: %s",
                Notifications.formatEnumName(this.potionEffectType.getKey().getKey())
        );
    }

    @Override
    public long getGameTickDuration() {
        return GameManager.MINUTES * 5;
    }
}
