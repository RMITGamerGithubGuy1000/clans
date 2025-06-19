package me.marco.Skills.Skills.Warrior.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Riposte extends Skill implements SwordSkill, InteractSkill {

    private final double godTime = 3;
    private final String GOD_CD_NAME = "RiposteGodCD";
    private HashMap<Player, Double> prepMap = new HashMap<Player, Double>();

    public Riposte(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    private double calculateGodTime(int level){
        return 3;
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Activate to prepare your " + ChatColor.AQUA + "Riposte" + ChatColor.YELLOW + ", after",
                ChatColor.YELLOW + "being attacked during a " + ChatColor.GREEN + this.formatNumber(.5, 1) + "" +  ChatColor.YELLOW + " second window, ",
                ChatColor.YELLOW + "gain " + ChatColor.LIGHT_PURPLE + "Invulnerability" + ChatColor.YELLOW + " for " + ChatColor.GREEN + this.formatNumber(calculateGodTime(level), 1) + "" + ChatColor.YELLOW + " seconds.",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 15 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 25;
    }

    @Override
    public void activate(Player player, int level) {
        prepareRiposte(player, level);
    }

    private void prepareRiposte(Player player, int level){
        this.prepMap.put(player, calculateGodTime(level));

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        UtilParticles.playInstantBreak(player.getLocation(), PotionEffectType.WEAKNESS);
        UtilParticles.playBlockParticle(player.getLocation().add(0, 2, 0), Material.WATER);
        new BukkitRunnable(){
            public void run(){
                if(prepMap.containsKey(player)){
                    prepMap.remove(player);
                    getInstance().getChat().sendClans(player, "Your " +
                            getInstance().getChat().highlightText + "Riposte" +
                            getInstance().getChat().textColour + " has expired");
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    UtilParticles.playBlockParticle(player.getLocation().add(0, 2, 0), Material.NETHER_PORTAL);
                }
            }
        }.runTaskLater(getInstance(), 10);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player target = event.getTarget();
        Player damager = event.getDamager();
        Client targetClient = getInstance().getClientManager().getClient(target);

        if(getCDManager().isCooling(target, GOD_CD_NAME)){
            event.setCancelled(true);
            target.getWorld().playSound(target.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0F, 1.2F);
            getChat().sendClans(damager, getChat().getClanRelation(target, damager) + target.getName() + "" +
                    getChat().textColour + " is invulnerable for " + getChat().highlightNumber + getCDManager().getRemainingString(target, GOD_CD_NAME) + "" +
                    getChat().textColour + " more seconds");
            return;
        }

        if(!targetClient.hasSkill(this)) return;
        if(!prepMap.containsKey(target)) return;
        event.setCancelled(true);

        UtilParticles.playInstantBreak(target.getLocation(), PotionEffectType.INSTANT_DAMAGE);

        UtilParticles.playBlockParticle(target.getLocation().add(0, 2, 0), 30, Material.LAVA);
        target.getWorld().playSound(target.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0F, 1.0F);
        getChat().sendClans(damager, "Your attack was countered by " + getChat().getClanRelation(target, damager) + target.getName());
        getChat().sendClans(target, "Your " + getChat().highlightText + "Riposte" + getChat().textColour + " was procced by " + getChat().getClanRelation(target, damager) + damager.getName());
        this.getCDManager().add(target, GOD_CD_NAME, this.getClassTypeName(), 0, prepMap.get(target), false);
        prepMap.remove(target);
    }

}
