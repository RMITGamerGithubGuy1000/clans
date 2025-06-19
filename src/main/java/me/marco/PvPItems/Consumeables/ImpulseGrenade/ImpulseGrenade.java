package me.marco.PvPItems.Consumeables.ImpulseGrenade;

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

public class ImpulseGrenade extends Consumeable {

    public ImpulseGrenade(Core core) {
        super(core, "Impulse Grenade", Material.FIREWORK_STAR);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        throwGrenade(player);
    }

    public void throwGrenade(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        Item item = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), new ItemStack(Material.FIREWORK_STAR));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Impulse Grenade");
        item.setCustomNameVisible(true);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(.8);
        item.setVelocity(tosend);
        getInstance().getPvPItemManager().addTask(new ImpulseGrenadeTag(client, item, getInstance()));
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
