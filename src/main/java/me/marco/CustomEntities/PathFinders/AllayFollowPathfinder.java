package me.marco.CustomEntities.PathFinders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.Random;

public class AllayFollowPathfinder extends Goal {
    private final Allay allay;
    private final Player target;
    private final double speed;
    private final float followDistance;
    private final float orbitDistance;

    private double orbitRadius;
    private boolean clockwise = true;
    private final Random random = new Random();

    public AllayFollowPathfinder(Allay allay, Player target, double speed, float followDistance, float orbitDistance) {
        this.allay = allay;
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
                this.allay.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                return true;
            }
        }
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        return true;
    }

    @Override
    public void tick() {
        double distanceSq = this.allay.distanceToSqr(this.target);
        if(distanceSq > 70){
            this.teleportToOwner();
            return;
        }
        if (distanceSq > (followDistance * followDistance)) {
            // Follow directly if too far
            Vec3 targetPos = this.target.position();
            this.allay.getNavigation().moveTo(targetPos.x, targetPos.y + 2.5, targetPos.z, speed);
        } else {
            // Orbit around player's head
            double time = this.allay.tickCount / 10.0;

            // Change direction every few seconds
            if (this.allay.tickCount % 200 == 0) {
                clockwise = !clockwise;
                orbitRadius = orbitDistance + random.nextDouble(); // slightly adjust radius
            }

            double angle = time * (clockwise ? 1 : -1);
            double x = target.getX() + orbitRadius * Math.cos(angle);
            double z = target.getZ() + orbitRadius * Math.sin(angle);

            // Add vertical bobbing
            double y = target.getY() + 1.75 + 0.25 * Math.sin(time * 2);

            this.allay.getNavigation().moveTo(x, y, z, speed);

            // Optional: face the player
            this.allay.getLookControl().setLookAt(target.getX(), target.getY() + 2, target.getZ());
        }
    }

}