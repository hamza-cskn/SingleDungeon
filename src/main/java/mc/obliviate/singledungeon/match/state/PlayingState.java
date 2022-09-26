package mc.obliviate.singledungeon.match.state;

import com.google.common.base.Preconditions;
import mc.obliviate.singledungeon.match.DungeonMatch;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class PlayingState implements ArenaState {

    private final DungeonMatch dungeonMatch;

    public PlayingState(DungeonMatch dungeonMatch) {
        this.dungeonMatch = dungeonMatch;
        start();
    }

    private void start() {
        this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.GREEN + "Dungeon mobs spawning...");
        for (Location loc : dungeonMatch.getArena().getMobSpawnLocations()) {
            spawnMagmaZombie(loc);
            spawnKnightSkeleton(loc);
            spawnLapisZombie(loc);
        }
    }

    private void spawnKnightSkeleton(Location loc) {
        Skeleton skeleton = (Skeleton) this.dungeonMatch.getMobTracker().spawn(EntityType.SKELETON, loc);
        skeleton.setPersistent(true);
        Preconditions.checkNotNull(skeleton.getEquipment());
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
    }

    private void spawnMagmaZombie(Location loc) {
        Zombie zombie = (Zombie) this.dungeonMatch.getMobTracker().spawn(EntityType.ZOMBIE, loc);
        zombie.setAdult();
        zombie.setPersistent(true);
        Preconditions.checkNotNull(zombie.getEquipment());
        zombie.getEquipment().setHelmet(new ItemStack(Material.MAGMA_BLOCK));
        zombie.getEquipment().setLeggings(new ItemStack(Material.LEATHER_CHESTPLATE));
    }

    private void spawnLapisZombie(Location loc) {
        Zombie zombie = (Zombie) this.dungeonMatch.getMobTracker().spawn(EntityType.ZOMBIE, loc);
        zombie.setPersistent(true);
        zombie.setAdult();
        Preconditions.checkNotNull(zombie.getEquipment());
        zombie.getEquipment().setHelmet(new ItemStack(Material.SEA_LANTERN));
        zombie.getEquipment().setChestplate(this.dyeLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromBGR(200, 0, 0)));
        zombie.getEquipment().setLeggings(this.dyeLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromBGR(200, 0, 0)));
        zombie.getEquipment().setBoots(this.dyeLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromBGR(200, 0, 0)));
    }

    private ItemStack dyeLeatherArmor(ItemStack itemStack, Color color) {
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta meta) {
            meta.setColor(color);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    @Override
    public void next() {
        this.dungeonMatch.setCurrentState(new EndingState(dungeonMatch));
    }

    @Override
    public DungeonMatch getDungeonArena() {
        return dungeonMatch;
    }
}
