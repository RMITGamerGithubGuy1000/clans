package me.marco.PvPItems.Consumeables.WebGrenade;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import me.marco.Client.Client;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class WebGrenadeTag extends ItemTask {

    private Item webGrenade;
    private final int toCountTo = 3;
    private int count = 0;

    public WebGrenadeTag(Client owner, Item webGrenade, Core instance) {
        super(owner, "WebGrenadeTag", 0, false, instance);
        this.webGrenade = webGrenade;
    }

    public boolean isDeadClause(){
        return this.webGrenade.isDead();
    }

    public boolean checkExpiry(){
        return this.webGrenade.isOnGround() || this.webGrenade.isDead();
    }

    @Override
    public void onTick() {
        if(count >= toCountTo){
            count = 0;
            playEffect();
            return;
        }
        count++;
    }

    public void burstGrenade(){
        if(webGrenade.isDead()) return;
        Block centerBlock = webGrenade.getLocation().getBlock();
        UtilParticles.playPotionBreak(webGrenade.getLocation(), PotionEffectType.INVISIBILITY);
        webGrenade.remove();
        HashMap<Block, Material> blockMap = new HashMap<Block, Material>();
        addToMap(centerBlock.getLocation().getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(1, 0, 0).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(-1, 0, 0).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(0, 0, 1).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(0, 0, -1).getBlock(), blockMap);

        addToMap(centerBlock.getLocation().add(1, 0, -1).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(-1, 0, -1).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(-1, 0, 1).getBlock(), blockMap);
        addToMap(centerBlock.getLocation().add(1, 0, 1).getBlock(), blockMap);


        addToMap(centerBlock.getLocation().add(0, 1, 0).getBlock(), blockMap);

        getInstance().getBlockChangeManager().addBlockChange(new BlockChange(blockMap, 5));
    }

    public void addToMap(Block block, HashMap<Block, Material> blockMap){
        if(block.getType() != Material.AIR) return;
        addToMap(block, Material.COBWEB, blockMap);
    }

    public void addToMap(Block block, Material type, HashMap<Block, Material> blockMap){
        blockMap.put(block, type);
    }

    public void playEffect(){
        UtilParticles.playBlockParticle(webGrenade.getLocation(), Material.COBWEB, true);
    }

    @Override
    public void onExpiry() {
        burstGrenade();
    }
}
