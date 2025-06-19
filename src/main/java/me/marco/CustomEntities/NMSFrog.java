package me.marco.CustomEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSFrog extends Frog {

    public NMSFrog(Location loc) {
        super(EntityType.FROG, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
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

    public void travel(Vec3 vec3){

    }

}

