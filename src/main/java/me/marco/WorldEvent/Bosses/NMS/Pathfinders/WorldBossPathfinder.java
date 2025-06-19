package me.marco.WorldEvent.Bosses.NMS.Pathfinders;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import org.bukkit.Bukkit;

import java.util.EnumSet;

public class WorldBossPathfinder extends Goal {
    private final Mob mob;
    private final double speed;
    private final int maxDistance = 250;
    private final BlockPos spawnPos;
    private final RandomSource random = RandomSource.create();

    public WorldBossPathfinder(Mob mob, BlockPos spawnPos, double speed) {
        this.mob = mob;
        this.spawnPos = spawnPos;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void start() {

    }


    @Override
    public void tick() {
        double distSq = this.mob.distanceToSqr(this.spawnPos.getX(), this.spawnPos.getY(), this.spawnPos.getZ());
        if (distSq > maxDistance) {
            this.mob.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        }
    }

}
