package mc.obliviate.singledungeon.stats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "Statistics")
public class DungeonStatistics {

    @Id
    @Column(name = "player", nullable = false, unique = true)
    private UUID playerUniqueId;

    @Column(name = "magma_zombie_kill_count", nullable = false)
    private int magmaZombieKills;

    @Column(name = "lapis_zombie_kill_count", nullable = false)
    private int lapisZombieKills;

    @Column(name = "knight_skeleton_kill_count", nullable = false)
    private int knightSkeletonKills;

    public DungeonStatistics() {
    }

    public DungeonStatistics(UUID playerUniqueId) {
        this.playerUniqueId = playerUniqueId;
    }

    public void setKnightSkeletonKills(int knightSkeletonKills) { this.knightSkeletonKills = knightSkeletonKills; }

    public void setLapisZombieKills(int lapisZombieKills) {
        this.lapisZombieKills = lapisZombieKills;
    }

    public void setMagmaZombieKills(int magmaZombieKills) {
        this.magmaZombieKills = magmaZombieKills;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public int getKnightSkeletonKills() {
        return knightSkeletonKills;
    }

    public int getLapisZombieKills() {
        return lapisZombieKills;
    }

    public int getMagmaZombieKills() {
        return magmaZombieKills;
    }
}
