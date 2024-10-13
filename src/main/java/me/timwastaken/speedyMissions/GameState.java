package me.timwastaken.speedyMissions;

import me.timwastaken.speedyMissions.missions.Mission;
import org.bukkit.entity.Player;

import java.util.*;

public class GameState {
    private Map<UUID, Integer> playerScores;
    private Collection<UUID> finishedPlayers;
    private Mission activeMission = null;
    private int delta = 0;

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
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    private void checkPlayerScore(Player p) {
        if (!this.playerScores.containsKey(p)) throw new IllegalArgumentException(p.getName() + " has no score!");
    }

    public int getPlayerScore(Player p) {
        this.checkPlayerScore(p);
        return this.playerScores.get(p.getUniqueId());
    }

    public void setPlayerScore(Player p, int newScore) {
        this.checkPlayerScore(p);
        this.playerScores.put(p.getUniqueId(), newScore);
    }

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
