package me.marco.Skills.Skills.Samurai.Sword;

import me.marco.Base.Core;
import me.marco.CustomEntities.NMSFrog;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SageToads extends Skill implements SwordSkill, InteractSkill {

    public SageToads(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
            return new String[]{
                    ChatColor.YELLOW + "Summon " + ChatColor.GREEN + getFrogCount(level) + "" + ChatColor.AQUA + " Sage Toads" + ChatColor.YELLOW + " in a " + ChatColor.GREEN + this.formatNumber(getRadius(level), 1) + "" + ChatColor.YELLOW + " block radius that ",
                    ChatColor.YELLOW + "pull players towards the " + ChatColor.LIGHT_PURPLE + "Center" + ChatColor.YELLOW + " of the",
                    ChatColor.YELLOW + "circle, and enemies outside the circle are pushed ",
                    ChatColor.YELLOW + "away whilst " + ChatColor.LIGHT_PURPLE + "Physically Damaging" + ChatColor.YELLOW + " them for " + ChatColor.GREEN + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " hearts."
            };
    }


    private int getFrogCount(int level){
        return 6 + level * 2;
    }

    private int getRadius(int level){
        return 2 + level * 2;
    }

    @Override
    public double getCooldown(int level) {
        return 10 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    @Override
    public void activate(Player player, int level) {
        activateSageToads(player, level);
    }

    private void activateSageToads(Player player, int level) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FROG_AMBIENT, 5, 5);
        ArrayList<Frog> frogList = new ArrayList<Frog>();
        Location center = player.getLocation();
        double radius = getRadius(level);
        int numberOfFrogs = getFrogCount(level);

        for (int i = 0; i < numberOfFrogs; i++) {
            double angle = 2 * Math.PI * i / numberOfFrogs;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location spawnLoc = new Location(center.getWorld(), x, center.getY(), z);

            NMSFrog nmsfrog = new NMSFrog(spawnLoc);
            Frog frog = (Frog) nmsfrog.getBukkitEntity();
            frogList.add(frog);
        }
        double radDiv = radius / 2;
        new BukkitRunnable(){
            int toadCounter = 0;
            ArrayList<Player> pList = new ArrayList<Player>();
            public void run(){
                if(toadCounter >= 100){
                    for(Frog frog : frogList){
                        frog.remove();
                    }
                    this.cancel();
                    return;
                }
                if(toadCounter % 10 == 0){
                    for(Frog frog : frogList){
                        frog.getWorld().playSound(frog.getLocation(), Sound.ENTITY_FROG_AMBIENT, 2, 1);
                        for(Entity e : frog.getNearbyEntities(radDiv, radDiv, radDiv)){
                            if(!(e instanceof Player)) continue;
                            Player near = (Player) e;
                            if(!pList.contains(player) && player != near){
                                handlePushPull(center, player, near, frog, level);
                                pList.add(near);
                            }
                        }
                    }
                }
                pList.clear();
                toadCounter+=10;
            }
        }.runTaskTimer(getInstance(), 0, 10);
    }

    private void playSonicEffect(Frog frog, Player target) {
        Location frogLoc = frog.getEyeLocation(); // mouth-level
        Location targetLoc = target.getLocation().add(0, 1, 0); // center of player body

        // Create particle line
        Vector path = targetLoc.toVector().subtract(frogLoc.toVector());
        int steps = 10;
        for (int i = 0; i <= steps; i++) {
            Vector point = frogLoc.toVector().clone().add(path.clone().multiply(i / (double) steps));
            Location particleLoc = point.toLocation(frog.getWorld());
            frog.getWorld().spawnParticle(Particle.SONIC_BOOM, particleLoc, 0);
        }

        // Mouth effect
        frog.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, frogLoc, 5, 0.1, 0.1, 0.1, 0);
    }

    private double getDamage(int level){
        return level * .5;
    }

    private void handlePushPull(Location center, Player owner, Player target, Frog frog, int level) {
        Location targetLoc = target.getLocation();
        Vector toCenter = center.toVector().subtract(targetLoc.toVector());

        double distance = toCenter.length();
        if (distance < 0.01) return;

        // Flatten for horizontal-only motion
        toCenter.setY(0);

        Vector force;

        if (distance < 6) {
            // PULL toward center
            force = center.toVector().subtract(targetLoc.toVector());
        } else {
            // PUSH away from center
            force = targetLoc.toVector().subtract(center.toVector());
        }

        // Flatten force vector again
        force.setY(0);

        // Normalize and apply force magnitude
        force = force.normalize().multiply(0.4); // tweak magnitude here

        // Optional: small vertical boost for visual effect
        force.setY(0.1);

        target.setVelocity(force);
        playSonicEffect(frog, target);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 0));
        getInstance().getServer().getPluginManager().callEvent(new PhysicalDamageEvent(owner, target, getDamage(level),
                getName()));
    }



}
