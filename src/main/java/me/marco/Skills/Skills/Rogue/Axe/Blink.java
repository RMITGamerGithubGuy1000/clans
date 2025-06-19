package me.marco.Skills.Skills.Rogue.Axe;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.Skills.SkillActivateEvent;
import me.marco.Skills.Builders.BuildSkill;
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
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Blink extends Skill implements AxeSkill, InteractSkill {

    public HashMap<UUID, Location> deblinkmap = new HashMap<UUID, Location>();

    public Blink(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract, true);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.AQUA + "Teleport " + ChatColor.GREEN + getRange(level) + "" + ChatColor.YELLOW + " blocks infront of you. " + ChatColor.LIGHT_PURPLE + "Re-Cast " ,
                ChatColor.YELLOW + "this ability within " + ChatColor.GREEN + "3" + ChatColor.YELLOW + " seconds to activate " + ChatColor.AQUA + "De-Blink ",
                ChatColor.AQUA + "teleporting" + ChatColor.YELLOW + " you back to your initial cast location. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 16 - (level * .5);
    }

    public int getRange(int level){
        return 6 + (2 * level);
    }

    @Override
    public int getMana(int level) {
        return 15 - level;
    }

    @Override
    public void activate(Player player, int level) {
        Location origin = player.getLocation();

        BlockIterator iterator = new BlockIterator(player.getWorld(), player
                .getLocation().add(0, 1, 0).toVector(), player.getEyeLocation()
                .getDirection(), 0, getRange(getLevel(player)));

        Block block = player.getLocation().getBlock();

        while(iterator.hasNext()){
            Block checkBlock = iterator.next();
            if(checkBlock.getType().isOccluding() && !checkBlock.isPassable()){
                break;
            }
            block = checkBlock;
        }
        // Destination
        Location toTele = block.getLocation().add(0, 0.5, 0);
        toTele.setPitch(origin.getPitch());
        toTele.setYaw(origin.getYaw());
        player.teleport(toTele);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
        player.setFallDistance(0);

        UtilParticles.drawLine(origin, toTele, 0.3, 1, 1, 1, 1f);

        deblinkmap.put(player.getUniqueId(), origin);

        new BukkitRunnable(){
            int c = 12;
            public void run(){
                if(c <= 0){
                    deblinkmap.remove(player.getUniqueId());
                    this.cancel();
                }else{
                    player.setFallDistance(0);
                    c--;
                }
            }
        }.runTaskTimer(getInstance(), 0, 5);
    }

    @EventHandler
    public void onAbilityActivate(SkillActivateEvent event){
        Player player = event.getActivator();
        BuildSkill buildSkill = event.getBuildSkill();
        if(buildSkill.getSkill() != this) return;
        if(deblinkmap.containsKey(player.getUniqueId())){
            event.setCancelled(true);
            Location origin = player.getLocation();
            Location location = deblinkmap.get(player.getUniqueId()).clone();
            location.setPitch(origin.getPitch());
            location.setYaw(origin.getYaw());
            player.teleport(location);
            deblinkmap.remove(player.getUniqueId());
            player.setFallDistance(0);
            getInstance().getChat().sendModule(player, "You used " + getInstance().getChat().highlightText + "De-Blink", getClassTypeName());
            UtilParticles.drawLine(origin, location, 0.3, 1, 1, 1, 1f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
            new BukkitRunnable(){
                int c = 4;
                public void run(){
                    if(c <= 0){
                        this.cancel();
                    }else{
                        player.setFallDistance(0);
                        c--;
                    }
                }
            }.runTaskTimer(getInstance(), 0, 5);
        }
    }

}
