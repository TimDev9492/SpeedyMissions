package me.timwastaken.speedyMissions;

import me.timwastaken.speedyMissions.missions.Mission;
import me.timwastaken.speedyMissions.missions.MissionFactory;
import me.timwastaken.speedyMissions.missions.ObtainItemMission;
import me.timwastaken.speedyMissions.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class captures the state of the game and
 * controls game events, such as ending and starting
 * new missions, managing player scores, etc.
 */
public class GameManager {
    public static final long SECONDS = 20;
    public static final long MINUTES = 60 * SECONDS;

    private static GameManager singletonInstance = null;

    private GameManager() {
        this.registeredPlayers = new ArrayList<>();
        this.playerFinishCallback = players -> {
            // skip mission if all players finish at the same time
            boolean allFinished = true;
            int playerCount = 0;
            for (Iterator<Player> it = this.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                playerCount++;
                if (!players.contains(p)) {
                    allFinished = false;
                    break;
                }
            }
            if (allFinished && playerCount > 1) {
                this.setupNextMission();
                return;
            }

            if (players.isEmpty()) return;
            int delta = this.state.getDelta();
            boolean containsNewFinisher = players.stream().anyMatch(p -> !this.state.hasFinished(p));
            if (!containsNewFinisher) return;
            for (Player player : players) {
                if (this.state.hasFinished(player)) continue;
                this.state.addFinishedPlayer(player);
                this.state.addPlayerScore(player, delta);
                Bukkit.broadcastMessage(Notifications.getPlayerFinishedNotification(player, delta));
            }
            for (Iterator<Player> it = this.getActivePlayerIterator(); it.hasNext(); ) {
                Player activePlayer = it.next();
                activePlayer.playSound(activePlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
            }
            if (delta > 1) {
                // decrease delta
                this.state.setDelta(delta - 1);
            }
        };
    }

    public static GameManager getInstance() {
        if (singletonInstance == null) singletonInstance = new GameManager();
        return singletonInstance;
    }

    private Collection<UUID> registeredPlayers;
    private GameState state;
    private boolean gameRunning = false;
    private final Consumer<Collection<Player>> playerFinishCallback;
    private int gameLoop = -1;

    public Iterator<Player> getActivePlayerIterator() {
        return new ConditionalIterator<>(
                new MappingIterator<>(
                    this.registeredPlayers.iterator(),
                    Bukkit::getPlayer
                ),
                Objects::nonNull
        );
    }

    public void setRegisteredPlayers(Collection<? extends Player> players) {
        this.registeredPlayers = players.stream().map(Entity::getUniqueId).collect(Collectors.toList());
    }

    /**
     * Start the game.
     */
    public void startGame() throws IllegalStateException {
        if (this.gameRunning) throw new IllegalStateException("The game is already running!");
        this.gameRunning = true;
        this.registeredPlayers.clear();
        Bukkit.getOnlinePlayers().forEach(p -> this.registeredPlayers.add(p.getUniqueId()));

        Map<UUID, Integer> playerScores = new ValueSortedMap<>(
                (a, b) -> b - a,
                new PlayerNameComparatorByUUID()
        );
        for (UUID uuid : this.registeredPlayers) {
            playerScores.put(uuid, 0);
        }
        this.state = new GameState(playerScores);

        this.setupNextMission();

        this.gameLoop = new BukkitRunnable() {
            @Override
            public void run() {
                if (state.getMillisRemaining() <= 0) {
                    skipMission();
                }
                Scoreboard sidebar = ScoreboardWrapper.getGameStateScoreboard(state);
                for (Iterator<Player> it = getActivePlayerIterator(); it.hasNext(); ) {
                    Player active = it.next();
                    active.setScoreboard(sidebar);
                }
            }
        }.runTaskTimer(SpeedyMissions.getInstance(), 0L, 1L).getTaskId();
    }

    public void stopGame() throws IllegalStateException {
        if (!this.gameRunning) throw new IllegalStateException("Can't stop the game when it's not running!");
        // clean up
        Bukkit.getScheduler().cancelTask(this.gameLoop);
        for (Iterator<Player> it = this.getActivePlayerIterator(); it.hasNext(); ) {
            Player p = it.next();
            if (Bukkit.getScoreboardManager() == null) continue;
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        this.registeredPlayers.clear();
        this.state = null;
        this.gameRunning = false;
    }

    public boolean getGameRunning() {
        return this.gameRunning;
    }

    /**
     * Generate a new mission.
     * @return The generated mission
     */
    private Mission generateNewMission() {
        return MissionFactory.generateRandomMission();
    }

    public void skipMission() {
        this.state.getActiveMission().stopMission();
        this.setupNextMission();
    }

    /**
     * Generate and start a new mission.
     */
    private void setupNextMission() {
        if (this.state.getActiveMission() != null) this.state.getActiveMission().stopMission();
        Mission next = this.generateNewMission();
        next.onPlayerFinishMission(this.playerFinishCallback);
        this.state.clearFinishedPlayers();
        this.state.setActiveMission(next);
        this.state.setDelta(5);

        next.startMission();
        for (Iterator<Player> it = this.getActivePlayerIterator(); it.hasNext(); ) {
            Player p = it.next();
            p.sendTitle(
                    Notifications.getMissionTitle(),
                    String.format("%s%s", ChatColor.GREEN, next.getMissionDescription()),
                    5, 100, 5
            );
            p.spigot().sendMessage(next.getMissionChatAnnouncement());
        }
    }

    public GameState getGameState() {
        return this.state;
    }

    public boolean isPlaying(Player p) {
        return this.registeredPlayers.contains(p.getUniqueId());
    }
}
