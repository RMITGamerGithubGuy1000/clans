package me.marco.Events.WorldBosses;

import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private WorldBoss hit;
    private double damage;
    private Player damager;

    public BossDamageEvent(WorldBoss hit, double damage, Player damager){
        this.hit = hit;
        this.damage = damage;
        this.damager = damager;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public WorldBoss getHit() {
        return hit;
    }

    public double getDamage() {
        return damage;
    }

    public Player getDamager() {
        return damager;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
