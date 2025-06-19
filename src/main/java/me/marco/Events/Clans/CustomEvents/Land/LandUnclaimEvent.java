package me.marco.Events.Clans.CustomEvents.Land;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LandUnclaimEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Client unclaiming;
    private final Chunk chunk;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LandUnclaimEvent(Clan clan, Client unclaiming, Chunk chunk) {
        this.clan = clan;
        this.unclaiming = unclaiming;
        this.chunk = chunk;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getClan(){ return this.clan; }

    public Client getUnclaiming(){ return this.unclaiming; }

    public Chunk getChunk() { return this.chunk; }

}
