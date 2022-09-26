package mc.obliviate.singledungeon.data.backup;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerDataBackup {

    private final UUID playerUniqueId;
    private final PlayerInventoryWrapper inventoryWrapper;
    private final float experience;
    private final double health;
    private final Location location;
    private final int foodLevel;

    public PlayerDataBackup(Player player) {
        this.playerUniqueId = player.getUniqueId();
        this.inventoryWrapper = new PlayerInventoryWrapper(player.getInventory());
        this.experience = player.getExp();
        this.health = player.getHealth();
        this.location = player.getLocation();
        this.foodLevel = player.getFoodLevel();
    }

    public void clear() {
        final Player player = Bukkit.getPlayer(this.playerUniqueId);
        Preconditions.checkNotNull(player, "player is not online: " + this.playerUniqueId);
        player.getInventory().clear();
        player.setExp(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.GOLDEN_APPLE, 3));
    }

    public void load() {
        final Player player = Bukkit.getPlayer(this.playerUniqueId);
        Preconditions.checkNotNull(player, "player is not online: " + this.playerUniqueId);
        this.inventoryWrapper.load(player.getInventory());
        player.setExp(this.experience);
        player.setHealth(this.health);
        player.setFoodLevel(this.foodLevel);
        if (!player.teleport(this.location)) {
            player.kickPlayer(ChatColor.RED + "You could not teleported to spawn back.");
        }

    }

}
