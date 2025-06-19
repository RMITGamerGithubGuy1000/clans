package me.marco.PvPItems.Consumeables.Enderpearl;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.Consumeables.Consumeable;
import me.marco.PvPItems.Consumeables.Incendiary.IncendiaryGrenadeTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Enderpearl extends Consumeable {

    public Enderpearl(Core core) {
        super(core, "Enderpearl", Material.ENDER_PEARL);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        throwPearl(player);
    }

    public void throwPearl(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        Item item = player.getWorld().dropItem(player.getLocation().add(0, 2, 0), new ItemStack(Material.ENDER_PEARL));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Enderpearl");
        item.setCustomNameVisible(true);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(2);
        item.setVelocity(tosend);
        getInstance().getPvPItemManager().addTask(new EnderpearlTag(player, client, item, getInstance()));
    }

    @Override
    public double getCooldown(Action action) {
        return 2;
    }

    @Override
    public int getMana(Action action) {
        return 0;
    }
}
