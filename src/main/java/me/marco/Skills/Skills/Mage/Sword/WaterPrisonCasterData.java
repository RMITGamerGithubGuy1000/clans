package me.marco.Skills.Skills.Mage.Sword;

import org.bukkit.entity.Player;

public class WaterPrisonCasterData {

    private Player target;
    private int count = 0;
    private final int toCountTo = 2;

    public WaterPrisonCasterData(Player target){
        this.target = target;
    }

    public boolean canMakePrison(){
        this.count++;
        return this.count >= this.toCountTo;
    }

    public Player getTarget() {
        return target;
    }

    public int getCount() {
        return count;
    }

    public int getToCountTo() {
        return toCountTo;
    }
}
