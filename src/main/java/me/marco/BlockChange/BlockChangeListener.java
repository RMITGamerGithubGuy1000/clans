package me.marco.BlockChange;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockChangeListener extends CListener<Core> {

    public BlockChangeListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onBlockChangeBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(block)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(event.getToBlock())) return;
        event.setCancelled(true);
    }

}
