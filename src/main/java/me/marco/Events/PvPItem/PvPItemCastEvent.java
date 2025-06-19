package me.marco.Events.PvPItem;

import me.marco.PvPItems.PvPItem;
import me.marco.Skills.Builders.BuildSkill;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

public class PvPItemCastEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player activator;
    private final PvPItem pvpItem;
    private final Action action;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PvPItemCastEvent(Player activator, PvPItem pvpItem, Action action) {
        this.activator = activator;
        this.pvpItem = pvpItem;
        this.action = action;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getActivator() {
        return activator;
    }

    public PvPItem getPvPItem() {
        return pvpItem;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}