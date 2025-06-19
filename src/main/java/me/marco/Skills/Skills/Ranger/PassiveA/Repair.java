package me.marco.Skills.Skills.Ranger.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import me.marco.Tags.MiscTags.NoFall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Repair extends Skill implements PassiveSkill, PassiveA {

    private final String REPAIRTAG_NAME = "RepairTag";

    public Repair(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        String numeral = "";
        for(int i = 0; i < level; i++){
            numeral+= "I";
        }
        return new String[]{
                ChatColor.YELLOW + "After taking " + ChatColor.LIGHT_PURPLE + "Physical" + ChatColor.YELLOW + ", "
                    + ChatColor.LIGHT_PURPLE + "Pure" + ChatColor.YELLOW + ", or " +
                    ChatColor.LIGHT_PURPLE + "Arrow Damage" + ChatColor.YELLOW + ", ",
                ChatColor.YELLOW + "after " + ChatColor.GREEN + this.formatNumber(getWaitDuration(level), 1) + "" + ChatColor.YELLOW + " seconds, " +
                ChatColor.YELLOW + "gain " + ChatColor.AQUA + "Regen " + numeral + "" + ChatColor.YELLOW + " for " +
                        ChatColor.GREEN + this.formatNumber(calcRegenTime(level), 1) + "" +
                        ChatColor.YELLOW + " seconds. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 45 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private long getWaitDuration(int level){
        return 25 - (level * 5);
    }

    private int calcRegenLevel(int level){
        return level;
    }

    private double calcRegenTime(int level){
        return 8 - .5*level;
    }

    @EventHandler
    public void onArrowDamage(ArrowDamageEvent event){
        if(event.isCancelled()) return;
        Player target = event.getTarget();
        Client client = getInstance().getClientManager().getClient(target);
        if(!client.hasSkill(this)) return;
        if(getCDManager().isCooling(target, getName())) return;
        int level = client.getActiveBuild().getSkillLevel(this);
        getInstance().getTagManager().addTagRemoveOld(client, new RepairTag(client, REPAIRTAG_NAME, level, calcRegenLevel(level), calcRegenTime(level), getWaitDuration(level), getCooldown(level), getInstance()));
    }

    @EventHandler
    public void onPhysicalDamage(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player target = event.getTarget();
        Client client = getInstance().getClientManager().getClient(target);
        if(!client.hasSkill(this)) return;
        if(getCDManager().isCooling(target, getName())) return;
        int level = client.getActiveBuild().getSkillLevel(this);
        getInstance().getTagManager().addTagRemoveOld(client, new RepairTag(client, REPAIRTAG_NAME, level, calcRegenLevel(level), calcRegenTime(level), getWaitDuration(level), getCooldown(level), getInstance()));
    }

    @EventHandler
    public void onPureDamageEvent(PureDamageEvent event){
        if(event.isCancelled()) return;
        Player target = event.getTarget();
        Client client = getInstance().getClientManager().getClient(target);
        if(!client.hasSkill(this)) return;
        if(getCDManager().isCooling(target, getName())) return;
        int level = client.getActiveBuild().getSkillLevel(this);
        getInstance().getTagManager().addTagRemoveOld(client, new RepairTag(client, REPAIRTAG_NAME, level, calcRegenLevel(level), calcRegenTime(level), getWaitDuration(level), getCooldown(level), getInstance()));
    }

//    @EventHandler
//    public void onPureDamageEvent(PureDamageEvent event){
//        if (event.isCancelled()) return;
//        Player player = event.getTarget();
//        Client client = getInstance().getClientManager().getClient(player);
//        if(!client.hasSkill(this)) return;
//        if(getCDManager().isCooling(player, getName())) return;
//        int level = getLevel(player);
//        if(client.hasTag(REPAIRTAG_NAME)){
//            client.removeTag(REPAIRTAG_NAME);
//        }
//        getInstance().getTagManager().addTagRemoveOld(client, new RepairTag(client, REPAIRTAG_NAME, level, getWaitDuration(level), getCooldown(level), getInstance()));
//    }
}
