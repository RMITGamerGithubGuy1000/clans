package me.marco.Items;

import me.marco.Utility.UtilBlock;
import me.marco.Utility.UtilTime;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class iThrowable {

    private Item item;
    private long startTime;
    private double expiryTime;
    private boolean canPickUp, isBlockCollision, isPlayerCollision;

    private final BlockFace[] blockFaces =
            { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
                    BlockFace.SOUTH_EAST, BlockFace.NORTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST };

    public iThrowable(Item item, double expiryTime, boolean canPickUp, boolean isBlockCollision, boolean isPlayerCollision){
        this.item = item;
        this.startTime = System.currentTimeMillis();
        this.expiryTime = expiryTime * 1000;
        this.canPickUp = canPickUp;
        this.isBlockCollision = isBlockCollision;
        this.isPlayerCollision = isPlayerCollision;
        if(!this.canPickUp) item.setPickupDelay(Integer.MAX_VALUE);
    }

    public double getRemaining() {
        return UtilTime.convert((getExpiryTime() + getStartTime()) - System.currentTimeMillis(), UtilTime.TimeUnit.SECONDS, 1);
    }

    public boolean isExpired(){
        return this.item.isDead() || this.item == null || this.getRemaining() <= 0;
    }

    public Item getItem() {
        return item;
    }

    public long getStartTime() {
        return startTime;
    }

    public double getExpiryTime() {
        return expiryTime;
    }

    public Player hasPlayerCollided(){
        for (Entity entity : item.getNearbyEntities(0.2, 0.2, 0.2)){
            if(entity instanceof Player) return (Player) entity;
        }
        return null;
    }

    public Block getBlockCollided(){
        if(UtilBlock.isGrounded(this.item)) return item.getLocation().add(0, -.5, 0).getBlock();
        for(BlockFace blockFace : blockFaces){
            Block block = item.getLocation().getBlock().getRelative(blockFace);
            if(block.getType() != Material.AIR && !UtilBlock.airFoliage(block)) return block;
        }
        Block block = item.getLocation().getBlock();
        if(block != null && block.getType() != Material.AIR) return block;
        return null;
    }

    public boolean canPickup() {
        return canPickUp;
    }

    public boolean canPlayerCollide() { return this.isPlayerCollision; }

    public boolean canBlockCollide() { return this.isBlockCollision; }
}