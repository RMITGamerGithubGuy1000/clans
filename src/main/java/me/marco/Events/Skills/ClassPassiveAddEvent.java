package me.marco.Events.Skills;

import me.marco.Skills.Builders.eClassType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassPassiveAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player equipping;
    private final eClassType classType;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClassPassiveAddEvent(Player equipping, eClassType classType) {
        this.equipping = equipping;
        this.classType = classType;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getEquipping() { return this.equipping; }

    public eClassType getClassType() { return this.classType; }

}
