package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanPromoteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client promotee;
    private final Client promoter;
    private final ClanRank clanRank;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanPromoteEvent(Clan clan, Client promotee, Client promoter, ClanRank clanRank) {
        this.clan = clan;
        this.promotee = promotee;
        this.promoter = promoter;
        this.clanRank = clanRank;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getPromotee(){
        return this.promotee;
    }

    public Client getPromoter() { return this.promoter; }

    public ClanRank getClanRank() { return this.clanRank; }

}
