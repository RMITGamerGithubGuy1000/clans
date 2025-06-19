package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanKickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client kicked;
    private final Client kicker;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanKickEvent(Clan clan, Client kicked, Client kicker) {
        this.clan = clan;
        this.kicked = kicked;
        this.kicker = kicker;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getKicked() {
        return this.kicked;
    }

    public Client getKicker() {
        return this.kicker;
    }
}
