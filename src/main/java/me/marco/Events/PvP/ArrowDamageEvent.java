package me.marco.Events.PvP;


import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArrowDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player shooter;
    private Player target;
    private Arrow arrow;
    private double damage;
    private boolean cancelled = false;
    private boolean knockbackCancelled = false;
    private String cause = "Arrow";

    public ArrowDamageEvent(Player shooter, Arrow arrow, Player target, double damage) {
        this.shooter = shooter;
        this.target = target;
        this.arrow = arrow;
        this.damage = damage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getShooter() {
        return shooter;
    }

    public Player getTarget() {
        return target;
    }

    public double getDamage() {
        return this.damage;
    }

    public Arrow getArrow(){
        return this.arrow;
    }

    public void setDamage(double damage){ this.damage = damage; }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
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
