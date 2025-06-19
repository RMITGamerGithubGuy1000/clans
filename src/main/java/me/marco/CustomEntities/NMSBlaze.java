package me.marco.CustomEntities;

import me.marco.CustomEntities.PathFinders.AllayFollowPathfinder;
import me.marco.CustomEntities.PathFinders.BlazeFollowPathfinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSBlaze extends Blaze {

    public NMSBlaze(Location loc, org.bukkit.entity.Player player) {
        super(EntityType.BLAZE, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        this.setPos(loc.getX(), loc.getY() + 3, loc.getZ());
        Player nmsPlayer = ((CraftPlayer) player).getHandle();
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        //Allay allay, Player target, double speed, float followDistance, float orbitDistance
        this.goalSelector.addGoal(1, new BlazeFollowPathfinder(this, nmsPlayer, 1.3f, 2f, 1f));
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.setNoGravity(true);
        this.setSilent(true);
        this.persist = true;
    }

    public void playAmbientSound() {
    }

}

