package mc.obliviate.singledungeon.listener;

import mc.obliviate.singledungeon.match.mob.MobTracker;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MobListener implements Listener {

    private static final Set<MobTracker> MOB_TRACKERS = new HashSet<>();

    public static void registerTracker(MobTracker mobTracker) {
        MobListener.MOB_TRACKERS.add(mobTracker);
    }

    public static MobTracker getTrackerByEntity(int entityId) {
        for (MobTracker mobTracker : MobListener.MOB_TRACKERS) {
            if (mobTracker.getEntityIds().contains(entityId)) return mobTracker;
        }
        return null;
    }

    public static Set<MobTracker> getMobTrackers() {
        return Collections.unmodifiableSet(MOB_TRACKERS);
    }

    @EventHandler
    public void onEntityDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Damageable)) return;
        if (((Damageable) event.getEntity()).getHealth() > event.getFinalDamage()) return;
        final MobTracker tracker = MobListener.getTrackerByEntity(event.getEntity().getEntityId());
        if (tracker == null) return;
        tracker.onMobDeath(event);
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player player) {
            DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(player.getUniqueId());
            if (dungeonPlayer.getDungeonMatch() == null) return;
            if (dungeonPlayer.getDungeonMatch().getMobTracker().getEntityIds().contains(event.getEntity().getEntityId())) return;
            event.setCancelled(true);
        }
    }

}
