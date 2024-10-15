package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoverDistanceMission extends TickMission {
    private final int distance;
    private final Map<UUID, Location> playerOrigins;

    public CoverDistanceMission(GameManager gameManager, int distance) {
        super(gameManager);
        this.distance = distance;
        this.playerOrigins = new ConcurrentHashMap<>();
    }

    @Override
    public void startMission() {
        this.startTask(() -> {
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player player = it.next();
                if (!this.playerOrigins.containsKey(player.getUniqueId())) {
                    this.playerOrigins.put(player.getUniqueId(), player.getLocation().clone());
                }
                Location origin = this.playerOrigins.get(player.getUniqueId());
                Location current = player.getLocation();
                if (origin.getWorld() == null) throw new IllegalStateException("Origin world is null");
                if (!origin.getWorld().equals(current.getWorld())) {
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent(Notifications.getWrongDimensionActionbarNotification())
                    );
                    continue;
                }
                double distance = origin.distance(current);
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent(Notifications.getCoverDistanceActionbarNotification(distance))
                );
                if (distance >= this.distance) this.playerFinishCallback.accept(List.of(player));
            }
        });
    }

    @Override
    public String getMissionDescription() {
        return String.format("Cover a distance of %d blocks", this.distance);
    }

    @Override
    public long getGameTickDuration() {
        return this.distance * GameManager.SECONDS / 5;
    }
}
