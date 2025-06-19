package me.marco.Skills.Skills.Guardian.Axe;

import me.marco.Base.Core;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

public class Barrier extends Skill implements AxeSkill, InteractSkill {

    public Barrier(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    private final double BARRIER_DURATION = 4.0;
    private final double BARRIER_RADIUS = 3.5;

    private double calculateBarrierDuration(int level){
        return BARRIER_DURATION + (.5 * level);
    }

    private double calculateBarrierRadius(int level){
        return BARRIER_RADIUS + (.5 * level);
    }

    @Override
    public void activate(Player player, int level) {
        deployBarrier(player, level);
    }

    private void deployBarrier(Player player, int level){
        Location loc = player.getLocation();
//        Item barrier = getInstance().getItemManager().dropItem(loc, Material.SEA_LANTERN, player.getName() + "Barrier", true, null);
//        barrier.setPickupDelay(Integer.MAX_VALUE);
        double timer = calculateBarrierDuration(level);
        player.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_HURT, 1, 0.5f);
        Item barrier = getInstance().getItemManager().createThrowable(
                timer, false, false, false,
                loc, Material.SEA_LANTERN, player.getName() + "Barrier",
                true, null).getItem();

        double radius = calculateBarrierRadius(level);
        new BukkitRunnable(){
            double timer = calculateBarrierDuration(level) * 20;
            public void run(){
                if(timer <= 0 || barrier.isDead() || barrier == null){
                    barrier.remove();
                    this.cancel();
                    return;
                }
                if(timer % 10 == 0) {
                    playParticles(barrier, radius);
                }
                checkEntities(barrier, radius);
                timer--;
            }
        }.runTaskTimer(getInstance(), 0, 1);
    }

    private void playParticles(Item barrier, double radius){
        UtilParticles.drawColourCircle(barrier.getLocation().add(0, 0.5, 0), radius, 180, 0, 240, 240, 240, 1.5f);
        UtilParticles.drawColourCircle(barrier.getLocation().add(0, 1, 0), radius, 180, 0, 240, 240, 240, 1.5f);
        UtilParticles.drawColourCircle(barrier.getLocation().add(0, 1.5, 0), radius, 180, 0, 240, 240, 240, 1.5f);
    }

    private void checkEntities(Item barrier, double radius){
        for(Entity entity : barrier.getNearbyEntities(radius, radius, radius)){
            if(entity instanceof Arrow){
                removeEntity(barrier, entity);
                continue;
            }
            if(entity instanceof Item){
                if(entity.isCustomNameVisible()){
                    removeEntity(barrier, entity);
                    continue;
                }
            }
        }
    }

    private void removeEntity(Item barrier, Entity entity){
        UtilParticles.drawLine(barrier.getLocation(), entity.getLocation(), 0.5, 255, 1, 255, 0.5f);
        entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        entity.remove();
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[0];
    }

    @Override
    public double getCooldown(int level) {
        return 25 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 0;
    }
}
