package me.marco.PvPItems;

import me.marco.Base.Core;
import me.marco.Utility.Cooldowns.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract class PvPItem {

    private Core core;
    private String name;
    private Material itemType;
    private boolean canCastSafeZone;

    public PvPItem(Core core, String name, Material itemType){
        this.core = core;
        this.name = name;
        this.itemType = itemType;
    }

    public PvPItem(Core core, String name, Material itemType, boolean canCastSafeZone){
        this.core = core;
        this.name = name;
        this.itemType = itemType;
        this.canCastSafeZone = canCastSafeZone;
    }

    public abstract String getName(Action action);

    public Material getItemType() {
        return itemType;
    }

    public abstract boolean useageCheck(Player player, Action action);

    public abstract void onUseage(Player player, Action action);

    public abstract double getCooldown(Action action);

    public CooldownManager getCDManager(){
        return getInstance().getCooldownManager();
    }

    public Core getInstance() {
        return core;
    }

    public String getUseageCat(){
        return "Item";
    }

    public abstract int getMana(Action action);

    public boolean hasMana(Player player, Action action){
        if(player.getLevel() >= getMana(action)){
            return true;
        }
        getInstance().getChat().sendModule(player, "You need " + ChatColor.AQUA + (getMana(action) - player.getLevel()) + " Mana" +
                getInstance().getChat().textColour + " to use " + getInstance().getChat().highlightText + getName(action), "Mana");
        return false;
    }

    public void removeMana(Player player, Action action){
        int remaining = player.getLevel() - getMana(action);
        if(remaining < 0) remaining = 0;
        player.setLevel(remaining);
    }

    public String getPvPItemName(){
        return this.name;
    }

    public boolean isCanCastSafeZone() {
        return canCastSafeZone;
    }

}
