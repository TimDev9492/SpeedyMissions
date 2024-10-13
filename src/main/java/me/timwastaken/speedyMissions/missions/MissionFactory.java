package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class MissionFactory {
    private static final Random rnd = new Random();

    private static final Map<Class<?>, Integer> missionWeights = Map.of(
            ObtainItemMission.class, 1,
            KillPlayerMission.class, 5
    );

    private static final Map<Class<?>, Supplier<Mission>> missionFactories = Map.of(
            ObtainItemMission.class, () -> {
                Material[] materials = Material.values();
                return new ObtainItemMission(GameManager.getInstance(), materials[rnd.nextInt(materials.length)]);
            },
            KillPlayerMission.class, () -> new KillPlayerMission(GameManager.getInstance())
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
