package me.marco.WorldEvent.Bosses.NMS;

import me.marco.WorldEvent.Bosses.NMS.Pathfinders.WorldBossPathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSZombie extends Zombie {

    public NMSZombie(Location loc) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        this.goalSelector.addGoal(20, new WorldBossPathfinder(this, new BlockPos((int)loc.getX(), (int)loc.getY(), (int)loc.getZ()), 1));
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.persist = true;
    }

    public void playAmbientSound() {
    }

}
