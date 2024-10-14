package me.timwastaken.speedyMissions.utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class SidebarBuilder {
    private int emptyCount = 0;
    private final String objectiveName;
    private String sidebarTitle;
    private final List<String> lines;
    private static final int MAX_LINES = 15;

    public SidebarBuilder(String objectiveName) {
        this.objectiveName = objectiveName;
        this.lines = new ArrayList<>();
    }

    public SidebarBuilder setTitle(String sidebarTitle) {
        this.sidebarTitle = sidebarTitle;
        return this;
    }

    public SidebarBuilder addLine(String line) {
        if (line.isBlank()) return this.addEmptyLine();
        if (this.lines.contains(line)) throw new IllegalArgumentException("Cannot add duplicate line to sidebar!");
        this.lines.add(line);
        return this;
    }

    public SidebarBuilder addEmptyLine() {
        StringBuilder empty = new StringBuilder();
        while (empty.length() < this.emptyCount) empty.append(" ");
        this.lines.add(empty.toString());
        this.emptyCount++;
        return this;
    }

    public int getRemainingLineCount() {
        return MAX_LINES - this.lines.size();
    }

    public Scoreboard build() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) throw new IllegalStateException("Scoreboard manager is null!");
        Scoreboard board = scoreboardManager.getNewScoreboard();
        Objective objective;
        try {
            objective = board.registerNewObjective(
                    this.objectiveName,
                    Criteria.DUMMY,
                    this.sidebarTitle
            );
        } catch (IllegalArgumentException e) {
            Objective registered = board.getObjective(this.objectiveName);
            if (registered == null) throw new IllegalStateException(
                    String.format("Error occurred while trying to create objective '%s'!", this.objectiveName)
            );
            registered.unregister();
            objective = board.registerNewObjective(
                    this.objectiveName,
                    Criteria.DUMMY,
                    this.sidebarTitle
            );
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < this.lines.size(); i++) {
            String line = this.lines.get(i);
            Score scoreLine = objective.getScore(line);
            scoreLine.setScore(this.lines.size() - i - 1);
        }
        return board;
    }
}
