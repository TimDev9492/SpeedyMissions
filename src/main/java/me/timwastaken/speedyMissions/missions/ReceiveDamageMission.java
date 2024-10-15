package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class ReceiveDamageMission extends EventMission {
    private final EntityDamageEvent.DamageCause damageCause;

    public ReceiveDamageMission(GameManager gameManager, EntityDamageEvent.DamageCause damageCause) {
        super(gameManager);
        this.damageCause = damageCause;
    }

    @Override
    public String getMissionDescription() {
        return switch (this.damageCause) {
            case KILL -> "Unexpected KILL";
            case WORLD_BORDER -> "Unexpected WORLD_BORDER";
            case CONTACT -> "Receive damage from contacting a block";
            case ENTITY_ATTACK -> "Receive damage by getting attacked by and entity";
            case ENTITY_SWEEP_ATTACK -> "Unexpected ENTITY_SWEEP_ATTACK";
            case PROJECTILE -> "Receive damage from a projectile";
            case SUFFOCATION -> "Receive damage by getting put in a block";
            case FALL -> "Receive fall damage";
            case FIRE -> "Receive damage by getting exposed to fire";
            case FIRE_TICK -> "Receive damage from burning";
            case MELTING -> "Unexpected MELTING";
            case LAVA -> "Receive lava damage";
            case DROWNING -> "Receive damage from drowning";
            case BLOCK_EXPLOSION -> "Receive damage from a block explosion";
            case ENTITY_EXPLOSION -> "Receive damage from an entity explosion";
            case VOID -> "Receive damage from the void";
            case LIGHTNING -> "Receive damage by getting struck by lightning";
            case SUICIDE -> "Unexpected SUICIDE";
            case STARVATION -> "Receive starvation damage";
            case POISON -> "Receive damage through a potion effect";
            case MAGIC -> "Receive damage by getting hit by a damage potion or a spell";
            case WITHER -> "Receive wither damage";
            case FALLING_BLOCK -> "Receive damage from a falling anvil";
            case THORNS -> "Receive damage through the thorns enchantment";
            case DRAGON_BREATH -> "Receive damage by dragon breath";
            case CUSTOM -> "Unexpected CUSTOM";
            case FLY_INTO_WALL -> "Receive damage by flying into a wall";
            case HOT_FLOOR -> "Receive damage by standing on top of a magma block";
            case CAMPFIRE -> "Receive damage by standing on top of a campfire";
            case CRAMMING -> "Receive damage through entity cramming";
            case DRYOUT -> "Unexpected DRYOUT";
            case FREEZE -> "Receive freezing damage";
            case SONIC_BOOM -> "Receive damage through the warden's sonic boom attack";
        };
    }

    @Override
    public long getGameTickDuration() {
        return GameManager.MINUTES * 3;
    }

    @EventHandler
    public void onPlayerReceiveDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player damaged && this.gameManager.isPlaying(damaged)) {
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (cause == this.damageCause) {
                this.playerFinishCallback.accept(List.of(damaged));
            }
        }
    }
}
