package me.marco.Events.Clans.TimedEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimedThirtySecondsEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TimedThirtySecondsEvent(){}

    public HandlerList getHandlers() {
        return handlers;
    }

}
