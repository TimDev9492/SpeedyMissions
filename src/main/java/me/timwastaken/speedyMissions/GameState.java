package me.timwastaken.speedyMissions;

import me.timwastaken.speedyMissions.missions.Mission;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class GameState {
    private final Map<UUID, Integer> playerScores;
    private final Collection<UUID> finishedPlayers;
    private Mission activeMission = null;
    private int delta = 0;
    private long missionStartTime;

    public GameState(Map<UUID, Integer> playerScores) {
        this.playerScores = playerScores;
        this.finishedPlayers = new HashSet<>();
    }

    public void addFinishedPlayer(Player p) {
        this.finishedPlayers.add(p.getUniqueId());
    }

    public boolean hasFinished(Player p) {
        return this.finishedPlayers.contains(p.getUniqueId());
    }

    public void clearFinishedPlayers() {
        this.finishedPlayers.clear();
    }

    public void setActiveMission(Mission mission) {
        this.activeMission = mission;
        this.missionStartTime = System.currentTimeMillis();
    }

    public long getMissionStart() {
        return this.missionStartTime;
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    /**
     * Check if the player has a score.
     * @param p The player to check.
     * @throws IllegalArgumentException If the player has no score.
     */
    private void checkPlayerScore(Player p) {
        if (!this.playerScores.containsKey(p.getUniqueId())) throw new IllegalArgumentException(p.getName() + " has no score!");
    }

    public int getPlayerScore(Player p) {
        this.checkPlayerScore(p);
        return this.playerScores.get(p.getUniqueId());
    }

    /**
     * Set the player's score.
     * @param p The player to set the score for.
     * @param newScore The new score.
     * @throws IllegalArgumentException If the player has no score.
     */
    public void setPlayerScore(Player p, int newScore) {
        this.checkPlayerScore(p);
        this.playerScores.put(p.getUniqueId(), newScore);
    }

    public Map<UUID, Integer> getPlayerScores() {
        return playerScores;
    }

    /**
     * Add a score to the player's score.
     * @param p The player to add the score to.
     * @param delta The score to add.
     * @throws IllegalArgumentException If the player has no score.
     */
    public void addPlayerScore(Player p, int delta) {
        int oldScore = this.getPlayerScore(p);
        this.setPlayerScore(p, oldScore + delta);
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }
}
