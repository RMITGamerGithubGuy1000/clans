package me.marco.Skills.Skills.Warrior.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BullsCharge extends Skill implements AxeSkill, InteractSkill {

    private HashMap<Player, Integer> chargeMap = new HashMap<Player, Integer>();
    private final double BULLS_CHARGE_TIME = 3.0;

    public BullsCharge(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract, true);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        double duration = calcBullsChargeDuration(level) / 20;
        return new String[]{
                ChatColor.YELLOW + "Activate to gain" + ChatColor.AQUA + " Speed II " + ChatColor.YELLOW + "for " + ChatColor.GREEN + this.formatNumber(duration, 1) + "",
                ChatColor.YELLOW + "seconds and " + ChatColor.AQUA + "Slow" + ChatColor.YELLOW + " your next hit ",
                ChatColor.YELLOW + "target for " + ChatColor.GREEN + this.formatNumber(duration, 1) + "" + ChatColor.YELLOW + " seconds.",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 16 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    private int calcBullsChargeDuration(int level){
        return (int) Math.round((BULLS_CHARGE_TIME + (level * .5)) * 20);
    }

    @Override
    public void activate(Player player, int level) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
        UtilParticles.playBlockParticle(player.getLocation().add(0, 1, 0), Material.OBSIDIAN);
//        Client client = getInstance().getClientManager().getClient(player);
        int duration = calcBullsChargeDuration(level);
//        player.addPotionEffect(PotionEffectType.SPEED, duration, 1);
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.SPEED, duration, 1));
        chargeMap.put(player, level);
        new BukkitRunnable(){
            public void run(){
                if(!chargeMap.containsKey(player)) return;
                chargeMap.remove(player);
                getInstance().getChat().sendClans(player, "Your " + getChat().highlightText + "Bulls Charge" + getChat().textColour + " has expired");
                UtilParticles.playPotionBreak(player.getLocation(), PotionEffectType.FIRE_RESISTANCE);
            }
        }.runTaskLater(getInstance(), duration);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player target = event.getTarget();
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);

        if(!damagerClient.hasSkill(this)) return;
        if(!chargeMap.containsKey(damager)) return;

        UtilParticles.playBlockParticle(target.getLocation(), Material.OBSIDIAN);
        damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.5F, 0.0F);
        damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.5F, 0.5F);
        getChat().sendClans(target, "You were pinned by " + getChat().getClanRelation(target, damager) + damager.getName());
        getChat().sendClans(damager, "You pinned " + getChat().getClanRelation(target, damager) + target.getName());

//        Client targetClient = getInstance().getClientManager().getClient(target);
//        targetClient.addPotionEffect(PotionEffectType.SLOW, calcBullsChargeDuration(chargeMap.get(damager)), chargeMap.get(damager));
        getInstance().getServer().getPluginManager().callEvent(
                new PotionEffectEvent(target, damager,
                        PotionEffectType.SLOWNESS,
                        calcBullsChargeDuration(chargeMap.get(damager)), chargeMap.get(damager)));
        event.setKnockbackCancelled(true);
        chargeMap.remove(damager);
        event.setCause(getName());
    }

}
