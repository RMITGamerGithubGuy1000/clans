package me.marco.BlockChange;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;

public class BlockChange {

    private HashMap<Block, Material> blockMap = new HashMap<Block, Material>();
    private HashMap<Block, BlockData> dataMap = new HashMap<Block, BlockData>();
    private long timeStarted;
    private double duration;
    private boolean isForceExpired = false;

    public BlockChange(HashMap<Block, Material> blockMap, double duration){
        this.blockMap = blockMap;
        this.timeStarted = System.currentTimeMillis();
        this.duration = duration;
        setBlocks();
    }

    public BlockChange(Block block, Material type, double duration){
        this.blockMap = new HashMap<Block, Material>();
        this.blockMap.put(block, type);
        this.timeStarted = System.currentTimeMillis();
        this.duration = duration;
        setBlocks();
    }

    public void setBlocks(){
        for(Block block : blockMap.keySet()){
            dataMap.put(block, block.getBlockData());
            block.setType(blockMap.get(block));
        }
    }

    public void reset(){
        for(Block block : dataMap.keySet()){
            block.setBlockData(dataMap.get(block));
        }
    }

    public HashMap<Block, Material> getBlockList() {
        return blockMap;
    }

    public HashMap<Block, BlockData> getDataMap() {
        return dataMap;
    }

    public HashMap<Block, Material> getBlockMap() {
        return blockMap;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isExpired(){
        if(isForceExpired) return true;
        return System.currentTimeMillis() >= this.getTimeStarted() + this.getDuration() * 1000;
    }

    public boolean hasBlock(Block block) {
        return this.blockMap.keySet().contains(block);
    }

    public void setExpired() {
        this.isForceExpired = true;
    }

    public boolean containsBlock(Block block) {
        return this.blockMap.containsKey(block);
    }
}
