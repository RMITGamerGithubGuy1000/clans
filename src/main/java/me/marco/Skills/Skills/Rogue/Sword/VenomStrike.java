package me.marco.Skills.Skills.Rogue.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class VenomStrike extends Skill implements SwordSkill, InteractSkill {

    private HashMap<Player, Double> prepMap = new HashMap<Player, Double>();

    private int PREP_TIME = 3;

    public VenomStrike(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public void activate(Player player, int level) {
        prepPoison(player, level);
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

    private void prepPoison(Player player, int level){
        this.prepMap.put(player, (double) PREP_TIME);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 5);
        UtilParticles.playInstantBreak(player.getLocation(), PotionEffectType.POISON);
        UtilParticles.playBlockParticle(player.getLocation().add(0, 2, 0), Material.SLIME_BLOCK);
        new BukkitRunnable(){
            public void run(){
                if(prepMap.containsKey(player)){
                    prepMap.remove(player);
                    getInstance().getChat().sendClans(player, "Your " +
                            getInstance().getChat().highlightText + getName() +
                            getInstance().getChat().textColour + " has expired");
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 5);
                    UtilParticles.playInstantBreak(player.getLocation(), PotionEffectType.HASTE);
                }
            }
        }.runTaskLater(getInstance(), 60);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);

        if(event.isCancelled()) return;
        if(!damagerClient.hasSkill(this)) return;
        if(!prepMap.containsKey(damager)) return;
        Player target = event.getTarget();
        UtilParticles.playBlockParticle(target.getLocation().add(0, 2, 0), Material.SLIME_BLOCK);
        UtilParticles.playBlockParticle(target.getLocation().add(0, 2, 0), Material.OBSIDIAN);

        getChat().sendModule(target, "You were " + getChat().highlightText + "Poisoned" + getInstance().getChat().textColour + " by " +
                getChat().getClanRelation(target, damager) + target.getName(), getClassTypeName());
        getChat().sendModule(damager, "You " + getChat().highlightText + "Poisoned " + getChat().textColour +
                getChat().getClanRelation(target, damager) + damager.getName(), getClassTypeName());
        prepMap.remove(damager);

//        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
//        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(target, damager, PotionEffectType.POISON, 80, 1));
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(target, damager, PotionEffectType.SLOWNESS, 80, 0));
    }

}
