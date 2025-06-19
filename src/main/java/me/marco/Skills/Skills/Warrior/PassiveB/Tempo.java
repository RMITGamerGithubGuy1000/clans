package me.marco.Skills.Skills.Warrior.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.BowSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Tags.PvPTag;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

public class Tempo extends Skill implements PassiveSkill, PassiveB {

    public Tempo(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "After killing an enemy, your " + ChatColor.AQUA + "Class Ability ",
                ChatColor.AQUA + "Cooldowns " + ChatColor.YELLOW + "are " + ChatColor.LIGHT_PURPLE + "Reduced" + ChatColor.YELLOW + " by " +
                         ChatColor.GREEN + this.formatNumber(calcCDR(level) * 100, 2) + ChatColor.GREEN + "%" + ChatColor.YELLOW + ". ",
        };
    }

    private double calcCDR(int level){
        return .055 * level;
    }

    private void handleCooldownReduction(Client client, Player player){
        ClassBuild classBuild = client.getActiveBuild();
        if(classBuild == null) return;
        Skill swordSkill = classBuild.hasSwordSkill() ? classBuild.getSwordSkill().getSkill() : null;
        Skill axeSkill = classBuild.hasAxeSkill() ? classBuild.getAxeSkill().getSkill() : null;
        Skill bowSkill = classBuild.hasBowSkill() ? classBuild.getBowSkill().getSkill() : null;
        Skill passiveA = classBuild.hasPassiveA() ? classBuild.getPassiveA().getSkill() : null;
        Skill passiveB = classBuild.hasPassiveB()? classBuild.getPassiveB().getSkill() : null;
        int level = client.getActiveBuild().getSkillLevel(this);
        double redPerc = calcCDR(level);

        if(swordSkill != null && getInstance().getCooldownManager().isCooling(player, swordSkill.getName())){
            getInstance().getCooldownManager().reduceCooldown(player, swordSkill.getName(), redPerc, false);
        }
        if(axeSkill != null && getInstance().getCooldownManager().isCooling(player, axeSkill.getName())){
            getInstance().getCooldownManager().reduceCooldown(player, axeSkill.getName(), redPerc, false);
        }
        if(bowSkill != null && getInstance().getCooldownManager().isCooling(player, bowSkill.getName())){
            getInstance().getCooldownManager().reduceCooldown(player, bowSkill.getName(), redPerc, false);
        }
        if(passiveA != null && getInstance().getCooldownManager().isCooling(player, passiveA.getName())){
            getInstance().getCooldownManager().reduceCooldown(player, passiveA.getName(), redPerc, false);
        }
        if(passiveB != null && getInstance().getCooldownManager().isCooling(player, passiveB.getName())){
            getInstance().getCooldownManager().reduceCooldown(player, passiveB.getName(), redPerc, false);
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
        getInstance().getChat().sendModule(player, getInstance().getChat().highlightText + "Tempo CDR" +
                getInstance().getChat().textColour + ": " + getInstance().getChat().highlightNumber + this.formatNumber((redPerc * 100), 2) + "%" +
                getInstance().getChat().textColour + ".", getClassTypeName());

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
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Client client = getInstance().getClientManager().getClient(dead);
        if (!client.hasPvPTag()) return;
        PvPTag pvptag = client.getPvPTag();
        Player damager = pvptag.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(!damagerClient.hasSkill(this)) return;
        handleCooldownReduction(damagerClient, damager);

    }

}
