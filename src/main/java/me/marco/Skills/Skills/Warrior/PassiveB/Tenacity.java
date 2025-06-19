package me.marco.Skills.Skills.Warrior.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;

public class Tenacity extends Skill implements PassiveSkill, PassiveB {

    public Tenacity(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "When reaching below " + ChatColor.GREEN + "10" + ChatColor.YELLOW + " health gain " + ChatColor.AQUA + "Regen II ",
                ChatColor.YELLOW + "for " + ChatColor.GREEN + this.formatNumber(getDuration(level), 1) + "" + ChatColor.YELLOW + " seconds. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 35 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private double getDuration(int level){
        return 2 + level * 2;
    }

    @EventHandler
    public void onPhysicalDamageEvent(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player target = event.getTarget();
        Client targetClient = getInstance().getClientManager().getClient(target);
        if(!targetClient.hasSkill(this)) return;
        double damage = event.getDamage();
        if(target.getHealth() - damage > 10) return;
        if(!getInstance().getCooldownManager().isCooling(target, getName())){
            int level = targetClient.getActiveBuild().getSkillLevel(this);
            getInstance().getCooldownManager().add(target, getName(), getClassTypeName(), level, getCooldown(level), true);
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(target, target, PotionEffectType.REGENERATION, (int) getDuration(level) * 20, 1));
            UtilParticles.playBlockParticle(target.getLocation().add(0, 1, 0), Material.CHERRY_LEAVES, true);
            target.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            UtilParticles.playInstantBreak(target.getLocation(), PotionEffectType.INSTANT_HEALTH);
        }
    }

}
