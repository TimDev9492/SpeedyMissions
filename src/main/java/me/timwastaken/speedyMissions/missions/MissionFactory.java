package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.Material;

import java.util.*;
import java.util.function.Supplier;

public class MissionFactory {
    private static final Random rnd = new Random();
    private static final int MIN_BLOCK_COUNT = 16;
    private static final int MAX_BLOCK_COUNT = 128;

    private static final Map<Class<?>, Integer> missionWeights = Map.of(
            ObtainItemMission.class, 1,
            KillPlayerMission.class, 1,
            PlaceBlocksMission.class, 3
    );

    private static final Map<Class<?>, Supplier<Mission>> missionFactories = Map.of(
            ObtainItemMission.class, () -> {
                Material[] materials = Arrays.stream(Material.values()).filter(Material::isItem).toArray(Material[]::new);
                return new ObtainItemMission(GameManager.getInstance(), materials[rnd.nextInt(materials.length)]);
            },
            KillPlayerMission.class, () -> new KillPlayerMission(GameManager.getInstance()),
            PlaceBlocksMission.class, () -> new PlaceBlocksMission(
                    GameManager.getInstance(),
                    rnd.nextInt(MIN_BLOCK_COUNT, MAX_BLOCK_COUNT + 1)
            )
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
