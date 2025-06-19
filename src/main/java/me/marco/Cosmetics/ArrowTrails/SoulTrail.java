package me.marco.Cosmetics.ArrowTrails;

import me.marco.Base.Core;
import me.marco.Cosmetics.Objects.CosmeticTypes.ArrowTrail;
import me.marco.Utility.UtilParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

public class SoulTrail extends ArrowTrail {

    public SoulTrail(String cosmeticTag) {
        super(cosmeticTag, Material.SOUL_CAMPFIRE);
    }

    @Override
    public void playEffect(Core instance, Arrow arrow) {
        new BukkitRunnable(){
            public void run(){
                if(arrow.isDead() || arrow.isOnGround()){
                    this.cancel();
                }
                UtilParticles.playEffect(arrow.getLocation(), Particle.SOUL_FIRE_FLAME, 0, 0, 0, 0, 1);
            }
        }.runTaskTimer(instance, 0, 5);

    }
}
