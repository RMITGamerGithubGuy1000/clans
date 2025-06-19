package me.marco.Economy;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.Shops.ItemBuyEvent;
import me.marco.Events.Shops.ItemSellEvent;
import me.marco.GUI.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Iterator;

public class ShopButton extends Button {

//    public ShopButton(Core core, int slot, ShopItem shopItem) {
//        super(core, slot, shopItem.getName(), Arrays.asList(ChatColor.GREEN + "Buy Price: " + ChatColor.YELLOW + "$" + shopItem.getBuyPrice(),
//                ChatColor.RED + "Sell Price: " + ChatColor.YELLOW + "$" + shopItem.getSellPrice()), shopItem.getMaterial());
//    }

    public ShopButton(Core core, int slot, String name, double buyPrice, double sellPrice, Material material) {
        super(core, slot, ChatColor.YELLOW + name, Arrays.asList(ChatColor.GREEN + "Buy Price: " + ChatColor.YELLOW + "$" + buyPrice,
                ChatColor.RED + "Sell Price: " + ChatColor.YELLOW + "$" + sellPrice), material);
    }

    public void sendMessage(Player player, String message, Sound sound, float pitch){
        getInstance().getChat().sendModule(player, message, "Shops");
        player.playSound(player.getLocation(), sound, 1, pitch);
    }

    public boolean canBuy(Player player, Client client, int amountToBuy){
        if(player.getInventory().firstEmpty() == -1){
            sendMessage(player, "You do not have enough inventory space to purchase an item", Sound.BLOCK_NOTE_BLOCK_BASS, 3f);
            return false;
        }
        double cost = amountToBuy * getBuyAmount();
        if(client.getMoney() < cost){
            double remaining = cost - client.getMoney();
            double amount = Math.round(remaining*100.0)/100.0;
            sendMessage(player, "You need " + ChatColor.GREEN + "$" + amount + getInstance().getChat().textColour + " more dollars to purchase " +
                    ChatColor.YELLOW + amountToBuy + " " + getName() + "", Sound.BLOCK_NOTE_BLOCK_BASS, 3f);
            return false;
        }
        return true;
    }

    public void buyItem(Player player, Client client, int amountToBuy){
        ItemStack itemToBuy = getItem();
        double moneyToRemove = getBuyAmount() * amountToBuy;
        client.removeMoney(moneyToRemove);
        getInstance().getSqlRepoManager().getClientRepo().updateMoney(client);
        client.getScoreboard().refresh();
        ItemMeta im = itemToBuy.getItemMeta();
        im.setLore(null);
        ItemStack toGive = itemToBuy.clone();
        toGive.setAmount(amountToBuy);
        toGive.setItemMeta(im);
        player.getInventory().addItem(toGive);
        sendMessage(player, "You purchased " +
                ChatColor.YELLOW + amountToBuy + " " + getName() + "" + getInstance().getChat().textColour + " for " +
                ChatColor.GREEN + "$" + moneyToRemove, Sound.BLOCK_NOTE_BLOCK_PLING, 3f);
    }

    public boolean isUsedItem(ItemStack itemToSell){
        if(itemToSell instanceof Damageable){
            Damageable damagedItem = (Damageable) itemToSell;
            if(damagedItem.getDamage() < itemToSell.getType().getMaxDurability()){
                return true;
            }
        }
        return false;
    }

    public boolean canSell(Player player, int amount){
        return getAmountInInventory(player, getItem()) >= amount;
    }

    public void sellItem(Player player, Client client, ItemStack itemToSell, int amountToSell){
        sellAmountInInventory(player, amountToSell);
        player.updateInventory();
        double moneyToAdd = getSellAmount() * amountToSell;
        client.addMoney(moneyToAdd);
        getInstance().getSqlRepoManager().getClientRepo().updateMoney(client);
        client.getScoreboard().refresh();
        sendMessage(player, "You sold " +
                ChatColor.YELLOW + amountToSell + " " + getName() + "" + getInstance().getChat().textColour + " for " +
                ChatColor.GREEN + "$" + moneyToAdd, Sound.BLOCK_NOTE_BLOCK_PLING, 3f);
    }

    public void sellAmountInInventory(Player player, int amount){
        int toRemove = amount;
        for(Iterator iterator = player.getInventory().all(getItem().getType()).keySet().iterator(); iterator.hasNext();){
            int i = ((Integer)iterator.next()).intValue();
            ItemStack itemStack = player.getInventory().getItem(i);
            if(!itemStack.hasItemMeta() || !itemStack.getItemMeta().getDisplayName().equals(getItem().getItemMeta().getDisplayName())) continue;
            if(isUsedItem(itemStack)) continue;
            int foundAmount = itemStack.getAmount();
            if(toRemove >= foundAmount){
                toRemove-= foundAmount;
                player.getInventory().setItem(i, null);
            }else{
                itemStack.setAmount(foundAmount - toRemove);
            }
        }
    }

    public int getAmountInInventory(Player player, ItemStack itemStack){
        int total = 0;
        for(Iterator iterator = player.getInventory().all(itemStack.getType()).keySet().iterator(); iterator.hasNext();){
            int i = ((Integer)iterator.next()).intValue();
            ItemStack item = player.getInventory().getItem(i);
            if(!item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals(getItem().getItemMeta().getDisplayName())) continue;
            if(isUsedItem(itemStack)) continue;
            total += item.getAmount();
        }
        return total;
    }

    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        if(clickedItem == null) return;
        if(clickType == ClickType.LEFT){
            getInstance().getServer().getPluginManager().callEvent(new ItemBuyEvent(player, this, 1));
            return;
        }
        if(clickType == ClickType.SHIFT_LEFT){
            getInstance().getServer().getPluginManager().callEvent(new ItemBuyEvent(player, this, getItem().getMaxStackSize()));
            return;
        }
        if(clickType == ClickType.RIGHT){
            getInstance().getServer().getPluginManager().callEvent(new ItemSellEvent(player, this, 1));
            return;
        }
        if(clickType == ClickType.SHIFT_RIGHT){
            getInstance().getServer().getPluginManager().callEvent(new ItemSellEvent(player, this, getAmountInInventory(player, getItem())));
            return;
        }
    }

    public double getBuyAmount() {
        String[] lore = getItem().getItemMeta().getLore().get(0).split("\\$");
        return Double.valueOf(lore[1]);
    }

    public double getSellAmount() {
        String[] lore = getItem().getItemMeta().getLore().get(1).split("\\$");
        return Double.valueOf(lore[1]);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", ChatColor.stripColor(getName()));
        JSONArray loreArray = new JSONArray();
        double buyPrice = Integer.MAX_VALUE;
        double sellPrice = 0;
        for(String data : getDescription()){
            if(data.contains("Buy Price:")){
                buyPrice = Double.valueOf(data.split("\\$")[1]);
            }else if(data.contains("Sell Price:")){
                sellPrice = Double.valueOf(data.split("\\$")[1]);
            }
        }
        this.getDescription().forEach(description -> {
            loreArray.add(description);
        });
        jsonObject.put("description", loreArray);
        jsonObject.put("material", getMaterial().toString());
        jsonObject.put("slot", getSlot());
        return jsonObject;
    }

}
