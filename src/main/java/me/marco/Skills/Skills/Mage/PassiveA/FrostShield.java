package me.marco.Skills.Skills.Mage.PassiveA;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.Skills.ClassDequipEvent;
import me.marco.Events.Skills.ClassPassiveAddEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FrostShield extends Skill implements PassiveSkill, PassiveA {

    public FrostShield(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "When you would take " + ChatColor.LIGHT_PURPLE + "Physical Damage" + ChatColor.YELLOW + " that would " + ChatColor.GREEN + "kill ",
                ChatColor.YELLOW + "you, the attack will be " + ChatColor.LIGHT_PURPLE + "cancelled" + ChatColor.YELLOW + " and the attacker ",
                ChatColor.AQUA + "Frozen" + ChatColor.YELLOW + " in a " + ChatColor.AQUA + "Ice Barrier" + ChatColor.YELLOW + ". "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 20 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 100 - 25 * level;
    }

    public void onEquip(Player player){
        player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, 1, 5);
        player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_FANGS_ATTACK, 1, 5);
        getInstance().getChat().sendModule(player, "You formed a " + ChatColor.AQUA + "Frost Shield", getClassTypeName());
    }

    @EventHandler
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player target = event.getTarget();
        Player damager = event.getDamager();
        Client targetClient = getInstance().getClientManager().getClient(target);
        if(event.isCancelled()) return;
        if(!targetClient.hasSkill(this)) return;
        int level = getLevel(target);
        double damage = event.getDamage();
        if(target.getHealth() - damage > 0) return;
        if(getInstance().getCooldownManager().isCooling(target, getName())) return;
        if(!hasManaSilent(target, getMana(getLevel(target)))) return;
        removeMana(target);
        getInstance().getCooldownManager().add(target, getName(), getClassTypeName(), level, getCooldown(level), true);
        event.setCancelled(true);
        Vector launchUp = new Vector(0, .5, 0);
        damager.setVelocity(launchUp);
        new BukkitRunnable(){
            public void run(){
                Location current = damager.getLocation();
                Location centerOfBlock = new Location(
                        current.getWorld(),
                        current.getBlockX() + 0.5,
                        current.getY(),
                        current.getBlockZ() + 0.5,
                        current.getYaw(),
                        current.getPitch()
                );
                damager.teleport(centerOfBlock);
                frostTrapDamager(target, damager, level);
            }
        }.runTaskLater(getInstance(), 1);
    }

    private long getDuration(int level){
        return (long) (2 + (.5 * level));
    }

    private void frostTrapDamager(Player target, Player damager, int level){
        Location cent = damager.getLocation();
        Block top = cent.clone().add(0, 2, 0).getBlock();
        Block bottom = cent.clone().add(0, -1, 0).getBlock();
        Block x1 = cent.clone().add(1, 0, 0).getBlock();
        Block x2 = cent.clone().add(1, 1, 0).getBlock();
        Block x3 = cent.clone().add(-1, 0, 0).getBlock();
        Block x4 = cent.clone().add(-1, 1, 0).getBlock();

        Block z1 = cent.clone().add(0, 0, 1).getBlock();
        Block z2 = cent.clone().add(0, 1, 1).getBlock();
        Block z3 = cent.clone().add(0, 0, -1).getBlock();
        Block z4 = cent.clone().add(0, 1, -1).getBlock();
        double duration = getDuration(level);
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(top)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(top, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(bottom)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(bottom, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(x1)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(x1, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(x2)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(x2, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(x3)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(x3, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(x4)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(x4, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(z1)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(z1, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(z2)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(z2, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(z3)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(z3, Material.ICE, duration));
        }
        if(!getInstance().getBlockChangeManager().isBlockChangeBlock(z4)){
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(z4, Material.ICE, duration));
        }

        damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 5, 1);
        damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_WATER_AMBIENT, 5, 1);

        getInstance().getChat().sendModule(damager, "You were trapped in an " + getInstance().getChat().highlightText + getInstance().getChat().textColour +
                "Ice Chamber" + getInstance().getChat().textColour + " by " + getInstance().getChat().getClanRelation(target, damager) + target.getName(), getClassTypeName());
    }

}
