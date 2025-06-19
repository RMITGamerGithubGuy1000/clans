package me.marco.Skills.Skills.Ranger.Axe;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SmokeBomb extends Skill implements InteractSkill, AxeSkill {

    public SmokeBomb(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract, true);
    }

    private final long INVIS_TIME = 2;

    @Override
    public void activate(Player player, int level) {
        getInstance().getUtilInvisibility().hidePlayer(player, INVIS_TIME);
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.SPEED, (int) calcInvisTime(level) * 20, 1));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
    }

    private double calcInvisTime(int level){
        return INVIS_TIME + level;
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Become " + ChatColor.AQUA + "Invisible" + ChatColor.YELLOW + " for " + ChatColor.GREEN + calcInvisTime(level) + "" + ChatColor.YELLOW + " seconds. ",
                ChatColor.YELLOW + "You can be hit while " + ChatColor.AQUA + "Invisible" + ChatColor.YELLOW + ", ",
                ChatColor.YELLOW + "which will " + ChatColor.LIGHT_PURPLE + "Un-Vanish" + ChatColor.YELLOW + " you. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 23;
    }

    @Override
    public int getMana(int level) {
        return 20;
    }
}
