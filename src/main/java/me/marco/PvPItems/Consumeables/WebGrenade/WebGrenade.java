package me.marco.PvPItems.Consumeables.WebGrenade;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.Consumeables.Consumeable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class WebGrenade extends Consumeable {

    public WebGrenade(Core core) {
        super(core, "Web Grenade", Material.COBWEB);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        throwWeb(player);
    }

    public void throwWeb(Player player){
        Item item = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), new ItemStack(Material.COBWEB));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Web Grenade");
        item.setCustomNameVisible(true);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(1);
        item.setVelocity(tosend);
        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getPvPItemManager().addTask(new WebGrenadeTag(client, item, getInstance()));
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
