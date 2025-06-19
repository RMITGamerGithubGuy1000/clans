package me.marco.Skills.Skills.Rogue.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilMath;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Moxie extends Skill implements PassiveSkill, PassiveB {

    private List<Client> moxyList = new ArrayList<Client>();

    public Moxie(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Activates when you reach " + ChatColor.GREEN + healthRequirement(level) + "" + ChatColor.YELLOW + " health, and gives you ",
                ChatColor.YELLOW + "a " + ChatColor.LIGHT_PURPLE + "Physical Damage Boost" + ChatColor.YELLOW + " of " +
                        ChatColor.GREEN + this.formatNumber(multiplier(level), 2) + "" + ChatColor.YELLOW + " for " +
                        ChatColor.GREEN + this.formatNumber(moxyDuration(level) / 20, 1) + "" + ChatColor.YELLOW + " seconds. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 50 - (5 * level);
    }

    public double healthRequirement(int level){
        return 2 + level * 2;
    }

    public long moxyDuration(int level){
        return (long) ((2 + (.5 * level))) * 20;
    }

    public double multiplier(int level){
        return 1 + (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPhysicalDamage(PhysicalDamageEvent event) {
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if (damagerClient.hasSkill(this)) {
            if (this.moxyList.contains(damagerClient)) {
                event.setDamage(event.getDamage() * multiplier(getLevel(damager)));
                damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 10, 5);
                return;
            }
        }

        Player target = event.getTarget();
        Client targetClient = getInstance().getClientManager().getClient(target);
        if (event.isCancelled()) return;
        if (targetClient.hasSkill(this)) {
            if(getInstance().getCooldownManager().isCooling(damager, getName())) return;
            if(moxyList.contains(targetClient)) return;
            if (!(target.getHealth() <= healthRequirement(getLevel(target)))) return;
            target.getWorld().spawnParticle(Particle.NOTE, target.getLocation(), 1);
            getInstance().getCooldownManager().add(damager, getName(), getClassTypeName(), getLevel(target), getCooldown(getLevel(target)), false);
            moxyList.add(targetClient);
            getInstance().getChat().sendModule(target, "Your " + getInstance().getChat().highlightText + getName() + "" + getInstance().getChat().textColour + " acitvated.", getClassTypeName());
            new BukkitRunnable() {
                public void run() {
                    getInstance().getChat().sendModule(target, "Your " +
                            getInstance().getChat().highlightText + getName() + "" +
                            getInstance().getChat().textColour + " has expired", getClassTypeName());
                    target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                    moxyList.remove(targetClient);
                }
            }.runTaskLater(getInstance(), moxyDuration(getLevel(target)));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(!moxyList.contains(client)) return;
        UtilParticles.drawRedstoneParticle(player.getLocation().add(0, 1.2, 0), 244, 1, 1, 1);
    }

}
