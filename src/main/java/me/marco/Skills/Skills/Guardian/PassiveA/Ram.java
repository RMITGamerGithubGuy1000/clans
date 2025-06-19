package me.marco.Skills.Skills.Guardian.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkillData;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Ram extends ChannelSkill implements PassiveSkill, PassiveA {

    private HashMap<String, RamData> stampedeMap = new HashMap<String, RamData>();
    private final int MAX_INT = Integer.MAX_VALUE;

    public Ram(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public float requiredEnergy(int level) {
        return 0;
    }

    @Override
    public float requiredReserve(int level) {
        return 0;
    }

    @Override
    public double toggleCooldown(int level) {
        return 0;
    }

    @Override
    public int getTicks() {
        return 1;
    }

    public final double RAM_DAMAGE_REDUCTION = .3;

    @Override
    public boolean runTick(Player player) {
        if(!player.isSprinting()) return false;
        if(!stampedeMap.containsKey(player.getUniqueId().toString())) return false;
        RamData stampedeData = stampedeMap.get(player.getUniqueId().toString());
        int pastLevel = stampedeData.getLevel();
        int newLevel = stampedeData.increment(5);
        if(pastLevel != newLevel){
            player.removePotionEffect(PotionEffectType.SPEED);
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.SPEED, MAX_INT, newLevel - 1));
            //player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, newLevel - 1));
            player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1, stampedeData.soundPitch());
            String levelText = "";
            for(int i = 0; i < newLevel; i++){
                levelText = levelText + "I";
            }
            getInstance().getChat().sendModule(player, "You gained " + getInstance().getChat().highlightText + getName() + " " + levelText, getClassTypeName());
        }
        return true;
    }

    @Override
    public void cleanup(Player player) {
        if(!this.stampedeMap.containsKey(player.getUniqueId().toString())) return;
        this.stampedeMap.remove(player.getUniqueId().toString());
        if(player.hasPotionEffect(PotionEffectType.SPEED)){
            player.removePotionEffect(PotionEffectType.SPEED);
            getInstance().getChat().sendModule(player, "You lost your " + getInstance().getChat().highlightText + getName(), getClassTypeName());
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, .7f);
        }
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Continue running and every " + ChatColor.GREEN + "5 seconds",
                ChatColor.YELLOW + "you will gain a " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " charge",
                ChatColor.YELLOW + "On hit with a " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " deal extra damage",
                ChatColor.YELLOW + "and knock your target upwards",
//                ChatColor.YELLOW + "You deal " + ChatColor.RED + RAM_DAMAGE_REDUCTION + "% less damage " + ChatColor.YELLOW + "globally"
        };
    }

    @Override
    public double getCooldown(int level) {
        return 0;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler
    public void toggleSprintEvent(PlayerToggleSprintEvent event){
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(event.isSprinting()){
            activate(player);
            return;
        }
        cleanup(player);
    }

    public void activate(Player player){
        if(this.stampedeMap.containsKey(player.getUniqueId().toString())) return;
        this.stampedeMap.put(player.getUniqueId().toString(), new RamData());
        getInstance().getSkillManager().addChannelSkill(
                new ChannelSkillData(player, this, 1, getTicks()));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(event.isCancelled()) return;
        if(!damagerClient.hasSkill(this)) return;
//        double toReduce = event.getDamage() * RAM_DAMAGE_REDUCTION;
//        double damage = event.getDamage() - toReduce;
//        event.setDamage(damage);
        if(checkRam(damager, event.getTarget())) event.setKnockbackCancelled(true);
    }

    public boolean checkRam(Player damager, Player target){
        if(!stampedeMap.containsKey(damager.getUniqueId().toString())) return false;
        RamData stampedeData = stampedeMap.get(damager.getUniqueId().toString());
        int stampedeLevel = stampedeData.getLevel();
        if(stampedeLevel <= 0) return false;
        UtilParticles.playBlockParticle(target.getLocation().add(0, 2, 0), Material.DIAMOND_BLOCK);
        UtilParticles.playBlockParticle(target.getLocation().add(0, 2, 0), Material.OBSIDIAN);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, .5f);
        Vector toSend = damager.getEyeLocation().getDirection().normalize().multiply(.5 + stampedeLevel * .2).setY(.1 + stampedeLevel * .3);
        target.setVelocity(toSend);
        getChat().sendModule(target, "You were hit by " + getChat().getClanRelation(target, damager) + damager.getName() +
                getInstance().getChat().textColour + " with " + getChat().highlightText + getName(), getClassTypeName());
        getChat().sendModule(damager, "You hit " + getChat().getClanRelation(target, damager) + target.getName() +
                getInstance().getChat().textColour + " with your " + getChat().highlightText + getName(), getClassTypeName());
        damager.setSprinting(false);
//        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 + stampedeLevel * 20, stampedeLevel - 1));
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(target, damager, PotionEffectType.SLOWNESS, 20 + stampedeLevel * 20, stampedeLevel - 1));
        cleanup(damager);
        return true;
    }

}
