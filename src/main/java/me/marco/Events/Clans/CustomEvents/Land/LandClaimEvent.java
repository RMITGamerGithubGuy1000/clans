package me.marco.Events.Clans.CustomEvents.Land;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LandClaimEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client claiming;
    private final Chunk chunk;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LandClaimEvent(Clan clan, Client claiming, Chunk chunk) {
        this.clan = clan;
        this.claiming = claiming;
        this.chunk = chunk;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){ return this.clan; }

    public Client getClaiming(){ return this.claiming; }

    public Chunk getChunk() { return this.chunk; }

}
