package me.marco.Utility;

import me.marco.Base.Core;
import me.marco.Client.Client;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UtilItem {

    private Core core;

    public UtilItem(Core core){
        this.core = core;
    }

    private HashMap<UUID, ItemStack> skullMap = new HashMap<UUID, ItemStack>();

    public ItemStack headItem(Client client){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        sm.setOwningPlayer(core.getServer().getOfflinePlayer(client.getUUID()));
        sm.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Cosmetics");
        sm.setLore(Arrays.asList(ChatColor.YELLOW + "Cosmetics Menu"));
        head.setItemMeta(sm);
        this.skullMap.put(client.getUUID(), head);
        return head;
    }

    public ItemStack createItem(String name, List<String> description, Material mat, int maxStackSize){
        ItemStack itemstack = new ItemStack(mat);
        ItemMeta im = itemstack.getItemMeta();
        im.setMaxStackSize(maxStackSize);
        if(mat == Material.POTION){
            PotionMeta pmeta = (PotionMeta) im;
            if(pmeta != null) pmeta.setBasePotionType(PotionType.WATER);
        }
        im.setDisplayName(name);
        im.setLore(description);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemstack.setItemMeta(im);
        return itemstack;
    }

    public ItemStack createItem(String name, List<String> description, Material mat){
        ItemStack itemstack = new ItemStack(mat);
        ItemMeta im = itemstack.getItemMeta();
        if(mat == Material.POTION){
            PotionMeta pmeta = (PotionMeta) im;
            if(pmeta != null) pmeta.setBasePotionType(PotionType.WATER);
        }
        im.setDisplayName(name);
        im.setLore(description);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemstack.setItemMeta(im);
        return itemstack;
    }

    public ItemStack createNaturalItem(Material mat){
        ItemStack itemstack = new ItemStack(mat);
        ItemMeta im = itemstack.getItemMeta();
        if(mat == Material.POTION){
            PotionMeta pmeta = (PotionMeta) im;
            if(pmeta != null) pmeta.setBasePotionType(PotionType.WATER);
        }
        im.setDisplayName(ChatColor.YELLOW + im.getDisplayName());
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemstack.setItemMeta(im);
        return itemstack;
    }

    public HashMap<UUID, ItemStack> getSkullMap() {
        return skullMap;
    }
}
