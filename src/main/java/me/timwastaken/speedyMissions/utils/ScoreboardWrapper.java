package me.timwastaken.speedyMissions.utils;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.GameState;
import me.timwastaken.speedyMissions.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class ScoreboardWrapper {
    private static final String OBJECTIVE_NAME = "speedymissions_gamestate";
    private static final String PROGRESS_OBJECTIVE_NAME = "speedymissions_progress";

    public static Scoreboard getGameStateScoreboard(GameState state) throws IllegalStateException {
        // sidebar
        SidebarBuilder builder = new SidebarBuilder(OBJECTIVE_NAME);
        builder.setTitle(Notifications.getScoreboardTitle())
                .addEmptyLine()
                .addLine(Notifications.getMissionDescriptionTitle());
        List<String> descriptionLines = splitStringIntoLines(
                state.getActiveMission().getMissionDescription(),
                24
        );
        descriptionLines.forEach(builder::addLine);
        builder.addEmptyLine();
        int remainingLines = builder.getRemainingLineCount();
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
        while (playerLines < remainingLines - 3 && playerScores.hasNext()) {
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
                .addLine(Notifications.getTimeRemainingLine(state.getMillisRemaining()));
        Scoreboard infoBoard = builder.build();

        // player progress
        if (!state.getActiveMission().hasProgressScores()) return infoBoard;
        Objective progress = infoBoard.registerNewObjective(
                PROGRESS_OBJECTIVE_NAME,
                Criteria.DUMMY,
                "Player progress"
        );
        progress.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        for (Iterator<Player> it = GameManager.getInstance().getActivePlayerIterator(); it.hasNext(); ) {
            Player p = it.next();
            int progressScore = state.getActiveMission().getPlayerProgress(p);
            progress.getScore(p.getName()).setScore(progressScore);
        }

        return infoBoard;
    }

    /**
     * Splits a given string into multiple lines, each with a maximum specified length.
     *
     * @param text The text to be split into lines.
     * @param maxLineLength The maximum length of each line.
     * @return A list of strings, each representing a line of the original text.
     */
    private static List<String> splitStringIntoLines(String text, int maxLineLength) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+"); // Split text into words based on whitespace
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // If adding the next word exceeds the line length limit, store the current line and start a new one
            if (currentLine.length() + word.length() + 1 > maxLineLength) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(); // Start a new line
            }

            // Add the word to the current line
            if (!currentLine.isEmpty()) {
                currentLine.append(" "); // Add a space before the word if it's not the first word in the line
            }
            currentLine.append(word);
        }

        // Add the last line if there is any leftover content
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}
