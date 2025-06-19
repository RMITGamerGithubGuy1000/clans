package me.marco.Skills.Skills.Samurai.Axe;

import me.marco.Base.Core;
import me.marco.CustomEntities.NMSScout;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;

public class Scout extends Skill implements AxeSkill, InteractSkill {

    public Scout(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Summon " + ChatColor.AQUA + "Wolves" + ChatColor.YELLOW + " that chase after nearby enemies ",
                ChatColor.YELLOW + "in a " + ChatColor.GREEN + getRadius(level) + "" + ChatColor.YELLOW + " block radius. The " + ChatColor.AQUA + "Wolves" + ChatColor.YELLOW + " then pounce on their ",
                ChatColor.YELLOW + "targets when they are close by, inflicting " + ChatColor.GREEN + "2" + ChatColor.YELLOW + " hearts ",
                ChatColor.LIGHT_PURPLE + "Physical Damage" + ChatColor.YELLOW + " and giving them " + ChatColor.AQUA + "Slow II" + ChatColor.YELLOW + " for " + ChatColor.GREEN + "5" + ChatColor.YELLOW + " seconds. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 10 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    private int getRadius(int level){
        return 15 + level * 5;
    }

    @Override
    public void activate(Player player, int level) {
        activateScout(player, level);
    }

    private void activateScout(Player player, int level){
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);
        double radius = getRadius(level);
        for(Entity entity : player.getNearbyEntities(radius, radius, radius)){
            if(!(entity instanceof Player)) continue;
            Player nearby = (Player) entity;
            NMSScout nmsScout = new NMSScout(getInstance(), player, player.getLocation(), nearby);
            Wolf wolf = (Wolf) nmsScout.getBukkitEntity();
            wolf.setCustomNameVisible(true);
            wolf.setCustomName(ChatColor.AQUA + player.getName() + " Wolf");
        }
    }

}
