package me.marco.Events.Clans.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanAdminCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String name;
    private final Player creator;
    private final boolean isSafeZone;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanAdminCreateEvent(String name, Player player, boolean isSafeZone) {
        this.name = name;
        this.creator = player;
        this.isSafeZone = isSafeZone;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public String getClanName(){
        return this.name;
    }

    public Player getCreator(){
        return this.creator;
    }

    public boolean isSafeZone() { return this.isSafeZone; }

}
