package me.marco.Skills.Skills.Warrior.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;

public class Swordsmanship extends Skill implements PassiveSkill, PassiveA {

    public Swordsmanship(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return false;
    }

    @Override
    public String[] getDescription(int level) {
        double multiplier = calculateMultiplier(level);
        return new String[]{
                ChatColor.YELLOW + "Passively deal " + ChatColor.GREEN + this.formatNumber(multiplier, 2) + "" + ChatColor.YELLOW + " more " + ChatColor.LIGHT_PURPLE + "Physical Damage. "
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

    private double calculateMultiplier(int level){
        return (level * .25);
    }

    @EventHandler
    public void onPhysicalDamageEvent(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(!damagerClient.hasSkill(this)) return;
        int level = damagerClient.getActiveBuild().getSkillLevel(this);
        event.setDamage(event.getDamage() + event.getDamage() * calculateMultiplier(level));
    }

}
