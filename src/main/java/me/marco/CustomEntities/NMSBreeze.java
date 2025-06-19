package me.marco.CustomEntities;

import me.marco.Base.Core;
import me.marco.CustomEntities.PathFinders.AllayFollowPathfinder;
import me.marco.CustomEntities.PathFinders.BreezeFollowPathfinder;
import me.marco.CustomEntities.PathFinders.FollowGoal;
import me.marco.Events.PvP.MobDamageEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSBreeze extends Breeze {

    private Core instance;

    public NMSBreeze(Core instance, Location loc, org.bukkit.entity.Player player) {
        super(EntityType.BREEZE, ((CraftWorld) loc.getWorld()).getHandle());
        this.instance = instance;
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        this.setPos(loc.getX(), loc.getY() + 4, loc.getZ());
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        //Allay allay, Player target, double speed, float followDistance, float orbitDistance
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.persist = true;
        this.goalSelector.addGoal(1, new BreezeFollowPathfinder(this, ((CraftPlayer)player).getHandle(), 1.3f, 2, 1f));
        this.setSilent(true);
        this.setNoGravity(true);
    }

    public boolean canAttackType(EntityType<?> entitytypes) {
        return false;
//        if (this.getTarget() != null) {
//            return this.getTarget().getType() == entitytypes;
//        } else {
//            return entitytypes == EntityType.PLAYER || entitytypes == EntityType.IRON_GOLEM;
//        }
    }

    public void playAmbientSound() {
    }

}

