package me.marco.Skills.Skills.Ranger.Bow;

import org.bukkit.ChatColor;

public enum MagicArrowType {

    FIRE(ChatColor.RED.toString() + "Fire", ChatColor.RED + "Fire Arrows"),
    SLOW(ChatColor.DARK_PURPLE.toString() + "Slow", ChatColor.DARK_PURPLE + "Slow Arrows"),
    LONGSHOT(ChatColor.WHITE.toString() + "Longshot", ChatColor.WHITE + "Longshot Arrows");

    private String name;
    private String displayName;

    MagicArrowType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName(){
        return this.name;
    }

    public String getDisplayName(){
        return this.displayName;
    }

}
