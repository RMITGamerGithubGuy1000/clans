package me.marco.Skills.Skills.Guardian.PassiveB;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import me.marco.Client.Client;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.ISkill;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.HashMap;

public class GlacialAxe extends Skill implements PassiveSkill, PassiveB {

    public GlacialAxe(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return false;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "When walking on " + ChatColor.AQUA + "Water" + ChatColor.YELLOW + " it temporarily turns into " +
                        ChatColor.LIGHT_PURPLE + "Ice" + ChatColor.YELLOW + ". ",
                ChatColor.YELLOW + "When walking on " + ChatColor.AQUA + "Lava" + ChatColor.YELLOW + " it temporarily turns into " +
                        ChatColor.LIGHT_PURPLE + "Obsidian" + ChatColor.YELLOW + ". "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 0;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if (!Arrays.stream(ISkill.getAxes).anyMatch(
                material -> player.getInventory().getItemInMainHand().getType() == material)) return;
        cylinder(player);
    }

    public void cylinder(Player player) {
        if(!hasManaSilent(player, 2)) return;
        World world = player.getWorld();
        HashMap<Block, Material> blockMap = new HashMap<Block, Material>();
        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, 0)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, -1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, 1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, -1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, 1)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(2, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(2, -1, 1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(2, -1, -1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(2, -1, 2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(2, -1, -2)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(3, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(3, -1, 1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(3, -1, -1)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(3, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, 0)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(-2, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-2, -1, 1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-2, -1, -1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-2, -1, -2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-2, -1, 2)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(-3, -1, 0)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-3, -1, 1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-3, -1, -1)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, 1)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, 2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, 2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, 2)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, 3)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, 3)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, 3)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, -1)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, -2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, -2)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, -2)), blockMap);

        addToCircle(world.getBlockAt(player.getLocation().add(0, -1, -3)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(1, -1, -3)), blockMap);
        addToCircle(world.getBlockAt(player.getLocation().add(-1, -1, -3)), blockMap);
        if(!blockMap.isEmpty()){
            removeMana(player, 2);
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(blockMap, 5));
        }
    }

    public void addToCircle(Block block, HashMap<Block, Material> blockMap){
        if(getInstance().getBlockChangeManager().isBlockChangeBlock(block)) return;
        Material bType = block.getType();
        if(bType == Material.WATER){
            if(isAbleToFreeze(block)){
                addToMap(block, Material.BLUE_ICE, blockMap);
            }
        }
        if(bType == Material.LAVA){
            if(isAbleToFreeze(block)){
                addToMap(block, Material.CRYING_OBSIDIAN, blockMap);
            }
        }
    }

    public void addToMap(Block block, Material type, HashMap<Block, Material> blockMap){
        blockMap.put(block, type);
    }

    public boolean isAbleToFreeze(Block block){
        return block.getRelative(BlockFace.UP).getType() == Material.AIR;
    }

}
