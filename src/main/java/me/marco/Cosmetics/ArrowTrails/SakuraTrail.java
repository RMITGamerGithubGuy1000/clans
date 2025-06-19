package me.marco.Cosmetics.ArrowTrails;

import me.marco.Base.Core;
import me.marco.Cosmetics.Objects.CosmeticTypes.ArrowTrail;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

public class SakuraTrail extends ArrowTrail {

    public SakuraTrail(String cosmeticTag) {
        super(cosmeticTag, Material.CHERRY_SAPLING);
    }

    @Override
    public void playEffect(Core instance, Arrow arrow) {
        new BukkitRunnable(){
            public void run(){
                if(arrow.isDead() || arrow.isOnGround()){
                    this.cancel();
                }
                UtilParticles.playEffect(arrow.getLocation(), Particle.CHERRY_LEAVES, 0, 0, 0, 0, 1);
            }
        }.runTaskTimer(instance, 0, 3);

    }
}
