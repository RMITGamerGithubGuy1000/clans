package me.marco.Skills.Skills.Rogue.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilMath;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;

public class Backstab extends Skill implements PassiveSkill, PassiveB {

    public Backstab(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "When attacking an enemy from behind, ",
                ChatColor.YELLOW +  "deal " + ChatColor.GREEN + this.formatNumber(getMultiplier(level), 1) + "" + ChatColor.LIGHT_PURPLE + " more damage. "
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

    public double getMultiplier(int level){
        return (1 + (1 * (level * .5)));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player damager = event.getDamager();
        Player target = event.getTarget();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(event.isCancelled()) return;
        if(!damagerClient.hasSkill(this)) return;
        if (UtilMath.getAngle(damager.getLocation().getDirection(), target.getLocation().getDirection()) < 60) {
            int level = getLevel(damager);
            event.setDamage(event.getDamage() * getMultiplier(level));
            UtilParticles.playBlockParticle(target, Material.REDSTONE_BLOCK, true);
            event.setCause(getName());
        }
    }

}
