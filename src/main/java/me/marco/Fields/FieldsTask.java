package me.marco.Fields;

import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class FieldsTask {

    private Block block;
    private Material toReturnTo;
    private int ticksRequired;
    private int ticksAmounted;

    public FieldsTask(Block block, int ticksRequired){
        this.toReturnTo = block.getType();
        this.block = block;
        this.ticksRequired = ticksRequired;
        this.ticksAmounted = 0;
    }

    public void addTick(){
        this.ticksAmounted++;
    }

    public boolean hasFinished(){
        return this.ticksAmounted >= this.ticksRequired;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getTicksRequired() {
        return ticksRequired;
    }

    public void setTicksRequired(int ticksRequired) {
        this.ticksRequired = ticksRequired;
    }

    public int getTicksAmounted() {
        return ticksAmounted;
    }

    public void setTicksAmounted(int ticksAmounted) {
        this.ticksAmounted = ticksAmounted;
    }

    public boolean runTick() {
        this.addTick();
        return checkFinished();
    }

    private boolean checkFinished() {
        if(this.hasFinished()){
            this.block.setType(this.toReturnTo);
            UtilParticles.playBlockParticle(block.getLocation(), this.toReturnTo, true);
            return true;
        }
        return false;
    }

}
