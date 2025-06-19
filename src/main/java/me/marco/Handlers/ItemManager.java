package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Events.Items.ItemBlockCollisionEvent;
import me.marco.Items.iThrowable;
import me.marco.Utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemManager {

    private Core core;

    public ItemManager(Core core) {
        this.core = core;
    }

    private List<iThrowable> thrownItems = new ArrayList<iThrowable>();

//    public void handleItems() {
//        new BukkitRunnable() {
//            public void run() {
//
//            }
//        }.runTaskTimer(core, 0, 1);
//    }

    public void handleItems(){
        Iterator<iThrowable> itemIterator = thrownItems.iterator();
        while (itemIterator.hasNext()) {
            iThrowable item = itemIterator.next();
            Block blockCollided = item.getBlockCollided();
            if(blockCollided != null){
                core.getServer().getPluginManager().callEvent(new ItemBlockCollisionEvent(item, blockCollided));
            }
            if (item.isExpired()) {
                item.getItem().remove();
                itemIterator.remove();
            }
        }
    }

    public iThrowable createThrowable(double expiryTimer, boolean canPickUp, boolean isBlockCollision, boolean isPlayerCollision, Location location,
                                      Material material, String name, boolean glow, String... lores) {
        Item item = location.getWorld().dropItem(location, createItemStack(material, name, glow, lores));
        iThrowable throwable = new iThrowable(item, expiryTimer, canPickUp, isBlockCollision, isPlayerCollision);
        this.thrownItems.add(throwable);
        return throwable;
    }

    public Item dropItem(Location location, Material material, String name, boolean glow, String... lores) {
        return location.getWorld().dropItem(location, createItemStack(material, name, glow, lores));
    }

    public void addThrowable(iThrowable iThrowable) {
        this.thrownItems.add(iThrowable);
    }

    public ItemStack createItemStack(Material m, String name, boolean glow, String... lores) {
        ItemStack i = new ItemStack(m, 1);
        ItemMeta im = i.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }

        if (lores != null && lores.length > 0) {
            ArrayList<String> lore = new ArrayList<String>();
            for (String str : lores) {
                lore.add(str);
            }
            im.setLore(lore);
        }
        i.setItemMeta(im);
//        if (glow) return addGlow(i);
        return i;
    }

    public ItemStack createItemStack(Material m, String name, boolean glow, List<String> lores) {
        ItemStack i = new ItemStack(m, 1);
        ItemMeta im = i.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }

        if (lores != null && lores.size() > 0) {
            ArrayList<String> lore = new ArrayList<String>();
            for (String str : lores) {
                lore.add(str);
            }
            im.setLore(lore);
        }
        i.setItemMeta(im);
//        if (glow) return addGlow(i);
        return i;
    }

    public void handleItemStackNormalisation(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        if(!im.hasDisplayName()){
            im.setDisplayName(ChatColor.YELLOW + UtilString.fixItemCapitalisation(is.getType().toString()));
        }
        is.setItemMeta(im);
    }

//    public ItemStack addGlow(ItemStack item) {
//        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound tag = null;
//        if (!nmsStack.hasTag()) {
//            tag = new NBTTagCompound();
//            nmsStack.setTag(tag);
//        }
//        if (tag == null) tag = nmsStack.getTag();
//        NBTTagList ench = new NBTTagList();
//        tag.set("ench", ench);
//        nmsStack.setTag(tag);
//        return CraftItemStack.asCraftMirror(nmsStack);
//    }

}