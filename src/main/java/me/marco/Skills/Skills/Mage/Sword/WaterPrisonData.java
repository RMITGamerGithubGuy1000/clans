package me.marco.Skills.Skills.Mage.Sword;

import me.marco.BlockChange.BlockChange;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WaterPrisonData {

    private Player target;
    private Location toPullTo;
    private int count = 0;
    private int trapCount = 0;
    private final int toTrapCountTo = 2;
    private final int toCountTo = 4;
    private BlockChange blockChange;

    public WaterPrisonData(Player target, Location toPullTo){
        this.target = target;
        this.toPullTo = toPullTo;
    }

    private void addCount(){
        this.count++;
    }

    private boolean canDamage(){
        return this.count >= toCountTo;
    }

    public boolean checkDamage(){
        addCount();
        if(canDamage()){
            this.count = 0;
            return true;
        }
        return false;
    }

    public void wipePrison(){
        this.blockChange.setExpired();
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Location getToPullTo() {
        return toPullTo;
    }

    public void setToPullTo(Location toPullTo) {
        this.toPullTo = toPullTo;
    }

    public void setBlockChange(BlockChange blockChange) {
        this.blockChange = blockChange;
    }

    public boolean isBlock(Block block) {
        return this.blockChange.containsBlock(block);
    }
}
