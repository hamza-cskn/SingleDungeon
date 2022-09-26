package mc.obliviate.singledungeon.match.state;

import mc.obliviate.singledungeon.SingleDungeon;
import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.match.mob.MobTracker;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartingState implements ArenaState {

    private final DungeonMatch dungeonMatch;

    public StartingState(DungeonMatch dungeonMatch) {
        this.dungeonMatch = dungeonMatch;
        this.start();
    }

    private void start() {
        List<Player> viewers = new ArrayList<>();
        this.dungeonMatch.getPlayerList().forEach(dPlayer -> viewers.add(dPlayer.getPlayer()));

        for (DungeonPlayer dungeonPlayer : this.dungeonMatch.getPlayerList()) {
            dungeonPlayer.backup().clear();
            if (!dungeonPlayer.getPlayer().teleport(this.dungeonMatch.getArena().getSpawnLocation())) {
                this.dungeonMatch.uninstall();
                return;
            } else {
                for (Entity entity : Objects.requireNonNull(this.dungeonMatch.getArena().getSpawnLocation().getWorld()).getEntities()) {
                    if (entity instanceof Mob)
                        MobTracker.hideEntity(entity, dungeonPlayer.getPlayer());
                    if (entity instanceof Player player && !viewers.contains(player)) {
                        player.hidePlayer(SingleDungeon.getInstance(), dungeonPlayer.getPlayer());
                        dungeonPlayer.getPlayer().hidePlayer(SingleDungeon.getInstance(), player);
                    }
                }
            }
        }
        this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.AQUA + "You teleported to a dungeon.");
        this.dungeonMatch.runLater(60, this::next);
    }

    @Override
    public void next() {
        this.dungeonMatch.setCurrentState(new PlayingState(dungeonMatch));
    }

    @Override
    public DungeonMatch getDungeonArena() {
        return dungeonMatch;
    }
}
