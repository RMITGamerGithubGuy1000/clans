package me.marco.Skills.Data;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import me.marco.Events.CListener;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.ISkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.*;
import me.marco.Utility.Chat;
import me.marco.Utility.Cooldowns.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class Skill extends CListener<Core> implements ISkill {

    private String name;
    private Material[] items;
    private Action[] actions;
    private int maxLevel;
    private eClassType classType;
    private boolean showRecharge;
    private boolean useInteract;
    private boolean isSafezoneSafe = false;
    private boolean silent = false;

    public Skill(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(instance);
        this.name = name;
        this.classType = classType;
        this.items = items;
        this.actions = actions;
        this.maxLevel = maxLevel;
        this.showRecharge = showRecharge;
        this.useInteract = useInteract;
    }

    public Skill(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract, boolean isSafezoneSafe) {
        super(instance);
        this.name = name;
        this.classType = classType;
        this.items = items;
        this.actions = actions;
        this.maxLevel = maxLevel;
        this.showRecharge = showRecharge;
        this.useInteract = useInteract;
        this.isSafezoneSafe = isSafezoneSafe;
    }

    public void setSilent(){
        this.silent = true;
    }

    public boolean isInform() {
        return !silent;
    }

    public String getName() { return this.name; }

    public String getNameWithLevel(int level) {
        return name + " " + level;
    }

    public Material[] getItems() {
        return items;
    }

    public Action[] getActions() {
        return actions;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public eClassType getClassType() {
        return classType;
    }

    public boolean isShowRecharge() {
        return showRecharge;
    }

    public boolean isUseInteract() {
        return useInteract;
    }

    public int getLevel(Player player) {
        int level = 1;
        Client client = getInstance().getClientManager().getClient(player);

        if (client != null) {
            if (client.getActiveBuild() != null) {
                level = client.getActiveBuild().getBuildSkill(this).getLevel();
            }
        }
        if (this instanceof SwordSkill || this instanceof AxeSkill || this instanceof BowSkill) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.getType() == Material.DIAMOND_SWORD ||mainHand.getType() == Material.DIAMOND_AXE
                    || mainHand.getType() == Material.CROSSBOW || mainHand.getType() == Material.NETHERITE_SWORD
                    || mainHand.getType() == Material.NETHERITE_AXE) {
                level++;
            }
        }
        return level;
    }

    protected String formatNumber(int number, int decimalPlace) {
        DecimalFormat skillDecimalFormat = new DecimalFormat("#." + "0".repeat(Math.max(0, decimalPlace)));
        return skillDecimalFormat.format(number);
    }

    protected String formatNumber(double number, int decimalPlace) {
        DecimalFormat skillDecimalFormat = new DecimalFormat("#." + "0".repeat(Math.max(0, decimalPlace)));
        return skillDecimalFormat.format(number);
    }

    protected String formatNumber(float number, int decimalPlace) {
        DecimalFormat skillDecimalFormat = new DecimalFormat("#." + "0".repeat(Math.max(0, decimalPlace)));
        return skillDecimalFormat.format(number);
    }


    public CooldownManager getCDManager(){
        return getInstance().getCooldownManager();
    }
    public Chat getChat(){ return getInstance().getChat(); }

    public abstract boolean useageCheck(Player player);
    public abstract String[] getDescription(int level);
    public abstract double getCooldown(int level);
    public abstract int getMana(int level);

    public String[] getAppendedDescription(int level) {
        String[] original = this.getDescription(level);
        String[] modifiedOriginal = new String[original.length + 1];
        modifiedOriginal[0] = ""; // Add the empty string at the start
        System.arraycopy(original, 0, modifiedOriginal, 1, original.length);

        ArrayList<String> additional = new ArrayList<>();
        additional.add("");
        additional.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "Cooldown: " + ChatColor.RESET + "" + ChatColor.AQUA + getCooldown(level));

        if (getMana(level) > 0) {
            additional.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "Mana Cost: " + ChatColor.RESET + "" + ChatColor.AQUA + getMana(level));
        }

        String[] additionalArray = additional.toArray(new String[0]);
        String[] result = new String[modifiedOriginal.length + additionalArray.length];
        System.arraycopy(modifiedOriginal, 0, result, 0, modifiedOriginal.length);
        System.arraycopy(additionalArray, 0, result, modifiedOriginal.length, additionalArray.length);

        return result;
    }

    public boolean hasSwordInHand(Player player){
        return player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SWORD ||
                player.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD ||
                player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
                player.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD;
    }

    public boolean hasAxeInHand(Player player){
        return player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
                player.getInventory().getItemInMainHand().getType() == Material.IRON_AXE ||
                player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
                player.getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE;
    }

    public String getClassTypeName(){
        return this.classType.getName();
    }

    public boolean hasMana(Player player){
        int level = getLevel(player);
        if(player.getLevel() >= getMana(level)){
            return true;
        }
        getInstance().getChat().sendModule(player, "You need " + ChatColor.AQUA + (getMana(level) - player.getLevel()) + " Mana" +
                getInstance().getChat().textColour + " to use " + getInstance().getChat().highlightText + getName(), "Mana");
        return false;
    }

    public boolean hasManaSilent(Player player, int required){
        if(player.getLevel() >= required){
            return true;
        }
        return false;
    }

    public boolean hasThisSkill(Client client){
        return client.hasSkill(this);
    }

    public void removeMana(Player player){
        int skillLevel = getLevel(player);
        int remaining = player.getLevel() - getMana(skillLevel);
        if(remaining < 0) remaining = 0;
        player.setLevel(remaining);
    }

    public boolean hasMana(Player player, int amount, String skillname){
        if(player.getLevel() >= amount){
            return true;
        }
        getInstance().getChat().sendModule(player, "You need " + ChatColor.AQUA + (amount - player.getLevel()) + " Mana" +
                getInstance().getChat().textColour + " to use " + getInstance().getChat().highlightText + skillname, "Mana");
        return false;
    }

    public void removeMana(Player player, int amount){
        int remaining = player.getLevel() - amount;
        if(remaining < 0) remaining = 0;
        player.setLevel(remaining);
    }

//    public boolean cantUseInSafeZone(Player player){
//        if(!getInstance().getLandManager().isInSafezone(player)) return false;
//        if(getInstance().getClientManager().getClient(player).hasCombatTag()) return false;
//        Clan clan = getInstance().getLandManager().getOwningClan(player.getLocation().getChunk());
//        getInstance().getChat().sendModule(player, "You can not use " + getInstance().getChat().highlightNumber + getName() +
//                getInstance().getChat().textColour + " in " + getInstance().getChat().highlightText + clan.getName(), getClassTypeName());
//        return true;
//    }

    public boolean isSafezoneSafe() {
        return isSafezoneSafe;
    }

    public void onRecharge(Player player){

    }
}
