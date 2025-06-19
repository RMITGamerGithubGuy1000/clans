package me.marco.CustomEntities;

import me.marco.CustomEntities.PathFinders.BeastCallPigPathfinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSBeastCallPig extends Pig {

    public NMSBeastCallPig(Location loc, org.bukkit.entity.Player targetPlayer) {
        super(EntityType.PIG, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
        Player nmsTarget = ((CraftPlayer) targetPlayer).getHandle();
        this.goalSelector.removeAllGoals(goal -> true); // remove all goals
        this.targetSelector.removeAllGoals(goal -> true); // remove all target goals
        this.goalSelector.addGoal(10, new BeastCallPigPathfinder(this, 2D, .5D, nmsTarget));
        this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.STEP_HEIGHT)
                .setBaseValue(2.1D); // This makes it able to step up 1-block ledges
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
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

