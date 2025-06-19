package me.marco.Events.PotionEffects;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player toPotion;
    private Player potionFrom;
    private PotionEffectType potionEffectType;
    private int duration;
    private int level;
    private boolean cancelled = false;
    private boolean isOverride = false;
    private boolean silent = false;

    public PotionEffectEvent(Player toPotion, Player potionFrom, PotionEffectType potionEffectType, int duration, int level){
        this.toPotion = toPotion;
        this.potionFrom = potionFrom;
        this.potionEffectType = potionEffectType;
        this.duration = duration;
        this.level = level;
    }

    public PotionEffectEvent(Player toPotion, Player potionFrom, PotionEffectType potionEffectType, int duration, int level, boolean silent){
        this.toPotion = toPotion;
        this.potionFrom = potionFrom;
        this.potionEffectType = potionEffectType;
        this.duration = duration;
        this.level = level;
        this.silent = silent;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public Player getPotionFrom() {
        return potionFrom;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    public void setPotionEffectType(PotionEffectType potionEffectType) {
        this.potionEffectType = potionEffectType;
    }

    public void setSilent(boolean silent){
        this.silent = silent;
    }

    public Player getToPotion() {
        return toPotion;
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }

    public boolean isSilent() {
        return silent;
    }
}
