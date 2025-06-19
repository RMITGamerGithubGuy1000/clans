package me.marco.CustomEntities.PathFinders;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BeastCallPigPathfinder extends Goal {

    private final Pig pig;
    private final double speed;
    private final double stopDistance;
    private Player targetPlayer;
    private int tickCounter = 0;

    public BeastCallPigPathfinder(Pig pig, double speed, double stopDistance, Player targetPlayer) {
        this.pig = pig;
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.targetPlayer = targetPlayer;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        // Allow climbing 1-block steps (like stairs or terrain)
        this.pig.getNavigation().setCanFloat(true); // helps with both climbing and moving through water
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
        pig.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (targetPlayer == null) return;

        tickCounter++;

        double distanceSq = pig.distanceToSqr(targetPlayer);
        if (distanceSq > stopDistance * stopDistance) {
            // Navigate towards the player
            pig.getNavigation().moveTo(targetPlayer, speed);
        } else {
            // Look at player if within range
            Vec3 targetLook = new Vec3(
                    targetPlayer.getX(),
                    targetPlayer.getEyeY(),
                    targetPlayer.getZ()
            );
            pig.getLookControl().setLookAt(targetLook.x, targetLook.y, targetLook.z);
        }

        // Floating effect: Smooth Y-axis movement
        double targetY = targetPlayer.getY();
        double currentY = pig.getY();
        double yMultiplier = 0.1;
        double newY = currentY + (targetY - currentY) * yMultiplier;
        pig.setPos(pig.getX(), newY, pig.getZ());

        // Play pig ambient sound every 3 ticks
        if (tickCounter % 3 == 0) {
            pig.level().playSound(
                    null,
                    pig.getX(),
                    pig.getY(),
                    pig.getZ(),
                    SoundEvents.PIG_AMBIENT,
                    SoundSource.NEUTRAL,
                    5.0F,
                    5.0F
            );
        }
    }
}
