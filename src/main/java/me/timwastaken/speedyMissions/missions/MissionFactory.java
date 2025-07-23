package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.utils.ClutchType;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.WorldBorder;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class MissionFactory {
    private static final Random rnd = new Random();
    private static final int MIN_BLOCK_COUNT = 16;
    private static final int MAX_BLOCK_COUNT = 128;
    private static final int MIN_DISTANCE_100m = 3;
    private static final int MAX_DISTANCE_100m = 25;
    private static final int MIN_CLUTCH_HEIGHT_SQRT = (int) Math.sqrt(4);
    private static final int MAX_CLUTCH_HEIGHT_SQRT = (int) Math.sqrt(40);
    private static final long MILLIS_PER_HOUR = 3_600_000L;

    private static final Map<Class<?>, Integer> missionWeights = Map.of(
            ObtainItemMission.class, 8,
            KillPlayerMission.class, 1,
            PlaceBlocksMission.class, 2,
            ReceiveDamageMission.class, 2,
            CoverDistanceMission.class, 1,
            KillMobMission.class, 3,
            GetPotionEffectMission.class, 2,
            KillYourselfMission.class, 1,
            DoClutchMission.class, 3
    );

    private static final Map<Class<?>, Supplier<Mission>> missionFactories = Map.of(
            ObtainItemMission.class, () -> {
//                long millis = GameManager.getInstance().getGameElapsedMillis();
//                double t = Math.min(1, millis / MILLIS_PER_HOUR);
//                double a = 3;
//                double b = 1.5;
//                double p = rnd.nextDouble();
//                double transformed = Math.pow(p, a * (1 - t) + b * t);
//                int difficulty = (int) Math.floor(transformed * 9 + 1);

                Material[] materials = Arrays.stream(Material.values()).filter(Material::isItem).toArray(Material[]::new);
                return new ObtainItemMission(GameManager.getInstance(), materials[rnd.nextInt(materials.length)]);
            },
            KillPlayerMission.class, () -> new KillPlayerMission(GameManager.getInstance()),
            PlaceBlocksMission.class, () -> new PlaceBlocksMission(
                    GameManager.getInstance(),
                    rnd.nextInt(MIN_BLOCK_COUNT, MAX_BLOCK_COUNT + 1)
            ),
            ReceiveDamageMission.class, () -> {
                EntityDamageEvent.DamageCause[] possibleCauses = new EntityDamageEvent.DamageCause[] {
                        EntityDamageEvent.DamageCause.CONTACT,
                        EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                        EntityDamageEvent.DamageCause.PROJECTILE,
                        EntityDamageEvent.DamageCause.SUFFOCATION,
                        EntityDamageEvent.DamageCause.FALL,
                        EntityDamageEvent.DamageCause.FIRE,
                        EntityDamageEvent.DamageCause.FIRE_TICK,
                        EntityDamageEvent.DamageCause.LAVA,
                        EntityDamageEvent.DamageCause.DROWNING,
                        EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                        EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                        EntityDamageEvent.DamageCause.VOID,
                        EntityDamageEvent.DamageCause.LIGHTNING,
                        EntityDamageEvent.DamageCause.STARVATION,
                        EntityDamageEvent.DamageCause.POISON,
                        EntityDamageEvent.DamageCause.MAGIC,
                        EntityDamageEvent.DamageCause.WITHER,
                        EntityDamageEvent.DamageCause.FALLING_BLOCK,
                        EntityDamageEvent.DamageCause.THORNS,
                        EntityDamageEvent.DamageCause.DRAGON_BREATH,
                        EntityDamageEvent.DamageCause.FLY_INTO_WALL,
                        EntityDamageEvent.DamageCause.HOT_FLOOR,
                        EntityDamageEvent.DamageCause.CAMPFIRE,
                        EntityDamageEvent.DamageCause.CRAMMING,
                        EntityDamageEvent.DamageCause.FREEZE,
                        EntityDamageEvent.DamageCause.SONIC_BOOM
                };
                return new ReceiveDamageMission(
                        GameManager.getInstance(),
                        possibleCauses[rnd.nextInt(possibleCauses.length)]
                );
            },
            CoverDistanceMission.class, () -> new CoverDistanceMission(
                    GameManager.getInstance(),
                    rnd.nextInt(MIN_DISTANCE_100m, MAX_DISTANCE_100m + 1) * 100
            ),
            KillMobMission.class, () -> {
                EntityType[] possibleTypes = Arrays.stream(EntityType.values()).filter(EntityType::isAlive).toArray(EntityType[]::new);
                return new KillMobMission(GameManager.getInstance(), possibleTypes[rnd.nextInt(possibleTypes.length)]);
            },
            GetPotionEffectMission.class, () -> {
                PotionEffectType[] possibleEffects = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(Registry.EFFECT.iterator(), 0), false
                ).toArray(PotionEffectType[]::new);
                return new GetPotionEffectMission(
                        GameManager.getInstance(),
                        possibleEffects[rnd.nextInt(possibleEffects.length)]
                );
            },
            KillYourselfMission.class, () -> new KillYourselfMission(GameManager.getInstance()),
            DoClutchMission.class, () -> {
                double clutchHeightSqrt = rnd.nextInt(MIN_CLUTCH_HEIGHT_SQRT, MAX_CLUTCH_HEIGHT_SQRT);
                double clutchHeight = clutchHeightSqrt * clutchHeightSqrt;
                ClutchType[] possibleClutchTypes = ClutchType.values();
                return new DoClutchMission(
                        GameManager.getInstance(),
                        possibleClutchTypes[rnd.nextInt(possibleClutchTypes.length)],
                        clutchHeight
                );
            }
    );

    public static Mission generateRandomMission() {
        List<Class<?>> weightedMissionTypes = new ArrayList<>();
        for (Map.Entry<Class<?>, Integer> missionWeight : missionWeights.entrySet()) {
            for (int i = 0; i < missionWeight.getValue(); i++) weightedMissionTypes.add(missionWeight.getKey());
        }
        Class<?> missionType = weightedMissionTypes.get(rnd.nextInt(weightedMissionTypes.size()));
        return missionFactories.get(missionType).get();
    }
}
