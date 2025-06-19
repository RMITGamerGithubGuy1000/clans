package me.marco.Skills.Skills.Rogue.Sword;

import me.marco.Base.Core;
import me.marco.Events.Skills.SkillActivateEvent;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Recall extends Skill implements SwordSkill, InteractSkill {

    private HashMap<UUID, RecallData> recallMap = new HashMap<UUID, RecallData>();
    /*
    location
    health
    duration
    scrap item bc shit
     */

    private final double RECALL_TIME = 3.5;

    public Recall(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public void activate(Player player, int level) {
        recall(player);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[0];
    }

    @Override
    public double getCooldown(int level) {
        return 16;
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    private long calculateRecallDuration(int level){
        return (long) (RECALL_TIME + (.5 * level));
    }

    private void recall(Player player){
        UUID pUUID = player.getUniqueId();
        Location pLoc = player.getLocation();
        double pHealth = player.getHealth();
        UtilParticles.drawLine(pLoc, pLoc.add(0, 1, 0), 0.3, 1, 1, 1, 1f);
        RecallData recallData = new RecallData(pHealth, pLoc);
        recallMap.put(pUUID, recallData);
        UtilParticles.drawColourCircle(pLoc, 1, 0, 0, 1, 200, 20, 0.3f);
        new BukkitRunnable(){
            public void run(){
                if(!recallMap.containsKey(pUUID)) return;
                recallMap.remove(pUUID);
                getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() + getInstance().getChat().textColour + " expired", getName());
            }
        }.runTaskLater(getInstance(), calculateRecallDuration(getLevel(player)) * 20);
    }

    @EventHandler
    public void onAbilityActivate(SkillActivateEvent event){
        Player player = event.getActivator();
        UUID pUUID = player.getUniqueId();
        BuildSkill buildSkill = event.getBuildSkill();
        if(buildSkill.getSkill() != this) return;
        if(recallMap.containsKey(player.getUniqueId())){
            event.setCancelled(true);
            Location origin = player.getLocation();
            if(!recallMap.containsKey(pUUID)) return;
            RecallData recallData = recallMap.get(pUUID);
            Location location = recallData.getLoc().add(0, 1, 0);
            location.setPitch(origin.getPitch());
            location.setYaw(origin.getYaw());
            player.teleport(location);
            double recallHealth = recallData.getHealth();
            if(player.getHealth() < recallHealth) getInstance().getUtilPvP().setPvPHealth(player, recallHealth, true);
            recallMap.remove(pUUID);
            player.setFallDistance(0);
            getInstance().getChat().sendModule(player, "You used " + getInstance().getChat().highlightText + getName(), getClassTypeName());
            UtilParticles.drawLine(origin, location, 0.3, 1, 1, 1, 1f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 10);
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
            return;
        }
    }

}
