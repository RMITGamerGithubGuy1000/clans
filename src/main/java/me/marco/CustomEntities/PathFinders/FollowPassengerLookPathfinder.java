package me.marco.CustomEntities.PathFinders;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FollowPassengerLookPathfinder extends Goal {

    private final Mob mob;
    private final double speed;

    public FollowPassengerLookPathfinder(Mob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !mob.getPassengers().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        Entity passenger = mob.getFirstPassenger();
        if (passenger == null) return;
        Vec3 lookVec = passenger.getLookAngle(); // this is the direction they're facing
        Vec3 moveVec = new Vec3(lookVec.x, 0, lookVec.z).normalize().scale(speed);

        mob.getNavigation().moveTo(
                mob.getX() + moveVec.x,
                mob.getY(),
                mob.getZ() + moveVec.z,
                1.0 // actual speed modifier
        );
    }
}
