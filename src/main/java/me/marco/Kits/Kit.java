package me.marco.Kits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {

    private String name;
    private ChatColor color;
    private ItemStack helmet, chestplate, leggings, boots, sword, axe, extra1, extra2, extra3;

    public Kit(String name, ChatColor color, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
               ItemStack sword, ItemStack axe, ItemStack extra1, ItemStack extra2, ItemStack extra3){
        this.name = name;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.sword = sword;
        this.axe = axe;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.extra3 = extra3;
    }

    public ChatColor getColor(){
        return this.color;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ItemStack getSword() {
        return sword;
    }

    public ItemStack getAxe() {
        return axe;
    }

    public ItemStack getExtra1() {
        return extra1;
    }

    public ItemStack getExtra2() {
        return extra2;
    }

    public ItemStack getExtra3() {
        return extra3;
    }

    public void equip(Player player){
        player.getInventory().clear();
        player.getInventory().setHelmet(getHelmet());
        player.getInventory().setChestplate(getChestplate());
        player.getInventory().setLeggings(getLeggings());
        player.getInventory().setBoots(getBoots());
        player.getInventory().setItem(0, getSword());
        player.getInventory().setItem(1, getAxe());
        player.getInventory().setItem(2, getExtra1());
        player.getInventory().setItem(3, getExtra2());
        player.getInventory().setItem(4, getExtra3());
        player.updateInventory();
    }
}
