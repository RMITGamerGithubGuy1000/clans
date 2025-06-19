package me.marco.PvPItems;

import me.marco.Base.Core;
import me.marco.PvPItems.Consumeables.Douse.DouseBottle;
import me.marco.PvPItems.Consumeables.Enderpearl.Enderpearl;
import me.marco.PvPItems.Consumeables.HomingChrystal.HomingChrystal;
import me.marco.PvPItems.Consumeables.ImpulseGrenade.ImpulseGrenade;
import me.marco.PvPItems.Consumeables.Incendiary.IncendiaryGrenade;
import me.marco.PvPItems.Consumeables.MushroomSoup.MushieSoup;
import me.marco.PvPItems.Consumeables.WebGrenade.WebGrenade;
import me.marco.PvPItems.MagicBook.MagicBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PvPItemManager {

    private Core core;

    public PvPItemManager(Core core){
        this.core = core;
    }

    private List<PvPItem> pvpItemList = new ArrayList<PvPItem>();
    private List<ItemTask> itemTaskList = new ArrayList<ItemTask>();

    public void handleItemTasks(){
        List<ItemTask> toRemove = new ArrayList<ItemTask>();
        for(ItemTask task : this.itemTaskList){
            if(task.runTick()){
                task.onExpiry();
                toRemove.add(task);
            }
        }
        toRemove.forEach(tag -> this.itemTaskList.remove(tag));
    }

    public void addTask(ItemTask itemTask){
        this.itemTaskList.add(itemTask);
    }

    public void initialiseItems() {
        initialiseConsumeables();
    }

    public PvPItem isConsumeable(ItemStack itemStack){
        if(itemStack == null) return null;
        if(!itemStack.hasItemMeta()) return null;
        ItemMeta im = itemStack.getItemMeta();
        if(im.getDisplayName() == null) return null;
        return getItem(im.getDisplayName(), itemStack.getType());
    }

    public PvPItem getItem(String name, Material type){
        return this.pvpItemList.stream().filter(pvpItem -> pvpItem.getPvPItemName().equals(ChatColor.stripColor(name)) &&
                pvpItem.getItemType() == type).findFirst().orElse(null);
    }

    public void initialiseConsumeables(){
        addItem(new WebGrenade(getInstance()));
        addItem(new Enderpearl(getInstance()));
        addItem(new IncendiaryGrenade(getInstance()));
        addItem(new MushieSoup(getInstance()));
        addItem(new MagicBook(getInstance()));
        addItem(new DouseBottle(getInstance()));
        addItem(new ImpulseGrenade(getInstance()));
        addItem(new HomingChrystal(getInstance()));
    }

    public void addItem(PvPItem pvPItem){
        this.pvpItemList.add(pvPItem);
    }

    public Core getInstance(){
        return this.core;
    }

}
