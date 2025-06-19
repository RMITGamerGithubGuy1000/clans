package me.marco.Skills.Data.ISkills.SkillTypes;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract class ChannelSkill extends Skill {

    public ChannelSkill(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    public abstract float requiredEnergy(int level);
    public abstract float requiredReserve(int level);
    public abstract double toggleCooldown(int level);
    public abstract int getTicks();
    private boolean needsHoldItem = true;

    public String[] getAppendedDescription(int level) {
        String[] original = this.getDescription(level);
        String[] modifiedOriginal = new String[original.length + 1];
        modifiedOriginal[0] = ""; // Add the empty string at the start
        System.arraycopy(original, 0, modifiedOriginal, 1, original.length); // Shift rest by 1
        String[] additional = new String[] {
                "",
                ChatColor.GREEN + ChatColor.BOLD.toString() + "Cooldown: " + ChatColor.RESET + "" + ChatColor.AQUA + getCooldown(level),
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Mana Cost: " + ChatColor.RESET + "" + ChatColor.AQUA + getMana(level),
                "",
                ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Toggle CD: " + ChatColor.RESET + "" + ChatColor.AQUA + toggleCooldown(level),
                ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Reserve: " + ChatColor.RESET + "" + ChatColor.AQUA + requiredReserve(level),
                ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Energy: " + ChatColor.RESET + "" + ChatColor.AQUA + requiredEnergy(level) + " energy" + ChatColor.DARK_GRAY + " (per tick)",
                "",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "Runs every " + ChatColor.LIGHT_PURPLE + getTicks() + "" + ChatColor.GRAY + " ticks",
        };
        String[] result = new String[modifiedOriginal.length + additional.length];
        System.arraycopy(modifiedOriginal, 0, result, 0, modifiedOriginal.length);
        System.arraycopy(additional, 0, result, modifiedOriginal.length, additional.length);
        return result;
    }

    public abstract boolean runTick(Player player);

    public boolean needsHoldItem(){
        return this.needsHoldItem;
    }

    public void setNeedsHoldItem(boolean needsHoldItem){
        this.needsHoldItem = needsHoldItem;
    }

    public abstract void cleanup(Player player);

    public boolean canCastChannel(Player player, boolean isInitial, boolean isExpiry, int level){
        if(isInitial){
            if(hasMana(player)){
                return hasEnergy(player, isInitial, isExpiry, level);
            }
            return false;
        }
        return hasEnergy(player, isInitial, isExpiry, level);
    }

    public boolean checkChannelingForToggle(Player player){
        return getInstance().getClientManager().getClient(player).getIsChanneling() == this;
    }

    public boolean toggleChannel(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        if(client.getIsChanneling() == null){
            getInstance().getChat().sendModule(player, "You toggled " + getInstance().getChat().highlightText + getName() +
                    " " + getLevel(player) + " " + ChatColor.GREEN + "ON", getClassTypeName());
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 5);
            client.setChanneling(this);
            return true;
        }
        ChannelSkill channelSkill = client.getIsChanneling();
        getInstance().getChat().sendModule(player, "You toggled " + getInstance().getChat().highlightText + getName() +
                " " + getLevel(player) + " " + ChatColor.RED + "OFF", getClassTypeName());
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 5);
        client.setChanneling(null);
        getInstance().getCooldownManager().channelAdd(player, channelSkill, channelSkill.getClassTypeName(), channelSkill.getLevel(player), channelSkill.toggleCooldown(channelSkill.getLevel(player)), true, false);
        return false;
    }

    public boolean isChanneling(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        return client.getIsChanneling() != null;
    }

    public boolean hasEnergy(Player player, boolean isInitial, boolean isExpiry, int level){
        float toBe = player.getExp() - requiredEnergy(level);
        if(isInitial) {
            if (toBe < requiredReserve(level)) {
                int required = (int) ((requiredReserve(level) - toBe) * 100);
                getInstance().getChat().sendModule(player, "You need " + ChatColor.YELLOW + required + " Energy" +
                        getInstance().getChat().textColour + " to use " + getInstance().getChat().highlightText + getName(), "Energy");
                return false;
            }
        }
        if(toBe >= 0){
            return true;
        }
        if(isExpiry){
            getInstance().getChat().sendModule(player, "Your " + ChatColor.YELLOW + "Energy" +
                    getInstance().getChat().textColour + " reserves are too low. Your " + getInstance().getChat().highlightText + getName() +
                    getInstance().getChat().textColour + " has been cancelled", "Energy");
        }
        return false;
    }

    public void castChannelAbility(Player player, int level){
        removeMana(player);
        getInstance().getSkillManager().addChannelSkill(new ChannelSkillData(player, player.getInventory().getHeldItemSlot(), this, level, getTicks()));
    }

}
