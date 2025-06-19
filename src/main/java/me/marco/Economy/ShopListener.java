package me.marco.Economy;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.CListener;
import me.marco.Events.Shops.ItemBuyEvent;
import me.marco.Events.Shops.ItemSellEvent;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ShopListener extends CListener<Core> {

    public ShopListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onItemBuy(ItemBuyEvent event){
        Player player = event.getPlayer();
        ShopButton shopButton = event.getShopButton();
        int amount = event.getAmount();
        Client client = getInstance().getClientManager().getClient(player);
        if(!shopButton.canBuy(player, client, amount)){
            event.setCancelled(true);
            return;
        }
        shopButton.buyItem(player, client, amount);
    }

    @EventHandler
    public void onItemSell(ItemSellEvent event){
        Player player = event.getPlayer();
        ShopButton shopButton = event.getShopButton();
        int amount = event.getAmount();
        Client client = getInstance().getClientManager().getClient(player);
        if(!shopButton.canSell(player, amount)){
            event.setCancelled(true);
            return;
        }
        shopButton.sellItem(player, client, shopButton.getItem(), amount);
    }

}
