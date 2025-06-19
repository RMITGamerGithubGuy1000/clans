package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Relations.Pillage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanPillageEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Pillage pillage;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanPillageEndEvent(Pillage pillage) {
        this.pillage = pillage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Pillage getPillage(){ return this.pillage; }

}
