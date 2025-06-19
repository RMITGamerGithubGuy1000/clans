package me.marco.Cosmetics.TeleportEffects;

import me.marco.Cosmetics.Objects.CosmeticTypes.TeleportEffect;
import me.marco.Utility.UtilParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffectType;

public class SplashTeleport extends TeleportEffect {

    public SplashTeleport(String cosmeticTag) {
        super(cosmeticTag, Material.WATER_BUCKET);
    }

    @Override
    public void playEffect(Location from, Location to) {
        UtilParticles.playPotionBreak(from, PotionEffectType.SPEED);
        UtilParticles.playInstantBreak(to, PotionEffectType.SPEED);
        UtilParticles.playEffect(to, Particle.BUBBLE, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(to.add(0, 1, 0), Particle.SPLASH, 0, 0, 0, 0, 1);
    }
}
