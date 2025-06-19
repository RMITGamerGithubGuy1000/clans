package me.marco.Events.WorldBosses;

import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private WorldBoss dead;
    private Player killer;

    public BossDeathEvent(WorldBoss dead, Player killer){
        this.dead = dead;
        this.killer = killer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public WorldBoss getSpawned() {
        return dead;
    }

    public WorldBoss getDead() {
        return dead;
    }

    public Player getKiller() {
        return killer;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
