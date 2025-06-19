package me.marco.CustomEntities.PathFinders;

import me.marco.Base.Core;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Utility.UtilParticles;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;

public class FirefoxPathfinder extends Goal {

    private final Fox fox;
    private final double speed;
    private final double stopDistance;
    private Player targetPlayer;
    private org.bukkit.entity.Player owner;
    private int tickCounter = 0;
    private Core core;
    private boolean isBite = false;

    public FirefoxPathfinder(Core core, org.bukkit.entity.Player owner, Fox fox, double speed, double stopDistance, Player targetPlayer) {
        this.fox = fox;
        this.speed = speed;
        this.owner = owner;
        this.stopDistance = stopDistance;
        this.targetPlayer = targetPlayer;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.core = core;
        // Allow climbing 1-block steps (like stairs or terrain)
        this.fox.getNavigation().setCanFloat(true); // helps with both climbing and moving through water
    }

    @Override
    public boolean canUse() {
        return targetPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        return targetPlayer != null;
    }

    @Override
    public void stop() {
        // Optional: clear path when stopping
        fox.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (targetPlayer == null) return;

        tickCounter++;

        double distanceSq = fox.distanceToSqr(targetPlayer);
        if (distanceSq > stopDistance * stopDistance + 2) {
            // Navigate towards the player
            fox.getNavigation().moveTo(targetPlayer, speed);
        } else {
            if(isBite) return;
            // Look at player if within range
            Vec3 targetLook = new Vec3(
                    targetPlayer.getX(),
                    targetPlayer.getEyeY(),
                    targetPlayer.getZ()
            );
            fox.getLookControl().setLookAt(targetLook.x, targetLook.y, targetLook.z);
            double lungeStrength = 0.6; // strength for forward motion
            double upwardBoost = 0.4;   // how high the wolf should launch

            Vec3 direction = new Vec3(
                    targetPlayer.getX() - fox.getX(),
                    targetPlayer.getY() - fox.getY(),
                    targetPlayer.getZ() - fox.getZ()
            ).normalize();

            // Apply lunge with upward motion
            Vec3 lungeVelocity = new Vec3(
                    direction.x * lungeStrength,
                    upwardBoost, // fixed vertical component
                    direction.z * lungeStrength
            );
            fox.level().playSound(
                    null,
                    fox.getX(),
                    fox.getY(),
                    fox.getZ(),
                    SoundEvents.WOLF_AMBIENT,
                    SoundSource.NEUTRAL,
                    5.0F,
                    5.0F
            );
            isBite = true;
            fox.setDeltaMovement(lungeVelocity);
            core.getServer().getScheduler().runTaskLater(core, () -> {
                org.bukkit.entity.Player targetPlayerBukkit = (org.bukkit.entity.Player) targetPlayer.getBukkitEntity();
                core.getServer().getPluginManager().callEvent(
                        new PhysicalDamageEvent(owner, targetPlayerBukkit, 1,
                                "Firefox"));
                targetPlayerBukkit.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 0));
                UtilParticles.playEffect(targetPlayerBukkit.getLocation().add(0, 1, 0).getBlock().getLocation(), Particle.FLAME, 0, 0, 0, 0, 1);
                targetPlayerBukkit.setFireTicks(60);
                fox.remove(Entity.RemovalReason.DISCARDED);
            }, 10);
        }

        // Play pig ambient sound every 3 ticks
        if (tickCounter % 10 == 0) {
            fox.level().playSound(
                    null,
                    fox.getX(),
                    fox.getY(),
                    fox.getZ(),
                    SoundEvents.WOLF_AMBIENT,
                    SoundSource.NEUTRAL,
                    1.0F,
                    0.5F
            );
        }
        if(tickCounter >= 70){
            fox.remove(Entity.RemovalReason.DISCARDED);
            fox.level().playSound(
                    null,
                    fox.getX(),
                    fox.getY(),
                    fox.getZ(),
                    SoundEvents.WOLF_HURT,
                    SoundSource.NEUTRAL,
                    2.0F,
                    1.0F
            );
        }
    }
}
