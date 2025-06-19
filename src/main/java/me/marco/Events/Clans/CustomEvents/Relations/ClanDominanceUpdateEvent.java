package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDominanceUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan gainingClan;
    private final Clan losingClan;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanDominanceUpdateEvent(Clan gainingClan, Clan losingClan) {
        this.gainingClan = gainingClan;
        this.losingClan = losingClan;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getGainingClan() {
        return this.gainingClan;
    }

    public Clan getLosingClan() {
        return this.losingClan;
    }

}
