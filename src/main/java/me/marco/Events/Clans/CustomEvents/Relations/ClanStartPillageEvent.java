package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanStartPillageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan pillagingClan;
    private final Clan toPillage;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanStartPillageEvent(Clan pillagingClan, Clan toPillage) {
        this.pillagingClan = pillagingClan;
        this.toPillage = toPillage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getPillagingClan() {
        return this.pillagingClan;
    }

    public Clan getToPillage() {
        return this.toPillage;
    }

}
