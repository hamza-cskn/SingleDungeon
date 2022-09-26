package mc.obliviate.singledungeon.user;

import com.google.common.base.Preconditions;
import mc.obliviate.singledungeon.SingleDungeon;
import mc.obliviate.singledungeon.data.backup.PlayerDataBackup;
import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.stats.DungeonStatistics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class DungeonPlayer {

    private static final Map<UUID, DungeonPlayer> DUNGEON_PLAYER_MAP = new HashMap<>();

    private final UUID playerUniqueId;
    private PlayerDataBackup playerDataBackup;
    private final DungeonStatistics statistics;
    private DungeonMatch dungeonMatch = null;

    public DungeonPlayer(UUID playerUniqueId) {
        Preconditions.checkNotNull(playerUniqueId, "uuid cannot be null");
        this.playerUniqueId = playerUniqueId;
        this.statistics = SingleDungeon.getInstance().getDungeonRepository().findByUID(this.playerUniqueId).orElseGet(() -> new DungeonStatistics(playerUniqueId));

        DungeonPlayer.DUNGEON_PLAYER_MAP.put(playerUniqueId, this);
    }

    @Nonnull
    public static DungeonPlayer getDungeonPlayer(UUID playerUniqueId) {
        return Objects.requireNonNull(DungeonPlayer.DUNGEON_PLAYER_MAP.get(playerUniqueId));
    }

    public static boolean isInDungeon(UUID playerUniqueId) {
        return DungeonPlayer.getDungeonPlayer(playerUniqueId).getDungeonMatch() != null;
    }

    @Nonnull
    public static Map<UUID, DungeonPlayer> getDungeonPlayerMap() {
        return DUNGEON_PLAYER_MAP;
    }

    @Nullable
    public DungeonMatch getDungeonMatch() {
        return this.dungeonMatch;
    }

    public void exitDungeon() {
        this.setDungeonMatch(null);
    }

    public void setDungeonMatch(DungeonMatch dungeonMatch) {
        this.dungeonMatch = dungeonMatch;
    }

    @Nullable
    public UUID getPlayerUniqueId() {
        return this.playerUniqueId;
    }

    @Nonnull
    public Player getPlayer() {
        return Objects.requireNonNull(Bukkit.getPlayer(this.playerUniqueId));
    }

    @Nullable
    public PlayerDataBackup getPlayerDataBackup() {
        return playerDataBackup;
    }

    public PlayerDataBackup backup() {
        this.playerDataBackup = new PlayerDataBackup(this.getPlayer());
        return this.playerDataBackup;
    }

    public DungeonStatistics getStatistics() {
        return statistics;
    }
}
