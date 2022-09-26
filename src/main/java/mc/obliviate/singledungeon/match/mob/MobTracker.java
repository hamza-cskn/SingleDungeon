package mc.obliviate.singledungeon.match.mob;

import mc.obliviate.singledungeon.listener.MobListener;
import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class MobTracker {

    private final DungeonMatch match;
    private final Set<Integer> entityIds = new HashSet<>();

    public MobTracker(DungeonMatch match) {
        this.match = match;
        MobListener.registerTracker(this);
    }

    public Entity spawn(EntityType entityType, Location location) {
        final Entity entity = Objects.requireNonNull(location.getWorld()).spawnEntity(location, entityType);
        this.entityIds.add(entity.getEntityId());

        List<Player> viewers = new ArrayList<>();
        this.match.getPlayerList().forEach(dPlayer -> viewers.add(dPlayer.getPlayer()));

        hideEntityFromAllPlayers(entity, viewers);
        return entity;
    }

    public static void hideEntityFromAllPlayers(Entity entity, List<Player> exceptPlayers) {
        for (Player player : entity.getWorld().getPlayers()) {
            if (exceptPlayers.contains(player)) continue;
            hideEntity(entity, player);
        }
    }

    public static void hideEntity(Entity entity, Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        ((CraftPlayer) player).getHandle().b.a(packet);
    }

    private void unregister(int entityId) {
        this.entityIds.remove(entityId);
    }

    public Set<Integer> getEntityIds() {
        return Collections.unmodifiableSet(this.entityIds);
    }

    public DungeonMatch getMatch() {
        return this.match;
    }

    public void onMobDeath(EntityDamageEvent event) {
        this.unregister(event.getEntity().getEntityId());
        event.getEntity().remove();
        if (event instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
                DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(((EntityDamageByEntityEvent) event).getDamager().getUniqueId());
                Mob mob = (Mob) event.getEntity();
                if (mob.getEquipment() == null) return;
                if (mob.getEquipment().getHelmet() == null) return;
                switch (mob.getEquipment().getHelmet().getType()) {
                    case MAGMA_BLOCK ->
                            dungeonPlayer.getStatistics().setMagmaZombieKills(dungeonPlayer.getStatistics().getMagmaZombieKills() + 1);
                    case SEA_LANTERN ->
                            dungeonPlayer.getStatistics().setLapisZombieKills(dungeonPlayer.getStatistics().getLapisZombieKills() + 1);
                    case CHAINMAIL_HELMET ->
                            dungeonPlayer.getStatistics().setKnightSkeletonKills(dungeonPlayer.getStatistics().getKnightSkeletonKills() + 1);
                }
            }
        }
        if (this.entityIds.isEmpty()) {
            this.match.getCurrentState().next();
            this.match.sendMessageToDungeonPlayers("All dungeon mobs died. Dungeon ending...");
            this.match.sendMessageToDungeonPlayers("" + this.match.getCurrentState().getClass().getName());
        } else {
            this.match.sendMessageToDungeonPlayers("You killed a dungeon mob. " + entityIds.size() + " mobs left.");
        }
    }
}
