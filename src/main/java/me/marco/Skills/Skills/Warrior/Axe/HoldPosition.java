package me.marco.Skills.Skills.Warrior.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HoldPosition extends ChannelSkill implements AxeSkill, InteractSkill {

    public HoldPosition(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public float requiredEnergy(int level) {
//        return .0125f;
        return .0255f - (level * .005f);
    }

    @Override
    public float requiredReserve(int level) {
        return .3f;
    }

    @Override
    public double toggleCooldown(int level) {
        return 2;
    }

    @Override
    public int getTicks() {
        return 10;
    }

    @Override
    public boolean runTick(Player player) {
        int level = getLevel(player);
        UtilParticles.smallerCircleParticle(player.getLocation().add(0, 1, 0), Particle.DRIPPING_WATER, 1, 0, 0, 0, 0, 0, 1);
        UtilParticles.smallerCircleParticle(player.getLocation().add(0, 1.1, 0), Particle.DRIPPING_LAVA, 1, 0, 0, 0, 0, 0, 1);

        UtilParticles.smallerCircleParticle(player.getLocation().add(0, 1.7, 0), Particle.DRIPPING_WATER, 1, 0, 0, 0, 0, 0, 1);
        UtilParticles.smallerCircleParticle(player.getLocation().add(0, 1.8, 0), Particle.DRIPPING_LAVA, 1, 0, 0, 0, 0, 0, 1);

        getInstance().getServer().getPluginManager().callEvent(
                new PotionEffectEvent(player, player,
                        PotionEffectType.REGENERATION,
                        11, 3));
        player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
        return true;
    }

    @Override
    public void cleanup(Player player) {
        player.setWalkSpeed(0.2f);
        if(player.hasPotionEffect(PotionEffectType.JUMP_BOOST)){
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        }
    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }
    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Whilst channeling, you gain " + ChatColor.AQUA + "Regen V" + ChatColor.YELLOW + " but you are ",
                ChatColor.YELLOW + "unable to move or attack whilst channeling. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 20;
    }

    @Override
    public int getMana(int level) {
        return 30;
    }

    public void activate(Player player, int level) {
        castChannelAbility(player, getLevel(player));
        toggleChannel(player);
        runTick(player);
        player.setWalkSpeed(0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 200));
    }

}
