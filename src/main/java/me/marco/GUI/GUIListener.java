package me.marco.GUI;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import me.marco.Quests.GUI.DynamicMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryView;

public class GUIListener extends CListener<Core> {

    public GUIListener(Core instance) {
        super(instance);
    }



    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof WanderingTrader)) return;
        Entity entity = event.getRightClicked();
        if(!entity.isCustomNameVisible()) return;
        String name = ChatColor.stripColor(entity.getCustomName());
        Menu menu = getInstance().getMenuManager().getMenu(name);
        if(menu == null) return;
        event.setCancelled(true);
        menu.openMenu(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Menu menu = getInstance().getMenuManager().getMenu(ChatColor.stripColor(event.getView().getTitle()));
        if(menu == null) return;
        event.setCancelled(true);
        if(menu instanceof DynamicMenu){
            DynamicMenu dynamicMenu = (DynamicMenu) menu;
            Button button = dynamicMenu.getUniqueButton(getInstance().getClientManager().getClient(player), event.getCurrentItem());
            if(button == null) return;
            button.onClick(player, event.getCurrentItem(), event.getClick());
            return;
        }
        Button button = menu.getButton(event.getCurrentItem());
        if(button == null) return;
        button.onClick(player, event.getCurrentItem(), event.getClick());
    }

//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event){
//        Player player = (Player) event.getPlayer();
//    }

}
