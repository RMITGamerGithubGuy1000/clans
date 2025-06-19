package me.marco.Quests.GUI;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DynamicMenu extends Menu {

    private HashMap<Client, List<Button>> buttonMap = new HashMap<Client, List<Button>>();

    public DynamicMenu(Core core, String name, int size) {
        super(core, name, size, new ArrayList<Button>());
    }

    public void openMenu(Player player){
        if(this.getInventory() != null){
            player.openInventory(generateUniqueMenu(player));
        }
    }

    public abstract Inventory generateUniqueMenu(Player player);

    public HashMap<Client, List<Button>> getButtonMap() {
        return buttonMap;
    }

    public Button getUniqueButton(Client client, ItemStack item){
        if(item == null || item.getItemMeta() == null) return null;
        String name = item.getItemMeta().getDisplayName();
        for(Button button : this.buttonMap.get(client)){
            if(ChatColor.stripColor(button.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) return button;
        }
        return null;
    }

    public Inventory generateInventory(Client client, List<Button> uniqueButtons){
        Inventory inv = getInstance().getServer().createInventory(null, this.getSize(), ChatColor.BLUE + this.getName());
        this.buttonMap.put(client, uniqueButtons);
        for (Button button : uniqueButtons) {
            inv.setItem(button.getSlot(), button.getItem());
        }
        return inv;
    }

}
