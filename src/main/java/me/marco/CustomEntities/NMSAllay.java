package me.marco.CustomEntities;

import me.marco.CustomEntities.PathFinders.AllayFollowPathfinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSAllay extends Allay {

    public NMSAllay(Location loc, org.bukkit.entity.Player player) {
        super(EntityType.ALLAY, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        Player nmsPlayer = ((CraftPlayer) player).getHandle();
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        //Allay allay, Player target, double speed, float followDistance, float orbitDistance
        this.goalSelector.addGoal(1, new AllayFollowPathfinder(this, nmsPlayer, 2.3f, 1f, .5f));
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
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

