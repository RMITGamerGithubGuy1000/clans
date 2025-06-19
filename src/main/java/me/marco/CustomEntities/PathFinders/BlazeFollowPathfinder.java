package me.marco.CustomEntities.PathFinders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.Random;

public class BlazeFollowPathfinder extends Goal {
    private final Blaze blaze;
    private final Player target;
    private final double speed;
    private final float followDistance;
    private final float orbitDistance;

    private double orbitRadius;
    private boolean clockwise = true;
    private final Random random = new Random();

    public BlazeFollowPathfinder(Blaze blaze, Player target, double speed, float followDistance, float orbitDistance) {
        this.blaze = blaze;
        this.target = target;
        this.speed = speed;
        this.followDistance = followDistance;
        this.orbitDistance = orbitDistance;

        this.orbitRadius = orbitDistance + random.nextDouble(); // slightly varied per instance
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    private void teleportToOwner() {
        BlockPos blockposition = this.target.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockposition.getX() + j, blockposition.getY() + k, blockposition.getZ() + l);
            if (flag) {
                return;
            }
        }
    }

    private int randomIntInclusive(int i, int j) {
        return this.target.getRandom().nextInt(j - i + 1) + i;
    }

    private boolean maybeTeleportTo(int i, int j, int k) {
        if (Math.abs((double)i - this.target.getX()) < 2.0D && Math.abs((double)k - this.target.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(i, j, k))) {
            return false;
        } else {
            EntityTeleportEvent event = CraftEventFactory.callEntityTeleportEvent(this.target, (double)i + 0.5D, (double)j, (double)k + 0.5D);
            if (event.isCancelled()) {
                return false;
            } else {
                Location to = event.getTo();
                this.blaze.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                return true;
            }
        }
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        return true;
    }

    @Override
    public void tick() {

        double distanceSq = this.blaze.distanceToSqr(this.target);
        double minOrbitDistance = (followDistance + 2.5) * (followDistance + 2.5);

        if (distanceSq > 100) {
            this.teleportToOwner();
            return;
        }

        Vec3 targetPos = this.target.position();

        if (distanceSq > minOrbitDistance) {
            // Fly directly towards the player, staying above
            Vec3 direction = new Vec3(
                    targetPos.x - blaze.getX(),
                    (targetPos.y + 3) - blaze.getY(), // Stay higher
                    targetPos.z - blaze.getZ()
            ).normalize().scale(speed * 0.25);

            this.blaze.setDeltaMovement(direction);
        } else {
            // Orbit in the air
            double time = this.blaze.tickCount / 10.0;

            if (this.blaze.tickCount % 200 == 0) {
                clockwise = !clockwise;
                orbitRadius = orbitDistance + random.nextDouble();
            }

            double angle = time * (clockwise ? 1 : -1);
            double x = targetPos.x + orbitRadius * Math.cos(angle);
            double z = targetPos.z + orbitRadius * Math.sin(angle);
            double y = targetPos.y + 3 + 0.5 * Math.sin(time * 2);

            Vec3 orbitTarget = new Vec3(x, y, z);
            Vec3 movement = orbitTarget.subtract(this.blaze.position()).normalize().scale(speed * 0.25);

            this.blaze.setDeltaMovement(movement);
        }

        // Always look at the player
        this.blaze.getLookControl().setLookAt(target.getX(), target.getY() + 3, target.getZ());
    }



}