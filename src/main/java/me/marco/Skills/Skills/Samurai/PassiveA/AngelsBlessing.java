package me.marco.Skills.Skills.Samurai.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.NMSAllay;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class AngelsBlessing extends Skill implements PassiveSkill, PassiveA {

    public AngelsBlessing(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Summon an " + ChatColor.AQUA + "Angel" + ChatColor.YELLOW + " that watches over you, ",
                ChatColor.YELLOW + "negating any damage taken within a ",
                ChatColor.YELLOW + "threshold of " + ChatColor.GREEN + this.formatNumber(calcualteDamageThreshold(level), 1) + "" + ChatColor.YELLOW + ". It will then leave you ",
                ChatColor.YELLOW + "and return " + ChatColor.GREEN + this.formatNumber(getCooldown(level), 1) + "" + ChatColor.YELLOW + " seconds later. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 30 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private HashMap<UUID, Allay> angelMap = new HashMap<UUID, Allay>();

    private double calcualteDamageThreshold(int level){
        return (.5 + (level * .55));
    }

    public void onEquip(Player player){
        equipAngel(player);
    }

    public void onDequip(Player player){
        handleAngelDelete(player, getLevel(player), false);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(event.isCancelled()) return;
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(!angelMap.containsKey(player.getUniqueId())) return;
        if(event.getDamage() >= calcualteDamageThreshold(getLevel(player))){
            handleAngelDelete(player, getLevel(player), true);
            UtilParticles.playInstantBreak(player.getLocation(), PotionEffectType.SPEED);
            event.setCancelled(true);
        }
    }

    private void equipAngel(Player player){
        if(getInstance().getCooldownManager().isCooling(player, getName())) return;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 5, 1);
        getInstance().getChat().sendModule(player, "Your " + ChatColor.GREEN + "Angel" + getInstance().getChat().textColour + " has returned to you!", getClassTypeName());
        NMSAllay nmsAllay = new NMSAllay(player.getLocation().add(0, 1.5, 0), player);
        Allay angel = (Allay) nmsAllay.getBukkitEntity();
        angelMap.put(player.getUniqueId(), angel);
        angel.setCustomName(ChatColor.YELLOW + player.getName() + "'s Angel");
        angel.setCustomNameVisible(true);
    }

    private void handleAngelDelete(Player player, int level, boolean returnAllay){
        UUID uuid = player.getUniqueId();
        if(!angelMap.containsKey(uuid)) return;
        Allay angel = angelMap.get(uuid);
        angelMap.remove(uuid);
        getInstance().getCooldownManager().add(player, this, this.getClassTypeName(), level, this.getCooldown(level), false);
        getInstance().getChat().sendModule(player, "You lost your " + ChatColor.GREEN + "Angel", getClassTypeName());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ALLAY_DEATH, 5, 1);
        angel.remove();
        if(returnAllay){
            new BukkitRunnable(){
                public void run(){
                    equipAngel(player);
                }
            }.runTaskLater(getInstance(), (long) getCooldown(level) * 20);
        }
    }

}
