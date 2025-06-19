package me.marco.Events.Clans.CustomEvents;

import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client joining;
    private final Invite invite;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanJoinEvent(Clan clan, Client joining, Invite invite) {
        this.clan = clan;
        this.joining = joining;
        this.invite = invite;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){
        return this.clan;
    }

    public Client getJoining(){
        return this.joining;
    }

    public Invite getInvite() { return this.invite; }

}
