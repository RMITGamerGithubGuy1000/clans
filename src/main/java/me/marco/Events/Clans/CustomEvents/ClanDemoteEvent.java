package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDemoteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client demotee;
    private final Client demoter;
    private final ClanRank clanRank;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanDemoteEvent(Clan clan, Client demotee, Client demoter, ClanRank clanRank) {
        this.clan = clan;
        this.demotee = demotee;
        this.demoter = demoter;
        this.clanRank = clanRank;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getDemotee(){
        return this.demotee;
    }

    public Client getDemoter() { return this.demoter; }

    public ClanRank getClanRank() { return this.clanRank; }

}
