package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanLeaveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client leaver;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanLeaveEvent(Clan clan, Client leaver) {
        this.clan = clan;
        this.leaver = leaver;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getLeaver(){
        return this.leaver;
    }

}
