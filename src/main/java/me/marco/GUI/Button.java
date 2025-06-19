package me.marco.GUI;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public abstract class Button {

    private Core core;
    private int slot;
    private String name;
    private List<String> description;
    private Material material;
    private ItemStack item;

    public Button(Core core, int slot){
        this.slot = slot;
        this.core = core;
    }

    public Button(Core core, ItemStack itemStack, int slot){
        this.slot = slot;
        this.item = itemStack;
        ItemMeta im = itemStack.getItemMeta();
        this.name = im.getDisplayName();
        this.description = im.getLore();
        this.material = itemStack.getType();
        this.core = core;
    }

    public Button(Core core, int slot, String name, List<String> description, Material material, int maxStackSize){
        this.slot = slot;
        this.name = name;
        this.description = description;
        this.material = material;
        this.item = core.getUtilItem().createItem(name, this.description, material, maxStackSize);
        this.core = core;
    }

    public Button(Core core, int slot, String name, List<String> description, Material material){
        this.slot = slot;
        this.name = name;
        this.description = description;
        this.material = material;
        this.item = core.getUtilItem().createItem(name, this.description, material);
        this.core = core;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract void onClick(Player player, ItemStack clickedItem, ClickType clickType);

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", ChatColor.stripColor(getName()));
        JSONArray loreArray = new JSONArray();
        this.getDescription().forEach(description -> {
            loreArray.add(description);
        });
        jsonObject.put("description", loreArray);
        jsonObject.put("material", getMaterial().toString());
        jsonObject.put("slot", getSlot());
        return jsonObject;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Core getInstance(){
        return this.core;
    }

}
