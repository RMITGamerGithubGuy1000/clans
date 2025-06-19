package me.marco.Skills.Skills.Ranger.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;

public class AntiVenom extends Skill implements PassiveSkill, PassiveB {

    public AntiVenom(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return false;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "When receiving " + ChatColor.AQUA + "Poison" + ChatColor.YELLOW + ", it is removed with an ",
                ChatColor.AQUA + "Antivenom " + ChatColor.YELLOW + "and you gain " + ChatColor.GREEN + this.formatNumber(getHealthGain(level), 1) + "" + ChatColor.YELLOW + " health. "
        };
    }

    private double getHealthGain(int level){
        return level * 2;
    }

    @Override
    public double getCooldown(int level) {
        return 0;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPotion(PotionEffectEvent event){
        if(event.isCancelled()) return;
        Player player = event.getToPotion();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(getCDManager().isCooling(player, getName())) return;
        PotionEffectType potionEffectType = event.getPotionEffectType();
        if(potionEffectType != PotionEffectType.POISON) return;
        event.setCancelled(true);
        if(player.getHealth() >= 20) return;
        int level = getLevel(player);

        double toBe = player.getHealth() + getHealthGain(level);
        if(toBe > 20) toBe = 20;
        getInstance().getUtilPvP().setPvPHealth(player, toBe);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
        UtilParticles.playBlockParticle(player.getLocation().add(0, 2, 0), Material.GLOWSTONE);
        getInstance().getCooldownManager().add(player, getName(), getClassTypeName(), level, getCooldown(level), true);

    }

}
