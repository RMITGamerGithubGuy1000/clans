package me.marco.Skills.Skills.Mage.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class MagmaShield extends Skill implements PassiveSkill, PassiveB {

    public MagmaShield(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Upon taking " + ChatColor.AQUA + "Fire Damage" + ChatColor.YELLOW + ", gain " + ChatColor.AQUA + "Fire Resistance ",
                ChatColor.YELLOW + "for " + ChatColor.GREEN + this.formatNumber(calcTime(level), 1) + "" + ChatColor.YELLOW + " seconds. ",
        };
    }

    private double calcTime(int level){
        return 10 + level * 5;
    }

    @Override
    public double getCooldown(int level) {
        return 70 - level * 20;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private EntityDamageEvent.DamageCause[] damageList = {
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA
    };

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!Arrays.stream(damageList).anyMatch(cause -> cause == event.getCause())) return;
        Player player = (Player) event.getEntity();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        event.setCancelled(true);
        player.setFireTicks(0);
        player.setRemainingAir(player.getMaximumAir());
        handleMagmaShield(player);
    }

    public void handleMagmaShield(Player player){
        int level = getLevel(player);
        if(!this.getCDManager().add(player, this, this.getClassTypeName(), level, getCooldown(level), true)) return;
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player,
                PotionEffectType.FIRE_RESISTANCE, (int) (calcTime(level) * 20), 0));
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.GOLD_BLOCK);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CANDLE_EXTINGUISH, 5, 1);
    }

}
