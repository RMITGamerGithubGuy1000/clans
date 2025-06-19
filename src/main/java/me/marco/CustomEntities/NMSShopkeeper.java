package me.marco.CustomEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSShopkeeper extends WanderingTrader {

    public NMSShopkeeper(Location loc) {
        super(EntityType.WANDERING_TRADER, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.setDespawnDelay(0);
        this.persist = true;
    }

    public void travel(Vec3 vec3){

    }

    public boolean hurtServer(ServerLevel var1, DamageSource var2, float var3){
        return false;
    }

    public void playAmbientSound() {
    }

}

