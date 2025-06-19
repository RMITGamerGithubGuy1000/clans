package me.marco.PvPItems.Consumeables;

import me.marco.Base.Core;
import me.marco.PvPItems.PvPItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public abstract class Consumeable extends PvPItem {

    public Consumeable(Core core, String name, Material itemType) {
        super(core, name, itemType);
    }

    public Consumeable(Core core, String name, Material itemType, boolean canCastSafeZone) {
        super(core, name, itemType, canCastSafeZone);
    }

    @Override
    public boolean useageCheck(Player player, Action action) {
        if(!this.getCDManager().add(player, getPvPItemName(), getUseageCat(), 0, getCooldown(action), true)) return false;
        if(!consumeItem(player)) return false;
        return true;
    }

    public boolean canConsumeItem(Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack == null || itemStack.getType() != getItemType()) return false;
        return true;
    }

    public boolean consumeItem(Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack == null || itemStack.getType() != getItemType()) return false;
        int amount = itemStack.getAmount();
        int newAmount = amount - 1;
        if(newAmount < 1){
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }else{
            itemStack.setAmount(newAmount);
        }
        player.updateInventory();
        return true;
    }

    public String getName(Action action){
        return this.getPvPItemName();
    }

}
