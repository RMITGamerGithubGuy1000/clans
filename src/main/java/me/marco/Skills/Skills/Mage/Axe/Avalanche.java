package me.marco.Skills.Skills.Mage.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Avalanche extends ChannelSkill implements AxeSkill, InteractSkill {

    public Avalanche(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }


    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Cast an " + ChatColor.AQUA + "Avalanche" + ChatColor.YELLOW + " that inflicts " + ChatColor.AQUA + "Slow I" + ChatColor.YELLOW + " and ",
                ChatColor.YELLOW + "deals " + ChatColor.GREEN + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " damage to players in a range of ",
                ChatColor.GREEN + this.formatNumber(getRadius(level), 1) + "" + ChatColor.YELLOW + " blocks as well as " + ChatColor.LIGHT_PURPLE + "stopping their Sprint" + ChatColor.YELLOW + ". "
        };
    }


    @Override
    public float requiredEnergy(int level) {
       // return .0125f
        return (float) (.0215 - (level * .003));
    }

    @Override
    public float requiredReserve(int level) {
        return (float) (.3 - level * .05);
    }

    @Override
    public double toggleCooldown(int level) {
        return 2;
    }

    @Override
    public int getTicks() {
        return 10;
    }

    private double getRadius(int level){
        return 3 + level;
    }

    private double getDamage(int level){
        return .25 * level;
    }

    @Override
    public boolean runTick(Player player) {
        int level = getLevel(player);
        double radius = getRadius(level);
        UtilParticles.drawCircleWithVector(player.getLocation().add(0, radius, 0), Particle.CLOUD, radius, 0, 0, 1.2f, new Vector(0, -.2, 0));
        UtilParticles.playBlockParticle(player, Material.SNOW, true);
        Client playerClient = getInstance().getClientManager().getClient(player);
        for(Entity entities : player.getNearbyEntities(radius, radius, radius)){
            if(entities instanceof Player){
                Player nearby = (Player) entities;
                Client entityClient = getInstance().getClientManager().getClient(nearby);
                if(entityClient.isFriendly(playerClient)){
                    continue;
                }
                if(getInstance().getLandManager().isInSafezone(nearby)){
                    continue;
                }
                getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(nearby, player, PotionEffectType.SLOWNESS, 60, 0));
                getInstance().getServer().getPluginManager().callEvent(new PhysicalDamageEvent(player, nearby, getDamage(level), getName()));
                UtilParticles.playBlockParticle(nearby.getLocation(), Material.SNOW_BLOCK);

                nearby.setSprinting(false);
            }
        }
        return true;
    }

    @Override
    public void cleanup(Player player) {

    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }

    @Override
    public double getCooldown(int level) {
        return 20;
    }

    @Override
    public int getMana(int level) {
        return 30;
    }

    public void activate(Player player, int level) {
        castChannelAbility(player, getLevel(player));
        toggleChannel(player);
    }

}
