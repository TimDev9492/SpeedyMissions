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
//            boolean allFinished = true;
//            for (Iterator<Player> it = this.getActivePlayerIterator(); it.hasNext(); ) {
//                Player p = it.next();
//                if (!players.contains(p)) {
//                    allFinished = false;
//                    break;
//                }
//            }
//            if (allFinished) {
//                this.setupNextMission();
//                return;
//            }

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
    public void startGame(World mainWorld) throws IllegalStateException {
        if (this.gameRunning) throw new IllegalStateException("The game is already running!");
        this.gameRunning = true;
        this.registeredPlayers.clear();
        Bukkit.getOnlinePlayers().forEach(p -> this.registeredPlayers.add(p.getUniqueId()));

        Map<UUID, Integer> playerScores = new ValueSortedMap<>(
                Integer::compareTo,
                new PlayerNameComparatorByUUID()
        );
        for (UUID uuid : this.registeredPlayers) {
            playerScores.put(uuid, 0);
        }
        this.state = new GameState(playerScores, mainWorld);

        this.setupNextMission();

        new BukkitRunnable() {
            @Override
            public void run() {
                Scoreboard sidebar = ScoreboardWrapper.getGameStateScoreboard(state);
                for (Iterator<Player> it = getActivePlayerIterator(); it.hasNext(); ) {
                    Player active = it.next();
                    active.setScoreboard(sidebar);
                }
            }
        }.runTaskTimer(SpeedyMissions.getInstance(), 0L, 1L);
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
        }
    }
}
