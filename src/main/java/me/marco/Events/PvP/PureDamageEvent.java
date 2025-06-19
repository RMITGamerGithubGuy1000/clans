package me.marco.Events.PvP;


import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PureDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player damager;
    private Player target;
    private double damage;
    private boolean cancelled = false;
    private boolean knockbackCancelled = false;
    private String cause = null;

    public PureDamageEvent(Player damager, Player target, double damage) {
        this.damager = damager;
        this.target = target;
        this.damage = damage;
    }

    public PureDamageEvent(Player damager, Player target, double damage, String cause) {
        this.damager = damager;
        this.target = target;
        this.damage = damage;
        this.cause = cause;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getDamager() {
        return damager;
    }

    public Player getTarget() {
        return target;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage){ this.damage = damage; }

    public String getCause() {
        return cause;
    }

    public void setCause(String reason) {
        this.cause = cause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public void setKnockbackCancelled(boolean b) {
        this.knockbackCancelled = b;
    }

    public boolean isKnockbackCancelled() {
        return this.knockbackCancelled;
    }
}
