package me.marco.Events.Clans.CustomEvents.Land;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LandOverclaimEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan losingClan;
    private final Clan claimingClan;
    private final Client claiming;
    private final Chunk chunk;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LandOverclaimEvent(Clan losingClan, Clan claimingClan, Client claiming, Chunk chunk) {
        this.losingClan = losingClan;
        this.claimingClan = claimingClan;
        this.claiming = claiming;
        this.chunk = chunk;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getLosingClan(){ return this.losingClan; }

    public Clan getClaimingClan(){ return this.claimingClan; }

    public Client getClaiming(){ return this.claiming; }

    public Chunk getChunk() { return this.chunk; }

}
