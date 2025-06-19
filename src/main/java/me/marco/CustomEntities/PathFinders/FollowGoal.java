package me.marco.CustomEntities.PathFinders;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.PathType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.EnumSet;

public class FollowGoal extends Goal {
    public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
    private final Mob entity;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;

    public FollowGoal(Mob mob, ServerPlayer owner, double walkSpeed, float startDistance, float stopDistance, boolean canFly) {
        this.entity = mob;
        this.owner = owner;
        this.level = mob.level();
        this.speedModifier = walkSpeed;
        this.navigation = mob.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.canFly = canFly;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
//        if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
//            throw new IllegalArgumentException("Unsupported mob type for FollowGoal");
//        }
    }

    public boolean canUse() {
        LivingEntity entityliving = this.owner;
        if (entityliving == null) {
            return false;
        } else if (entityliving.isSpectator()) {
            this.entity.getBukkitEntity().remove();
            return false;
        }
        return true;
    }

    public boolean canContinueToUse() {
        return canUse();
    }

    private boolean unableToMove() {
        return this.entity.getNavigation().isStuck() || this.entity.isPassenger() || this.entity.isLeashed();
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathfindingMalus(PathType.WATER);
        this.entity.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.entity.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.entity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.entity.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.entity.distanceToSqr(this.owner) >= 144.0D) {
                System.out.println(this.entity.distanceToSqr(this.owner));
                this.teleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }

    }

    private void teleportToOwner() {
        BlockPos blockposition = this.owner.blockPosition();

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

    private boolean maybeTeleportTo(int i, int j, int k) {
        if (Math.abs((double)i - this.owner.getX()) < 2.0D && Math.abs((double)k - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(i, j, k))) {
            return false;
        } else {
            EntityTeleportEvent event = CraftEventFactory.callEntityTeleportEvent(this.entity, (double)i + 0.5D, (double)j, (double)k + 0.5D);
            if (event.isCancelled()) {
                return false;
            } else {
                Location to = event.getTo();
                this.entity.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                this.navigation.stop();
                return true;
            }
        }
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        return true;
//        BlockPathTypes pathtype = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, blockposition.mutable());
//        if (pathtype != BlockPathTypes.WALKABLE) {
//            return false;
//        } else {
//            BlockState iblockdata = this.level.getBlockState(blockposition.below());
//            if (!this.canFly && iblockdata.getBlock() instanceof LeavesBlock) {
//                return false;
//            } else {
//                BlockPos blockposition1 = blockposition.subtract(this.entity.blockPosition());
//                return this.level.noCollision(this.entity, this.entity.getBoundingBox().move(blockposition1));
//            }
//        }
    }

    private int randomIntInclusive(int i, int j) {
        return this.entity.getRandom().nextInt(j - i + 1) + i;
    }
}