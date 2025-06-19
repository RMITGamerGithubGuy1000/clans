package me.marco.Events.Items;

import me.marco.Items.iThrowable;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemBlockCollisionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private iThrowable collided;
    private Block collidedOn;

    public ItemBlockCollisionEvent(iThrowable collided, Block collidedOn) {
        this.collided = collided;
        this.collidedOn = collidedOn;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public iThrowable getCollided() {
        return collided;
    }

    public Block getCollidedOn() {
        return collidedOn;
    }

}
