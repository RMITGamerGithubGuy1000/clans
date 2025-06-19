package me.marco.GUI;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private Core core;
    private String name;
    private int size;
    private List<Button> buttonList = new ArrayList<Button>();
    private Inventory inventory;
    private boolean isStatic = true;

    public Menu(Core core, String name){
        this.core = core;
        this.name = name;
    }

    public Menu(Core core, String name, int size){
        this.core = core;
        this.name = name;
        this.size = size;
        this.inventory = generateInventory();
    }

    public Menu(Core core, String name, int size, List<Button> buttonList){
        this.core = core;
        this.name = name;
        this.size = size;
        this.buttonList = buttonList;
        this.inventory = generateInventory();
    }

    public Inventory generateInventory(){
        Inventory inv = core.getServer().createInventory(null, this.size, ChatColor.BLUE + this.name);
        if(this.buttonList == null){
            this.buttonList = new ArrayList<Button>();
            return inv;
        }
        for (Button button : this.buttonList) {
            inv.setItem(button.getSlot(), button.getItem());
        }
        return inv;
    }

    public void setInventory(Inventory inventory){
        this.inventory = inventory;
    }

    public void openMenu(Player player){
        if(this.inventory != null) player.openInventory(this.inventory);
    }

    public Core getInstance() {
        return core;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<Button> getButtons() {
        return buttonList;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Button getButton(ItemStack item){
        if(item == null || item.getItemMeta() == null) return null;
        String name = item.getItemMeta().getDisplayName();
        for(Button button : this.buttonList){
            if(button.getName().equalsIgnoreCase(name)) return button;
        }
        return null;
    }

    public Button getButton(String name){
        for(Button button : this.buttonList){
            if(button.getName().equalsIgnoreCase(name)) return button;
        }
        return null;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void addButton(Button button){
        this.buttonList.add(button);
    }
}
