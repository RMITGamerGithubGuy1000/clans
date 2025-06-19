package me.marco.Events.Skills;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassCheckEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClassCheckEvent() { }

    public HandlerList getHandlers() {
        return handlers;
    }

}
