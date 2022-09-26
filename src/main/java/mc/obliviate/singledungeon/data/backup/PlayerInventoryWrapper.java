package mc.obliviate.singledungeon.data.backup;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryWrapper {

    private final ItemStack[] armorContents;
    private final ItemStack[] contents;

    PlayerInventoryWrapper(PlayerInventory inventory) {
        this.contents = inventory.getContents();
        this.armorContents = inventory.getArmorContents();
    }

    public void load(PlayerInventory inventory) {
        inventory.setContents(this.contents);
        inventory.setArmorContents(this.armorContents);
    }

}
