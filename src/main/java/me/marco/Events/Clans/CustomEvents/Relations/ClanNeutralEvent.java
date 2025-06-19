package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanNeutralEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan neutralOwner;
    private final Clan toNeutral;
    private final Invite invite;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanNeutralEvent(Clan neutralOwner, Clan toNeutral) {
        this.neutralOwner = neutralOwner;
        this.toNeutral = toNeutral;
        this.invite = null;
    }
    public ClanNeutralEvent(Clan neutralOwner, Clan toNeutral, Invite invite) {
        this.neutralOwner = neutralOwner;
        this.toNeutral = toNeutral;
        this.invite = invite;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getNeutralOwner() {
        return this.neutralOwner;
    }

    public Clan getToNeutral() {
        return this.toNeutral;
    }

    public Invite getInvite() { return this.invite; }

}
