package me.marco.Kits;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class KitManager {

    private Core instance;

    public KitManager(Core instance){
        this.instance = instance;
    }

    private HashMap<String, Kit> kitmap = new HashMap<String, Kit>();

    public void initialise(){
        ItemStack goldSword = getInstance().getUtilItem().createItem(ChatColor.YELLOW + "Radiant Sword",
                Arrays.asList(ChatColor.YELLOW + "A Shining Sword That Deals Immense Damage"), Material.GOLDEN_SWORD);
        ItemStack goldAxe = getInstance().getUtilItem().createItem(ChatColor.YELLOW + "Radiant Axe",
                Arrays.asList(ChatColor.YELLOW + "A Radiant Axe"), Material.GOLDEN_AXE);
        ItemStack rangerBow = getInstance().getUtilItem().createItem(ChatColor.GOLD + "Ranger's Bow", Arrays.asList(ChatColor.YELLOW + "A Ranger's Bow"),
                Material.BOW);
        this.kitmap.put("warrior", new Kit("Warrior", ChatColor.WHITE,
                getInstance().getUtilItem().createItem("Warrior's Helmet", Arrays.asList(ChatColor.YELLOW + "A Warrior's Helmet"), Material.IRON_HELMET),
                getInstance().getUtilItem().createItem("Warrior's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Warrior's Chestplate"), Material.IRON_CHESTPLATE),
                getInstance().getUtilItem().createItem("Warrior's Leggings", Arrays.asList(ChatColor.YELLOW + "A Warrior's Leggings"), Material.IRON_LEGGINGS),
                getInstance().getUtilItem().createItem("Warrior's Boots", Arrays.asList(ChatColor.YELLOW + "A Warrior's Boots"), Material.IRON_BOOTS),
                goldSword, goldAxe, null, null, null
        ));
        this.kitmap.put("guardian", new Kit("Guardian", ChatColor.DARK_AQUA,
                getInstance().getUtilItem().createItem(ChatColor.DARK_AQUA + "Guardian's Helmet", Arrays.asList(ChatColor.YELLOW + "A Guardian's Helmet"),
                        Material.DIAMOND_HELMET),
                getInstance().getUtilItem().createItem(ChatColor.DARK_AQUA + "Guardian's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Guardian's Chestplate"),
                        Material.DIAMOND_CHESTPLATE),
                getInstance().getUtilItem().createItem(ChatColor.DARK_AQUA + "Guardian's Leggings", Arrays.asList(ChatColor.YELLOW + "A Guardian's Leggings"),
                        Material.DIAMOND_LEGGINGS),
                getInstance().getUtilItem().createItem(ChatColor.DARK_AQUA + "Guardian's Boots", Arrays.asList(ChatColor.YELLOW + "A Guardian's Boots"),
                        Material.DIAMOND_BOOTS),
                goldSword, goldAxe, null, null, null
        ));
        this.kitmap.put("mage", new Kit("Mage", ChatColor.GOLD,
                getInstance().getUtilItem().createItem(ChatColor.GOLD + "Mage's Helmet", Arrays.asList(ChatColor.YELLOW + "A Mage's Helmet"),
                        Material.GOLDEN_HELMET),
                getInstance().getUtilItem().createItem(ChatColor.GOLD + "Mage's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Mage's Chestplate"),
                        Material.GOLDEN_CHESTPLATE),
                getInstance().getUtilItem().createItem(ChatColor.GOLD + "Mage's Leggings", Arrays.asList(ChatColor.YELLOW + "A Mage's Leggings"),
                        Material.GOLDEN_LEGGINGS),
                getInstance().getUtilItem().createItem(ChatColor.GOLD + "Mage's Boots", Arrays.asList(ChatColor.YELLOW + "A Mage's Boots"),
                        Material.GOLDEN_BOOTS),
                goldSword, goldAxe, null, null, null
        ));
        this.kitmap.put("ranger", new Kit("Ranger", ChatColor.GRAY,
                getInstance().getUtilItem().createItem(ChatColor.GRAY + "Ranger's Helmet", Arrays.asList(ChatColor.YELLOW + "A Ranger's Helmet"),
                        Material.CHAINMAIL_HELMET),
                getInstance().getUtilItem().createItem(ChatColor.GRAY + "Ranger's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Ranger's Chestplate"),
                        Material.CHAINMAIL_CHESTPLATE),
                getInstance().getUtilItem().createItem(ChatColor.GRAY + "Ranger's Leggings", Arrays.asList(ChatColor.YELLOW + "A Ranger's Leggings"),
                        Material.CHAINMAIL_LEGGINGS),
                getInstance().getUtilItem().createItem(ChatColor.GRAY + "Ranger's Boots", Arrays.asList(ChatColor.YELLOW + "A Ranger's Boots"),
                        Material.CHAINMAIL_BOOTS),
                goldSword, goldAxe, rangerBow, new ItemStack(Material.ARROW, 64), new ItemStack(Material.ARROW, 64)
        ));
        this.kitmap.put("druid", new Kit("Druid", ChatColor.DARK_PURPLE,
                getInstance().getUtilItem().createItem(ChatColor.DARK_PURPLE + "Druid's Helmet", Arrays.asList(ChatColor.YELLOW + "A Ranger's Helmet"),
                        Material.NETHERITE_HELMET),
                getInstance().getUtilItem().createItem(ChatColor.DARK_PURPLE + "Druid's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Ranger's Chestplate"),
                        Material.NETHERITE_CHESTPLATE),
                getInstance().getUtilItem().createItem(ChatColor.DARK_PURPLE + "Druid's Leggings", Arrays.asList(ChatColor.YELLOW + "A Ranger's Leggings"),
                        Material.NETHERITE_LEGGINGS),
                getInstance().getUtilItem().createItem(ChatColor.DARK_PURPLE + "Druid's Boots", Arrays.asList(ChatColor.YELLOW + "A Ranger's Boots"),
                        Material.NETHERITE_BOOTS),
                goldSword, goldAxe, null, null, null
        ));
        this.kitmap.put("rogue", new Kit("Rogue", ChatColor.RED,
                getInstance().getUtilItem().createItem(ChatColor.RED + "Rogue's Helmet", Arrays.asList(ChatColor.YELLOW + "A Rogue's Helmet"),
                        Material.LEATHER_HELMET),
                getInstance().getUtilItem().createItem(ChatColor.RED + "Rogue's Chestplate", Arrays.asList(ChatColor.YELLOW + "A Rogue's Chestplate"),
                        Material.LEATHER_CHESTPLATE),
                getInstance().getUtilItem().createItem(ChatColor.RED + "Rogue's Leggings", Arrays.asList(ChatColor.YELLOW + "A Rogue's Leggings"),
                        Material.LEATHER_LEGGINGS),
                getInstance().getUtilItem().createItem(ChatColor.RED + "Rogue's Boots", Arrays.asList(ChatColor.YELLOW + "A Rogue's Boots"),
                        Material.LEATHER_BOOTS),
                goldSword, goldAxe, null, null, null
        ));
    }

    public boolean isKit(String kitName){
        return this.kitmap.containsKey(kitName);
    }

    public Kit getKit(String kitName){
        if(!isKit(kitName)) return null;
        return this.kitmap.get(kitName);
    }

    public Core getInstance() {
        return instance;
    }

}
