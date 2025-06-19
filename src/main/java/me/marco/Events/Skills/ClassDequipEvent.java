package me.marco.Events.Skills;

import me.marco.Client.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassDequipEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClassDequipEvent(Player player) {
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() { return this.player; }

}
