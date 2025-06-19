package me.marco.CustomEntities.PathFinders;

import me.marco.Base.Core;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

import java.util.EnumSet;

public class SageToadPathfinder extends Goal {

    private final Frog frog;
    private final double speed;
    private final double stopDistance;
    private Player targetPlayer;
    private org.bukkit.entity.Player tplayerBukkit;
    private org.bukkit.entity.Player owner;
    private int tickCounter = 0;
    private Core core;
    private boolean isBite = false;

    public SageToadPathfinder(Core core, org.bukkit.entity.Player owner, Frog frog, double speed, double stopDistance, Player targetPlayer, org.bukkit.entity.Player tplayerBukkit) {
        this.frog = frog;
        this.speed = speed;
        this.owner = owner;
        this.stopDistance = stopDistance;
        this.targetPlayer = targetPlayer;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.tplayerBukkit = tplayerBukkit;
        this.core = core;
        // Allow climbing 1-block steps (like stairs or terrain)
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
        frog.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (targetPlayer == null) return;

        tickCounter++;
        Vec3 targetLook = new Vec3(
                targetPlayer.getX(),
                targetPlayer.getEyeY(),
                targetPlayer.getZ()
        );
        frog.getLookControl().setLookAt(targetLook.x, targetLook.y, targetLook.z);

        double distanceSq = frog.distanceToSqr(targetPlayer);
        if (distanceSq < stopDistance) {
            if(tickCounter % 5 == 0){
                double lungeStrength = 0.3; // strength for forward motion
                double upwardBoost = 0.1;   // how high the wolf should launch

                Vector frogVec = new Vector(
                        frog.getX(),
                        frog.getY(),
                        frog.getZ()
                ).normalize();

                frog.level().playSound(
                        null,
                        frog.getX(),
                        frog.getY(),
                        frog.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.NEUTRAL,
                        2.0F,
                        0.5F
                );

                Vector direction = tplayerBukkit.getLocation().toVector().subtract(frogVec).normalize();
                direction.setX(direction.getX() * -0.5D);
                direction.setZ(direction.getZ() * -0.5D);
                direction.setY(direction.getY() * -.1);
                if(direction.getY() < 0) direction.setY(0);
                tplayerBukkit.setVelocity(direction);
//                frog.setTongueTarget(targetPlayer);
//                frog
            }

        }

        // Play pig ambient sound every 3 ticks
        if (tickCounter % 5 == 0) {
            frog.level().playSound(
                    null,
                    frog.getX(),
                    frog.getY(),
                    frog.getZ(),
                    SoundEvents.FROG_AMBIENT,
                    SoundSource.NEUTRAL,
                    2.0F,
                    0.5F
            );
        }
        if(tickCounter >= 50){
            frog.remove(Entity.RemovalReason.DISCARDED);
            frog.level().playSound(
                    null,
                    frog.getX(),
                    frog.getY(),
                    frog.getZ(),
                    SoundEvents.FROG_DEATH,
                    SoundSource.NEUTRAL,
                    2.0F,
                    1.0F
            );
        }
    }
}
