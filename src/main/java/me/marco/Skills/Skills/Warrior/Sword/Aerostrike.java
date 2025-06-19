package me.marco.Skills.Skills.Warrior.Sword;

import me.marco.Base.Core;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.PvPItems.ItemTask;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Aerostrike  extends Skill implements SwordSkill, InteractSkill {

    public Aerostrike(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public void activate(Player player, int level) {
        useAerostrike(player, level);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Dash forwards and hit enemies " + ChatColor.GREEN +
                        this.formatNumber(getRange(level), 1) + "" + ChatColor.YELLOW + " blocks in front ",
                ChatColor.YELLOW + "and " + ChatColor.GREEN + this.formatNumber(1.5, 1) + "" +
                        ChatColor.YELLOW + " blocks wide from that path for ",
                ChatColor.GREEN + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " hearts of " +
                ChatColor.LIGHT_PURPLE + "Pure Damage" + ChatColor.YELLOW + ". " + ChatColor.LIGHT_PURPLE + "Resets Cooldown ",
                ChatColor.YELLOW + "after successfully killing an enemy. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 25 - 3 * level;
    }

    @Override
    public int getMana(int level) {
        return 15;
    }

    private double getRange(int level){
        return 7;
    }

    private double getDamage(int level){
        return 3 + .5 * level;
    }

    private void useAerostrike(Player player, int level) {
        BlockIterator iterator = new BlockIterator(player.getWorld(), player
                .getLocation().add(0, 1, 0).toVector(), player.getEyeLocation()
                .getDirection(), 0, 5);
        HashSet<Block> blockSet = new HashSet<>();
        while (iterator.hasNext()) {
            Block b = iterator.next();
            b.getWorld().spawnParticle(Particle.SWEEP_ATTACK, b.getLocation().add(0, 1, 0), 0);
            blockSet.add(b);
        }
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 8);
        launchPlayer(player, 1.2);

        double range = getRange(level);
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Player)) continue;
            Player target = (Player) entity;
            if (target == player) continue;

            boolean inRange = false;
            for (Block block : blockSet) {
                if (block.getLocation().distance(target.getLocation()) <= 1.5) {
                    inRange = true;
                    break;
                }
            }
            if (!inRange) continue;

            double damage = getDamage(level);
            if (target.getHealth() - damage <= 0) {
                if(getInstance().getCooldownManager().isCooling(player, getName())) {
                    getInstance().getCooldownManager().removeCooldown(player.getName(), getName(), true);
                    getInstance().getChat().sendModule(player, getInstance().getChat().highlightText + getName() + "" + getInstance().getChat().textColour + " reset for killing", getName());
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 5, 1);
                }
            }
            getInstance().getServer().getPluginManager().callEvent(new PureDamageEvent(player, target, damage, getName()));
        }
    }

    private void launchPlayer(Player player, double multiply){
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(multiply);
        tosend.setY(tosend.getY() + .3);
        player.setVelocity(tosend);
    }

}
