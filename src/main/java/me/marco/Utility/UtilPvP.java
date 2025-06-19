package me.marco.Utility;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Skills.Skills.Warrior.PassiveA.MaimTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class UtilPvP {

    private Core instance;

    public UtilPvP(Core instance){
        this.instance = instance;
    }

    public void setPvPHealth(Player player, double toBe){
        if(player.getHealth() >= toBe){
            spawnHeal(player, (toBe - player.getHealth()));
            player.setHealth(toBe);
            return;
        }
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasTag("MaimTag")){
            spawnHeal(player, (toBe - player.getHealth()));
            player.setHealth(toBe);
            return;
        }
        MaimTag maimTag = (MaimTag) client.getTag("MaimTag");
        double gained = toBe - player.getHealth();
        double reduction = maimTag.getReduction();
        double reduceBy = gained * reduction;
        double netHealth = toBe - reduceBy;
        double netGained = netHealth - player.getHealth();
        if(netHealth <= player.getHealth()) return;
        player.setHealth(netHealth);
        spawnHeal(player, netGained);
    }

    public void setPvPHealth(Player player, double toBe, boolean ignoreMaim){
        if(player.getHealth() >= toBe){
            player.setHealth(toBe);
            return;
        }
        if(ignoreMaim) {
            spawnHeal(player, (toBe - player.getHealth()));
            player.setHealth(toBe);
            return;
        }
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasTag("MaimTag")){
            spawnHeal(player, (toBe - player.getHealth()));
            player.setHealth(toBe);
            return;
        }
        MaimTag maimTag = (MaimTag) client.getTag("MaimTag");
        double gained = toBe - player.getHealth();
        double reduction = maimTag.getReduction();
        double reduceBy = gained * reduction;
        double netHealth = toBe - reduceBy;
        double netGained = netHealth - player.getHealth();
        if(netHealth <= player.getHealth()) return;
        player.setHealth(netHealth);
        spawnHeal(player, netGained);
    }

    public void spawnHeal(Player player, double toGain){
        double amount = Math.round(toGain*100.0)/100.0;
        int time = 5990;
        Item item = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.LIME_DYE));
        item.setPickupDelay(time);
        item.setTicksLived(time);
        item.setCustomName(ChatColor.GREEN + "+" + amount + "");
        item.setCustomNameVisible(true);
    }

    public double handleSwordDamage(Player damager, Player target) {
        Material type = getInHand(damager).getType();
        if (type == Material.GOLDEN_SWORD) {
            target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 10);
            return 4;
        }
        if (type == Material.NETHERITE_SWORD) {
//            target.getWorld().playSound(target.getLocation(), Sound.ITEM_TRIDENT_HIT, 1, 1);
            return 3.5;
        }
        if (type == Material.DIAMOND_SWORD) {
//            target.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 10);
            return 3;
        }
        if (type == Material.IRON_SWORD) {
//            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1, 10);
            return 2;
        }
        if (type == Material.STONE_SWORD) {
            return 1.5;
        }
        return 1;
    }

    public String getPhysicalDamageCause(Player damager) {
        ItemStack itemStack = damager.getInventory().getItemInMainHand();
        if (itemStack == null) {
            return "Hood Style: These Hands No Jutsu";
        }
        if (itemStack.getType() == Material.AIR) {
            return "Hood Style: These Hands No Jutsu";
        }
        return UtilString.fixItemCapitalisation(itemStack.getType().toString());
    }

    public ItemStack getInHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public Core getInstance() {
        return instance;
    }
}
