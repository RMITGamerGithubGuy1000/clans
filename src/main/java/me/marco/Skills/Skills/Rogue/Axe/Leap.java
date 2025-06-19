package me.marco.Skills.Skills.Rogue.Axe;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilBlock;
import me.marco.Utility.UtilParticles;
import me.marco.Utility.UtilVelocity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Leap extends Skill implements AxeSkill, InteractSkill {

    public Leap(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract, true);
    }

    @Override
    public boolean useageCheck(Player player) {
        if(player.hasPotionEffect(PotionEffectType.SLOWNESS)){
            getInstance().getChat().sendModule(player,
                    "You can not use " + ChatColor.GOLD + getName() + "" + getInstance().getChat().textColour +
                            " whiled " + getInstance().getChat().highlightText + "Slowed", getName());
            return false;
        }
        if(wallKick(player)){
            return false;
        }
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Launches you forward at a high velocity. " + ChatColor.LIGHT_PURPLE + "Cast ",
                ChatColor.YELLOW + "this ability with your back to a wall to perform ",
                ChatColor.YELLOW + "a " + ChatColor.AQUA + "Wallkick" + ChatColor.YELLOW + " (a weaker " + ChatColor.AQUA + "Leap" + ChatColor.YELLOW + " with no cooldown)"
        };
    }

    @Override
    public double getCooldown(int level) {
        return 14 - (level * .5);
    }

    @Override
    public int getMana(int level) {
        return 15 - level;
    }

    @Override
    public void activate(Player player, int level) {
        Block block = getBlockBehind(player);
        UtilParticles.playBlockParticle(player, Material.WHITE_WOOL, true);
        launchPlayer(player, 1.4);
    }

    public static Block getBlockBehind(Player player){
        Location location = player.getLocation().add(0, 1, 0);
        Vector direction = location.getDirection().multiply(new Vector(-1, 0, -1));
        return player.getWorld().getBlockAt(location.add(direction));
    }

    public boolean wallKick(Player player) {
        if(!this.getCDManager().add(player, "Wallkick", this.getClassTypeName(), 0, .25, false)) return false;
            Vector vec = player.getLocation().getDirection();

            boolean xPos = true;
            boolean zPos = true;

            if (vec.getX() < 0.0D) {
                xPos = false;
            }
            if (vec.getZ() < 0.0D) {
                zPos = false;
            }

            for (int y = 0; y <= 0; y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if ((x != 0) || (z != 0)) {
                            if (((!xPos) || (x <= 0))
                                    && ((!zPos) || (z <= 0))
                                    && ((xPos) || (x >= 0)) && ((zPos) || (z >= 0))) {
                                Block back = player.getLocation().getBlock().getRelative(x, y, z);
                                if (!UtilBlock.airFoliage(back)) {
                                    if (back.getLocation().getY() == Math.floor(player.getLocation().getY())
                                            || back.getLocation().getY() == Math.floor(player.getLocation().getY() - 0.25)) {
                                        if (back.getRelative(BlockFace.UP).getType() == Material.AIR) {
                                            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                                                continue;
                                            }
                                        }
                                    }
                                    Block forward = null;

                                    if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                        if (xPos) {
                                            forward = player.getLocation().getBlock().getRelative(1, 0, 0);
                                        } else {
                                            forward = player.getLocation().getBlock().getRelative(-1, 0, 0);
                                        }

                                    } else if (zPos) {
                                        forward = player.getLocation().getBlock().getRelative(0, 0, 1);
                                    } else {
                                        forward = player.getLocation().getBlock().getRelative(0, 0, -1);
                                    }

                                    if (UtilBlock.airFoliage(forward)) {
                                        if (Math.abs(vec.getX()) > Math.abs(vec.getZ())) {
                                            if (xPos) {
                                                forward = player.getLocation().getBlock().getRelative(1, 1, 0);
                                            } else {
                                                forward = player.getLocation().getBlock().getRelative(-1, 1, 0);
                                            }
                                        } else if (zPos) {
                                            forward = player.getLocation().getBlock().getRelative(0, 1, 1);
                                        } else {
                                            forward = player.getLocation().getBlock().getRelative(0, 1, -1);
                                        }

                                        if (UtilBlock.airFoliage(forward)) {
                                            if(hasMana(player, 5, "Wallkick")){
                                                if(player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
                                                    getInstance().getChat().sendModule(player,
                                                            "You can not use " + ChatColor.GOLD + "Wallkick" + getInstance().getChat().textColour +
                                                                    " whiled " + getInstance().getChat().highlightText + "Slowed", getName());
                                                    return true;
                                                }
                                                UtilParticles.playBlockParticle(player, Material.WHITE_WOOL, true);
                                                getInstance().getChat().sendUseage(player, "Wallkick", getClassTypeName());
                                                wallkickPlayer(player, 1);
                                                removeMana(player, 5);
                                                return true;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return false;
    }

    public void launchPlayer(Player player, double multiply){
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(multiply);
        tosend.setY(tosend.getY() + .1);
        player.setVelocity(tosend);
    }

    public void wallkickPlayer(Player player, double strength){
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(strength);
//        UtilVelocity.velocity(player, vec, strength, false, 0.0D, 0.8D, 2.0D, true);
        player.setVelocity(tosend);
    }

    public boolean isKickable(Block block){
        return !block.isLiquid() && !block.isPassable();
    }

}
