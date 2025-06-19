package me.marco.Events.WorldBosses;

import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private WorldBoss spawned;
    private Location location;

    public BossSpawnEvent(WorldBoss spawned, Location location){
        this.spawned = spawned;
        this.location = location;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public WorldBoss getSpawned() {
        return spawned;
    }

    public void setSpawned(WorldBoss spawned) {
        this.spawned = spawned;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
