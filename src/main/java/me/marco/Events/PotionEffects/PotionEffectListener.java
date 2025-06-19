package me.marco.Events.PotionEffects;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectListener extends CListener<Core> {

    public PotionEffectListener(Core instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionEffect(PotionEffectEvent event){
        if(event.isCancelled()) return;
        Player toPotion = event.getToPotion();
        PotionEffectType potionEffectType = event.getPotionEffectType();
        toPotion.addPotionEffect(new PotionEffect(potionEffectType, event.getDuration(), event.getLevel()));
        if(!event.isSilent()) dropPotionItem(toPotion, potionEffectType);
    }

    private void dropPotionItem(Player given, PotionEffectType potionType){
        if(potionType == PotionEffectType.SLOWNESS){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.COAL));
            item.setCustomName(ChatColor.BLACK + "Slow");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
        if(potionType == PotionEffectType.SPEED){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.FEATHER));
            item.setCustomName("Speed");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
        if(potionType == PotionEffectType.REGENERATION){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.CHERRY_SAPLING));
            item.setCustomName(ChatColor.LIGHT_PURPLE + "Regen");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
        if(potionType == PotionEffectType.POISON){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.SLIME_BALL));
            item.setCustomName(ChatColor.DARK_GREEN + "Poison");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
        if(potionType == PotionEffectType.INVISIBILITY){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.POTION));
            item.setCustomName(ChatColor.ITALIC + "Invisibility");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
        if(potionType == PotionEffectType.FIRE_RESISTANCE){
            Item item = given.getWorld().dropItemNaturally(given.getLocation().add(0, 0.6, 0), new ItemStack(Material.CAMPFIRE));
            item.setCustomName(ChatColor.DARK_RED + "Fire Resistance");
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setTicksLived(5990);
            return;
        }
    }

}
