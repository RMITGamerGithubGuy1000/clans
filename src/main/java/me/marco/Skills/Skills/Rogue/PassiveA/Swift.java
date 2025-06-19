package me.marco.Skills.Skills.Rogue.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.Skills.ClassDequipEvent;
import me.marco.Events.Skills.ClassEquipEvent;
import me.marco.Events.Skills.ClassPassiveAddEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class Swift extends Skill implements PassiveSkill, PassiveA {

    public Swift(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Gain " + ChatColor.AQUA + "Speed II" + ChatColor.LIGHT_PURPLE + " permanently" + ChatColor.YELLOW + ". ",
                ChatColor.YELLOW + "You also take no " + ChatColor.LIGHT_PURPLE + "Fall Damage" + ChatColor.YELLOW + ". ",
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

    public void onEquip(Player player){
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.SPEED, 999999999, 1));
    }

    public void onDequip(Player player){
        if(player.hasPotionEffect(PotionEffectType.SPEED)){
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            Client client = getInstance().getClientManager().getClient(player);
            if(!client.hasSkill(this)) return;
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, 1);
            }
        }
    }

}
