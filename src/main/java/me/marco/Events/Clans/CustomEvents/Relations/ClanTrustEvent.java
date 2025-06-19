package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanTrustEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan trustOwner;
    private final Clan toTrust;
    private final boolean trust;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanTrustEvent(Clan trustOwner, Clan toTrust, boolean trust) {
        this.trustOwner = trustOwner;
        this.toTrust = toTrust;
        this.trust = trust;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getTrustOwner() {
        return trustOwner;
    }

    public Clan getToTrust() {
        return toTrust;
    }

    public boolean getTrust() {
        return trust;
    }
}
