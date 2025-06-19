package me.marco.Skills.Skills.Ranger.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;

public class HealingArrow extends Skill implements PassiveSkill, PassiveA {

    public HealingArrow(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return false;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Gain " + ChatColor.GREEN +
                        this.formatNumber(getHealthGain(level), 1) + " " +
                        ChatColor.AQUA + "Lifesteal" + ChatColor.YELLOW + " from your " +
                        ChatColor.AQUA + "Arrows" + ChatColor.YELLOW + ". ",
        };
    }

    @EventHandler
    public void onArrowShoot(ArrowDamageEvent event){
        if(event.isCancelled()) return;
        Player shooter = event.getShooter();
        Client shooterClient = getInstance().getClientManager().getClient(shooter);
        if(!shooterClient.hasSkill(this)) return;
        if(shooter.getHealth() >= 20) return;
        int level = shooterClient.getActiveBuild().getSkillLevel(this);
        double calcGain = getHealthGain(level) * event.getDamage();
        double toBe = shooter.getHealth() + calcGain;
        if(toBe > 20) toBe = 20;
        getInstance().getUtilPvP().setPvPHealth(shooter, toBe);
        shooter.getWorld().spawnParticle(Particle.WITCH, shooter.getLocation().add(0, 2, 0), 3);
        shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    private double getHealthGain(int level){
        return .15 * level;
    }

    @Override
    public double getCooldown(int level) {
        return 0;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }


}
