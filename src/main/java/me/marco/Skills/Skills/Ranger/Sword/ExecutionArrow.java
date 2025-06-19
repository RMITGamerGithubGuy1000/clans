package me.marco.Skills.Skills.Ranger.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Items.iThrowable;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Skills.Skills.Ranger.Bow.MagicArrowType;
import me.marco.Utility.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ExecutionArrow extends Skill implements SwordSkill, InteractSkill {

    private HashMap<UUID, BukkitTask> executionMap = new HashMap<UUID, BukkitTask>();

    public ExecutionArrow(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Prepare a " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " that " + ChatColor.LIGHT_PURPLE + "Executes" +
                        ChatColor.YELLOW + " players under ",
                ChatColor.GREEN + this.formatNumber(getRange(level), 1) + "" + ChatColor.YELLOW + " health. " +
                        ChatColor.LIGHT_PURPLE + "Resets" + ChatColor.YELLOW + " cooldown after killing a target."
        };
    }


    @Override
    public void activate(Player player, int level) {
        prepareArrow(player,level);
    }

    private long getDuration(int level){
        return 2 + level;
    }

    private double getRange(int level){
        return level * 2;
    }


    private void prepareArrow(Player player, int level){
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3, 10);
        getInstance().getChat().sendModule(player, "You prepared your " + getInstance().getChat().highlightText + getName() + "" + getInstance().getChat().textColour + ".", getClassTypeName());
        UtilParticles.playPotionSwirl(player.getLocation(), PotionEffectType.SPEED);
        UtilParticles.playBlockParticle(player.getLocation(), Material.LAPIS_BLOCK);
        BukkitTask task = new BukkitRunnable(){
            public void run(){
                if(executionMap.containsKey(player.getUniqueId())){
                    executionMap.remove(player.getUniqueId());
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    UtilParticles.playBlockParticle(player.getLocation(), Material.PURPLE_WOOL);
                    getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() + "" + getInstance().getChat().textColour + " expired.", getClassTypeName());
                }
            }
        }.runTaskLater(getInstance(), getDuration(level) * 20);
        executionMap.put(player.getUniqueId(), task);
    }

    @EventHandler
    public void onArrowHit(ArrowDamageEvent event){
        if(event.isCancelled()) return;
        Player hitPlayer = event.getTarget();
        Arrow arrow = event.getArrow();
        Player shooter = event.getShooter();
        Client shooterClient = getInstance().getClientManager().getClient(shooter);
        if(!executionMap.containsKey(shooter.getUniqueId())) return;
        if(!shooterClient.hasSkill(this)) return;
        double healthToBe = hitPlayer.getHealth() - event.getDamage();
        int level = getLevel(shooter);
        if(healthToBe > getRange(level)) return;
        hitPlayer.setHealth(0);
        hitPlayer.getWorld().strikeLightningEffect(hitPlayer.getLocation());
        hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 30);
        event.setCause("Execution Arrow");
//        executionMap.get(shooterClient.getUUID()).cancel();
//        prepareArrow(shooter, level);
        getInstance().getCooldownManager().removeCooldown(shooter.getName(), this.getName(), false);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public double getCooldown(int level) {
        return 60 - (5 * level);
    }

    @Override
    public int getMana(int level) {
        return 65 - level * 5;
    }
}