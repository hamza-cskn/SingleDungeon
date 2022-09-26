package mc.obliviate.singledungeon.command;

import mc.obliviate.singledungeon.arena.DungeonArena;
import mc.obliviate.singledungeon.match.DungeonMatch;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class StartCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("This command is player only.");
            return false;
        }

        String arenaName = "default";
        if (args.length > 0) {
            arenaName = args[0];
        }

        DungeonArena arena = DungeonArena.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "No arena found: " + arenaName);
            return false;
        }

        new DungeonMatch(arena, Collections.singletonList(DungeonPlayer.getDungeonPlayer(player.getUniqueId()))).start();

        return false;
    }
}
