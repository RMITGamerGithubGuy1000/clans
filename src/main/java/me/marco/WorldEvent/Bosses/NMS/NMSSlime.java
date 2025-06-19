package me.marco.WorldEvent.Bosses.NMS;

import me.marco.WorldEvent.Bosses.NMS.Pathfinders.WorldBossPathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSSlime extends Slime {

    public NMSSlime(Location loc, org.bukkit.entity.Player targetPlayer) {
        super(EntityType.SLIME, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        Player nmsTarget = ((CraftPlayer) targetPlayer).getHandle();
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        this.goalSelector.addGoal(1, new WorldBossPathfinder(this, new BlockPos((int)loc.getX(), (int)loc.getY(), (int)loc.getZ()), 1));
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.persist = true;
        this.setSize(256, false);
    }

}
