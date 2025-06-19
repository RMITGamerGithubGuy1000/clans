package me.marco.CustomEntities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class CustomEntity {

    private String name;
    private LivingEntity entity;
    private Location homeLoc;

    public CustomEntity(String name, Location homeLoc){
        this.name = name;
        this.homeLoc = homeLoc;
        createNewMob();
    }

    public CustomEntity(LivingEntity livingEntity, String name, Location homeLoc){
        this.entity = livingEntity;
        this.name = name;
        this.homeLoc = homeLoc;
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(name);
        this.entity.setRemoveWhenFarAway(false);
        this.entity.setPersistent(true);
        postSpawnEffects();
    }

    public boolean isAlive(){
        return this.entity != null && !this.entity.isDead();
    }

    public void checkRespawn(){
        if(isAlive()) return;
        createNewMob();
    }

    public void createNewMob(){
        if(this.isAlive()) this.entity.remove();
        createMob();
    }

    public void createMob(){
        this.entity = spawnMob(this.homeLoc);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(name);
        this.entity.setRemoveWhenFarAway(false);
        this.entity.setPersistent(true);
        postSpawnEffects();
    }

    public abstract LivingEntity spawnMob(Location location);

    public abstract void postSpawnEffects();

    public void setEntity(LivingEntity entity){
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Location getHomeLoc() {
        return homeLoc;
    }
}
