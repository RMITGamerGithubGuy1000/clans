package me.marco.Skills.Skills.Rogue.Sword;

import org.bukkit.Location;

public class RecallData {

    private double health;
    private Location loc;

    public RecallData(double health, Location loc){
        this.health = health;
        this.loc = loc;
    }

    public double getHealth() {
        return health;
    }

    public Location getLoc() {
        return loc;
    }
}
