package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDisbandEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client owner;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanDisbandEvent(Clan clan, Client owner) {
        this.clan = clan;
        this.owner = owner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getOwner(){
        return this.owner;
    }

}
