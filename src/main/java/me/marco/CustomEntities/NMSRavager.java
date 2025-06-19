package me.marco.CustomEntities;

import me.marco.CustomEntities.PathFinders.FollowPassengerLookPathfinder;
import me.marco.CustomEntities.PathFinders.ScoutPathfinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSRavager extends Ravager {

    public NMSRavager(Location loc) {
        super(EntityType.RAVAGER, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        this.goalSelector.addGoal(10, new FollowPassengerLookPathfinder(this, 3f));
        this.persist = true;
    }

    public boolean hurtServer(ServerLevel var1, DamageSource var2, float var3){
        return false;
    }

    public boolean hurtClient(DamageSource damagesource) {
        return false;
    }

    public void playAmbientSound() {
    }

}

