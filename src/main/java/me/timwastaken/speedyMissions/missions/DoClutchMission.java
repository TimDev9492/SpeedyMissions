package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.utils.ClutchType;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class DoClutchMission extends TickEventMission {
    private final ClutchType clutchType;
    private final double minFallingHeight;
    private final Map<UUID, Double> fallingHeights;
    private final Map<UUID, Object> clutchData;
    private final Map<UUID, Boolean> receivedFallDamage;

    public DoClutchMission(GameManager gameManager, ClutchType clutchType, double minFallingHeight) {
        super(gameManager);
        this.clutchType = clutchType;
        this.minFallingHeight = minFallingHeight;

        this.fallingHeights = new HashMap<>();
        this.clutchData = new HashMap<>();
        this.receivedFallDamage = new HashMap<>();
    }

    @Override
    public String getMissionDescription() {
        return String.format(
                "Do a %d block %s clutch",
                (int) Math.ceil(this.minFallingHeight),
                switch (this.clutchType) {
                    case WATER -> "water";
                    case BOAT -> "boat";
                }
        );
    }

    @Override
    public long getGameTickDuration() {
        return GameManager.MINUTES;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {
            if (!this.gameManager.isPlaying(p)) return;
            if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
            // player received fall damage -> clutch failed
            this.receivedFallDamage.put(p.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.gameManager.isPlaying(player)) return;
        if (!this.isOnGround(player)) {
            if (!this.isInFreeFall(player)) {
                this.fallingHeights.remove(player.getUniqueId());
                return;
            }
            if (!this.fallingHeights.containsKey(player.getUniqueId())) {
                this.fallingHeights.put(player.getUniqueId(), player.getLocation().getY());
                this.clutchData.remove(player.getUniqueId());
                this.receivedFallDamage.put(player.getUniqueId(), false);
            }
        } else {
            // player is on ground
            if (!this.fallingHeights.containsKey(player.getUniqueId())) return;
            // +1 because Player.isOnGround() is client-sided and therefore lags behind
            double fallDistance = this.fallingHeights.remove(player.getUniqueId()) - player.getLocation().getY() + 1;
            // check if the clutch was successful
            if (!this.receivedFallDamage.containsKey(player.getUniqueId())) throw new IllegalStateException(
                    "Can't tell if the player received fall damage!"
            );
            // player received fall damage
            if (this.receivedFallDamage.get(player.getUniqueId())) return;
            this.receivedFallDamage.remove(player.getUniqueId());
            if (!this.clutchData.containsKey(player.getUniqueId())) return;
            switch (this.clutchType) {
                case WATER -> {
                    Block waterBlockPlaced = (Block) this.clutchData.remove(player.getUniqueId());
                    if (waterBlockPlaced.getLocation().clone()
                            .add(0.5, 0, 0.5)
                            .distance(player.getLocation()) > 0.5 + 0.5 * player.getWidth()) return;
                }
                case BOAT -> {
                    Boat boatPlaced = (Boat) this.clutchData.remove(player.getUniqueId());
                    if (!boatPlaced.getPassengers().contains(player)) return;
                }
            }
            // clutch successful!
            if (fallDistance > this.minFallingHeight) this.playerFinishCallback.accept(List.of(player));
        }
    }

    @EventHandler
    public void onPlayerPlaceWater(PlayerBucketEmptyEvent event) {
        if (this.clutchType != ClutchType.WATER) return;
        Player p = event.getPlayer();
        if (!this.gameManager.isPlaying(p)) return;
        this.clutchData.put(p.getUniqueId(), event.getBlock());
    }

    @EventHandler
    public void onPlayerPlaceBoat(VehicleCreateEvent event) {
        System.out.println(event.getVehicle());
        // check in a 6-block radius for the nearest player and assign the boat to him
        if (event.getVehicle() instanceof Boat boat) {
            Player nearest = null;
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                if (nearest == null) {
                    nearest = p;
                    continue;
                }
                double closestDistSquared = boat.getLocation().distanceSquared(nearest.getEyeLocation());
                double currentDistSquared = boat.getLocation().distanceSquared(p.getEyeLocation());
                if (currentDistSquared < closestDistSquared) nearest = p;
            }
            if (nearest == null) return;
            this.clutchData.put(nearest.getUniqueId(), boat);
        }
    }

    private boolean isOnGround(Player p) {
        return p.isOnGround() || p.isInsideVehicle() || p.isSwimming();
    }

    private boolean isInFreeFall(Player p) {
        return !(p.isFlying() || p.isGliding());
    }
}
