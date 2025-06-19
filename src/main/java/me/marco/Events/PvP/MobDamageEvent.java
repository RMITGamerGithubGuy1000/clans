package me.marco.Events.PvP;


import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MobDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private LivingEntity hitMob;
    private Player damager;
    private double damage;
    private boolean cancelled = false;
    private boolean knockbackCancelled = false;

    public MobDamageEvent(LivingEntity hitMob, Player damager, double damage) {
        this.hitMob = hitMob;
        this.damager = damager;
        this.damage = damage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public double getDamage() {
        return this.damage;
    }

    public Player getDamager(){
        return this.damager;
    }

    public LivingEntity getHitMob(){
        return this.hitMob;
    }

    public void setDamage(double damage){ this.damage = damage; }

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
