package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanTransferOwnershipEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client previousOwner;
    private final Client newOwner;


    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanTransferOwnershipEvent(Clan clan, Client previousOwner, Client newOwner) {
        this.clan = clan;
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getPreviousOwner(){
        return this.previousOwner;
    }

    public Client getNewOwner() { return this.newOwner; }

}
