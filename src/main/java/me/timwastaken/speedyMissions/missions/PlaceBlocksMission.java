package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaceBlocksMission extends TickEventMission {
    private final int blockCount;
    private final Map<UUID, Integer> blocksPlaced;

    public PlaceBlocksMission(GameManager gameManager, int blockCount) {
        super(gameManager);
        this.blockCount = blockCount;
        this.blocksPlaced = new ConcurrentHashMap<>();
    }

    @Override
    public void startMission() {
        super.startMission();
        this.startTask(() -> {
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                int placed = this.blocksPlaced.getOrDefault(p.getUniqueId(), 0);
                p.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent(
                                Notifications.getBlocksPlacedActionbarTitle(placed, this.blockCount)
                        )
                );
            }
        });
    }

    @Override
    public String getMissionDescription() {
        return String.format("Place %d blocks", this.blockCount);
    }

    @Override
    public long getGameTickDuration() {
        return this.blockCount * GameManager.SECONDS / 2;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();
        if (!gameManager.isPlaying(placer)) return;
        UUID playerUUID = placer.getUniqueId();
        int alreadyPlaced = this.blocksPlaced.getOrDefault(playerUUID, 0);
        if (alreadyPlaced >= this.blockCount) {
            this.playerFinishCallback.accept(List.of(placer));
            return;
        }
        this.blocksPlaced.put(playerUUID, alreadyPlaced + 1);
    }
}
