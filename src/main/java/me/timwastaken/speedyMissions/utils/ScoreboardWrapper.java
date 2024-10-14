package me.timwastaken.speedyMissions.utils;

import me.timwastaken.speedyMissions.GameState;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreboardWrapper {
    private static final String OBJECTIVE_NAME = "speedymissions_gamestate";

    public static Scoreboard getGameStateScoreboard(GameState state) {
        SidebarBuilder builder = new SidebarBuilder(OBJECTIVE_NAME);
        builder.setTitle(Notifications.getScoreboardTitle())
                .addEmptyLine()
                .addLine(Notifications.getMissionDescriptionTitle())
                .addLine(Notifications.getMissionDescription(state.getActiveMission()))
                .addEmptyLine();
        int playerLines = 0;
        Iterator<Map.Entry<Player, Integer>> playerScores = new ConditionalIterator<>(
                new MappingIterator<>(
                    state.getPlayerScores().entrySet().iterator(),
                    entry -> new AbstractMap.SimpleImmutableEntry<>(
                            Bukkit.getPlayer(entry.getKey()),
                            entry.getValue()
                    )
                ),
                entry -> entry.getKey() != null
        );
        while (playerLines < 8 && playerScores.hasNext()) {
            Map.Entry<Player, Integer> playerScoreEntry = playerScores.next();
            builder.addLine(Notifications.getPlayerScoreLine(
                    playerScoreEntry.getKey(),
                    playerScoreEntry.getValue(),
                    state.hasFinished(playerScoreEntry.getKey())
            ));
            playerLines++;
        }
        builder
                .addEmptyLine()
                .addLine(Notifications.getTimeRemainingTitle())
                .addLine(Notifications.getTimeRemainingLine(
                        state.getMissionStartGameTick() + state.getActiveMission().getGameTickDuration() - state.getOperatingWorld().getFullTime()
                ));
//                .addLine(Notifications.getTimeRemainingLine(state.getOperatingWorld().getFullTime()));
        return builder.build();
    }
}
