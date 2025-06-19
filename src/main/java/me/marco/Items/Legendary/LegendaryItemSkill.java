package me.marco.Items.Legendary;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import me.marco.Skills.Builders.eClassType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract class LegendaryItemSkill extends CListener<Core> {

    private String name;
    private boolean showRecharge;
    private boolean useInteract;
    private boolean isSafezoneSafe = false;
    private boolean silent = false;

    public LegendaryItemSkill(Core instance, String name, boolean showRecharge, boolean useInteract, boolean isSafezoneSafe, boolean silent) {
        super(instance);
        this.name = name;
        this.showRecharge = showRecharge;
        this.useInteract = useInteract;
        this.isSafezoneSafe = isSafezoneSafe;
        this.silent = silent;
    }

    public abstract boolean useageCheck(Player player);
    public abstract int getMana();
    public abstract double getCooldown();
    public abstract void activate(Player player);

    public boolean hasMana(Player player){
        if(player.getLevel() >= getMana()){
            return true;
        }
        getInstance().getChat().sendModule(player, "You need " + ChatColor.AQUA + (getMana() - player.getLevel()) + " Mana" +
                getInstance().getChat().textColour + " to use " + getInstance().getChat().highlightText + getName(), "Mana");
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean isShowRecharge() {
        return showRecharge;
    }

    public boolean isUseInteract() {
        return useInteract;
    }

    public boolean isSafezoneSafe() {
        return isSafezoneSafe;
    }

    public boolean isSilent() {
        return silent;
    }
}
