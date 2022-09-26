package mc.obliviate.singledungeon.listener;

import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        new DungeonPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(event.getPlayer().getUniqueId());
        if (dungeonPlayer.getDungeonMatch() != null) {
            dungeonPlayer.getDungeonMatch().leave(dungeonPlayer, DungeonMatch.LeaveReason.DISCONNECT);
        }
    }
}
