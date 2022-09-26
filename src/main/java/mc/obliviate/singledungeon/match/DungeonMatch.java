package mc.obliviate.singledungeon.match;

import com.google.common.base.Preconditions;
import mc.obliviate.singledungeon.SingleDungeon;
import mc.obliviate.singledungeon.arena.DungeonArena;
import mc.obliviate.singledungeon.match.mob.MobTracker;
import mc.obliviate.singledungeon.match.state.ArenaState;
import mc.obliviate.singledungeon.match.state.StartingState;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class DungeonMatch {

    private final DungeonArena arena;
    private final MobTracker mobTracker = new MobTracker(this);
    private final List<DungeonPlayer> playerList;
    private final List<BukkitTask> tasks = new ArrayList<>();
    private ArenaState currentState = null;

    public DungeonMatch(DungeonArena arena, List<DungeonPlayer> playerList) {
        this.arena = Objects.requireNonNull(arena);
        Preconditions.checkNotNull(playerList, "dungeon player list cannot be null");
        this.playerList = new ArrayList<>(playerList);
        Preconditions.checkArgument(!playerList.isEmpty(), "dungeon player list cannot be empty");
        playerList.forEach(dPlayer -> {
            Preconditions.checkArgument(!DungeonPlayer.isInDungeon(dPlayer.getPlayerUniqueId()), dPlayer.getPlayer().getName() + " cannot join to a dungeon. The player already playing a dungeon.");
            dPlayer.setDungeonMatch(this);
        });
    }

    public void start() {
        this.setCurrentState(new StartingState(this));
    }

    public ArenaState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(ArenaState currentState) {
        this.currentState = currentState;
    }

    public List<DungeonPlayer> getPlayerList() {
        return Collections.unmodifiableList(this.playerList);
    }

    public DungeonArena getArena() {
        return arena;
    }

    public void sendMessageToDungeonPlayers(String message) {
        this.playerList.forEach(dungeonPlayer -> dungeonPlayer.getPlayer().sendMessage(message));
    }

    public void leave(DungeonPlayer dungeonPlayer, LeaveReason reason) {
        dungeonPlayer.exitDungeon();
        this.playerList.remove(dungeonPlayer);
        Objects.requireNonNull(dungeonPlayer.getPlayerDataBackup(), dungeonPlayer.getPlayerUniqueId() + " player backup could not found!").load();
        SingleDungeon.getInstance().getDungeonRepository().save(dungeonPlayer.getStatistics());
        if (!reason.equals(LeaveReason.UNINSTALL) && this.playerList.isEmpty()) {
            this.uninstall();
        }
    }

    public void uninstall() {
        for (DungeonPlayer dungeonPlayer : new ArrayList<>(this.playerList)) {
            leave(dungeonPlayer, LeaveReason.UNINSTALL);
        }
        this.tasks.forEach(BukkitTask::cancel);
        Objects.requireNonNull(this.arena.getSpawnLocation().getWorld()).getLivingEntities().stream().filter(entity -> this.mobTracker.getEntityIds().contains(entity.getEntityId())).forEach(Entity::remove);
    }

    public void runLater(long tick, Runnable runnable) {
        this.tasks.add(Bukkit.getScheduler().runTaskLater(SingleDungeon.getInstance(), runnable, tick));
    }

    public void runTimer(long tick, Runnable runnable) {
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(SingleDungeon.getInstance(), runnable, 0, tick));
    }

    public List<BukkitTask> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }

    public MobTracker getMobTracker() {
        return this.mobTracker;
    }

    public enum LeaveReason {
        UNINSTALL,
        DISCONNECT,
        COMMAND,
        DEATH
    }
}
