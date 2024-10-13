package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObtainItemMission extends TickMission {
    private final Material itemMaterial;

    public ObtainItemMission(GameManager gameManager, Material itemMaterial) {
        super(gameManager);
        this.itemMaterial = itemMaterial;
    }

    @Override
    public void startMission() {
        this.startTask(() -> {
            List<Player> finishingPlayers = new ArrayList<>();
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                if (!p.getInventory().contains(itemMaterial)) continue;
                finishingPlayers.add(p);
            }
            if (!finishingPlayers.isEmpty()) this.playerFinishCallback.accept(finishingPlayers);
        });
    }

    @Override
    public void stopMission() {
        super.stopMission();
    }

    @Override
    public String getMissionDescription() {
        return String.format("Obtain item: %s", Notifications.getItemName(this.itemMaterial));
    }

    @Override
    public long getGameTickDuration() {
        return 2 * GameManager.MINUTES;
    }
}
