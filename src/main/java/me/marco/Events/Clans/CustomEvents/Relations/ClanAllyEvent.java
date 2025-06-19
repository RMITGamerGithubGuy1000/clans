package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanAllyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan allyOwner;
    private final Clan toAlly;
    private final Invite invite;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanAllyEvent(Clan allyOwner, Clan toAlly, Invite invite) {
        this.allyOwner = allyOwner;
        this.toAlly = toAlly;
        this.invite = invite;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getAllyOwner() {
        return this.allyOwner;
    }

    public Clan getToAlly() {
        return this.toAlly;
    }

    public Invite getInvite() { return this.invite; }

}
