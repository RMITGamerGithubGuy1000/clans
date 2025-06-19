package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockChangeManager {

    private Core instance;

    public BlockChangeManager(Core instance){
        this.instance = instance;
    }

    private List<BlockChange> blockChangeList = new ArrayList<BlockChange>();

    public void handleBlockChanges(){
        List<BlockChange> toRemove = new ArrayList<BlockChange>();
        this.blockChangeList.forEach(blockChange -> {
            if(blockChange.isExpired()){
                blockChange.reset();
                toRemove.add(blockChange);
            }
        });
        toRemove.forEach(blockChange -> blockChangeList.remove(blockChange));
    }

    public void addBlockChange(BlockChange blockChange){
        this.blockChangeList.add(blockChange);
    }

    public Core getInstance() {
        return instance;
    }

    public boolean isBlockChangeBlock(Block block) {
        for(BlockChange blockChange : this.blockChangeList){
            if(blockChange.hasBlock(block)) return true;
        }
        return false;
    }
}
