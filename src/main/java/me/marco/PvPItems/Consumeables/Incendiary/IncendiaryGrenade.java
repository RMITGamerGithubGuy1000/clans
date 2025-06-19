package me.marco.PvPItems.Consumeables.Incendiary;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.Consumeables.Consumeable;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class IncendiaryGrenade extends Consumeable {

    public IncendiaryGrenade(Core core) {
        super(core, "Incendiary Grenade", Material.MAGMA_CREAM);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        throwGrenade(player);
    }

    public void throwGrenade(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        Item item = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), new ItemStack(Material.MAGMA_CREAM));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Incendiary Grenade");
        item.setCustomNameVisible(true);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(.6);
        item.setVelocity(tosend);
        getInstance().getPvPItemManager().addTask(new IncendiaryGrenadeTag(client, item, getInstance()));
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


