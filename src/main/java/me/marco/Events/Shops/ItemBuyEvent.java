package me.marco.Events.Shops;

import me.marco.Economy.ShopButton;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemBuyEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player player;
    private ShopButton shopButton;
    private int amount;
    private boolean cancelled = false;

    public ItemBuyEvent(Player player, ShopButton shopButton, int amount){
        this.player = player;
        this.amount = amount;
        this.shopButton = shopButton;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public Player getPlayer() {
        return player;
    }

    public ShopButton getShopButton() {
        return shopButton;
    }

    public int getAmount() {
        return amount;
    }

}
