package me.marco.Skills.Builders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum eClassType {
    WARRIOR("Warrior",
            Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            ChatColor.WHITE, Sound.ENTITY_IRON_GOLEM_HURT, 5, .25),
    MAGE("Mage",
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            ChatColor.GOLD, Sound.ENTITY_WITHER_SHOOT, 20, .175),
    RANGER("Ranger",
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            ChatColor.GRAY, Sound.ITEM_ARMOR_EQUIP_IRON, 5, .2),
    ROGUE("Rogue",
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            ChatColor.RED, Sound.ENTITY_ITEM_PICKUP, 10, .125),
    GUARDIAN("Guardian",
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            ChatColor.DARK_AQUA, Sound.ENTITY_BLAZE_HURT, 1, .3),
    SAMURAI("Druid",
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
            ChatColor.DARK_PURPLE, Sound.ENTITY_IRON_GOLEM_REPAIR, 10, .45);


    private String name;
    private Material helmet, chestplate, leggings, boots;
    private ChatColor colour;
    private Sound sound;
    private float pitch;
    private double resistancePoints;

    eClassType(String name, Material helmet, Material chestplate, Material leggings,
               Material boots, ChatColor colour, Sound sound, float pitch, double resistancePoints){
        this.name = name;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.colour = colour;
        this.sound = sound;
        this.pitch = pitch;
        this.resistancePoints = resistancePoints;
    }

    public String getName(){
        return this.name;
    }

    public ChatColor getColour(){
        return this.colour;
    }

    public Material getHelmet(){
        return this.helmet;
    }

    public Material getChestplate() {
        return chestplate;
    }

    public Material getLeggings() {
        return leggings;
    }

    public Material getBoots() {
        return boots;
    }

    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public double getResistancePoints() {
        return resistancePoints;
    }

    public static eClassType getWearing(Player player){
        ItemStack helm = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        if(helm == null || chestplate == null || leggings == null || boots == null) return null;
        for(eClassType classType : eClassType.values()){
            if(helm.getType() == classType.getHelmet()
                    && chestplate.getType() == classType.getChestplate()
                    && leggings.getType() == classType.getLeggings()
                    && boots.getType() == classType.getBoots()) return classType;
        }
        return null;
    }

}
