package me.marco.Events.Skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InventoryAdjustEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public InventoryAdjustEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public HandlerList getHandlers() {
        return getHandlers();
    }
}
