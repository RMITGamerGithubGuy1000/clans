package me.marco.Events.Clans.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String name;
    private final Player creator;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanCreateEvent(String name, Player player) {
        this.name = name;
        this.creator = player;
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

}
