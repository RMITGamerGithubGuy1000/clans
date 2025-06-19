package me.marco.Client;

import org.bukkit.ChatColor;

public enum ClientRank {
    DEFAULT("Default", ChatColor.YELLOW, 0),
    RECRUIT("Helper", ChatColor.GREEN, 1),
    MEMBER("Moderator", ChatColor.AQUA, 2),
    ADMIN("Admin", ChatColor.RED, 3),
    OWNER("Owner", ChatColor.GOLD, 4);

    private String name;
    private int power;
    private ChatColor chatColor;

    ClientRank(String name, ChatColor chatColor, int power) {
        this.name = name;
        this.power = power;
        this.chatColor = chatColor;
    }

    public String getName(){
        return this.name;
    }

    public int getPower() {
        return power;
    }

    public ChatColor getColour(){
        return this.chatColor;
    }
}
