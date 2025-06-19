package me.marco.Skills.Skills.Ranger.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Tags.Tag;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class RepairTag extends Tag {

    private int level;
    private double cooldownTime;
    private int regenLevel;
    private double regenTime;

    public RepairTag(Client owner, String name, int level, int regenLevel, double regenTime, long expiry, double cooldownTime, Core instance) {
        super(owner, name, expiry, instance);
        this.level = level;
        this.cooldownTime = cooldownTime;
        this.regenLevel = regenLevel;
        this.regenTime = regenTime;
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onExpiry() {
        if(this.isForceExpire()) return;
        Player player = getOwner().getPlayer();
        getInstance().getCooldownManager().add(player, "Repair", "Ranger", level, this.cooldownTime, true);
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.REGENERATION, (int) (regenTime * 20), this.regenLevel - 1));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 5, 5);
        UtilParticles.playBlockParticle(player, Material.DIAMOND_BLOCK, true);
        UtilParticles.playBlockParticle(player, Material.IRON_BARS, true);
    }
}
