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

public class LeaveCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("This command is player only.");
            return false;
        }

        DungeonPlayer dungeonPlayer = DungeonPlayer.getDungeonPlayer(player.getUniqueId());
        if (dungeonPlayer.getDungeonMatch() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a dungeon game.");
            return false;
        }

        dungeonPlayer.getDungeonMatch().leave(dungeonPlayer, DungeonMatch.LeaveReason.COMMAND);

        return false;
    }
}
