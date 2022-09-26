package mc.obliviate.singledungeon.listener;

import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ArenaListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (!DungeonPlayer.isInDungeon(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (!DungeonPlayer.isInDungeon(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(event.getPlayer().getUniqueId());
        if (dungeonPlayer.getDungeonMatch() == null) return;
        if (event.getTo() == null) return;
        if (event.getTo().getWorld() == null) return;
        if (event.getTo().getWorld().equals(dungeonPlayer.getDungeonMatch().getArena().getSpawnLocation().getWorld())) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport to another world. You're playing a dungeon.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(player.getUniqueId());
        if (dungeonPlayer.getDungeonMatch() == null) return;
        if (event.getFinalDamage() < player.getHealth()) return;
        dungeonPlayer.getDungeonMatch().leave(dungeonPlayer, DungeonMatch.LeaveReason.DEATH);
    }
}
