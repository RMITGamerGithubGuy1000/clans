package me.marco.WorldEvent.Bosses.Lycanthrope.Skills.WolfSpawn;

import me.marco.Base.Core;
import me.marco.CustomEntities.PathFinders.ScoutPathfinder;
import me.marco.WorldEvent.Bosses.NMS.Pathfinders.WorldBossPathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSWolfSpawn extends Wolf {

    public NMSWolfSpawn(Core core, Location loc, org.bukkit.entity.Player targetPlayer, double damage, double speed) {
        super(EntityType.WOLF, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        net.minecraft.world.entity.player.Player nmsTarget = ((CraftPlayer) targetPlayer).getHandle();
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        this.goalSelector.addGoal(10, new WolfSpawnPathfinder(core,this, speed, 2D, nmsTarget, damage));
        this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.STEP_HEIGHT)
                .setBaseValue(speed); // This makes it able to step up 1-block ledges
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.persist = true;
    }

    public void playAmbientSound() {
    }

}
