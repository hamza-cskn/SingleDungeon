package mc.obliviate.singledungeon.command;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.singledungeon.stats.DungeonStatistics;
import mc.obliviate.singledungeon.user.DungeonPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

import javax.annotation.Nonnull;

public class StatisticCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("This command is player only.");
            return false;
        }

        new StatsGUI(player).open();

        return false;
    }


    public static class StatsGUI extends Gui {

        private final DungeonStatistics stats;

        public StatsGUI(@Nonnull Player player) {
            super(player, "stats-gui", "Dungeon Statistics", 4);
            this.stats = DungeonPlayer.getDungeonPlayer(player.getUniqueId()).getStatistics();
        }

        @Override
        public void onOpen(InventoryOpenEvent event) {
            fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE), 3);
            addItem(10, new Icon(Material.SEA_LANTERN).setName(ChatColor.GOLD + "Lapis Zombie").setLore(ChatColor.GRAY + "Kills: " + ChatColor.WHITE + this.stats.getLapisZombieKills()));
            addItem(13, new Icon(Material.MAGMA_BLOCK).setName(ChatColor.GOLD + "Magma Zombie").setLore(ChatColor.GRAY + "Kills: " + ChatColor.WHITE + this.stats.getMagmaZombieKills()));
            addItem(15, new Icon(Material.SKELETON_SKULL).setName(ChatColor.GOLD + "Skeleton Knight").setLore(ChatColor.GRAY + "Kills: " + ChatColor.WHITE + this.stats.getKnightSkeletonKills()));
        }
    }
}
