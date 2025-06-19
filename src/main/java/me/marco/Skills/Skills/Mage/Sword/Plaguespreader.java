package me.marco.Skills.Skills.Mage.Sword;

import me.marco.Base.Core;
import me.marco.PvPItems.Consumeables.ImpulseGrenade.ImpulseGrenadeTag;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class Plaguespreader extends Skill implements InteractSkill, SwordSkill {

    public Plaguespreader(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public void activate(Player player, int level) {
        usePlaguespreader(player, level);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Spawns a " + ChatColor.AQUA + "Plaguespreader" + ChatColor.YELLOW + " that follows your cursor ",
                ChatColor.YELLOW + "and deals " + ChatColor.GREEN + "" + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " hearts of " + ChatColor.LIGHT_PURPLE + "Pure Damage" + ChatColor.YELLOW + " to enemies ",
                ChatColor.YELLOW + "in a radius of " + ChatColor.GREEN + getRange(level) + "" + ChatColor.YELLOW + ", as well as inflicting " + ChatColor.AQUA + "Poison I",
                ChatColor.YELLOW + "and " + ChatColor.AQUA + "Slow I" + ChatColor.YELLOW + " for " + ChatColor.GREEN + "4" + ChatColor.YELLOW + " seconds. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 16;
    }

    @Override
    public int getMana(int level) {
        return 50 - level * 5;
    }

    private double getRange(int level){
        return 3 + .5 * level;
    }

    private long getDuration(int level){
        return (long) (5 + (.5 * level));
    }

    private double getDamage(int level){
        return .25 * level;
    }

    private void usePlaguespreader(Player player, int level){
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(.6);
        Item item = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), new ItemStack(Material.PODZOL));
        //item.setGravity(false);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Plaguespreader");
        item.setVelocity(tosend);
        item.setGravity(false);
        //    public PlaguespreaderTag(Client owner, Player playerOwner, double range, double expiry, Item item, Core instance) {
        getInstance().getPvPItemManager().addTask(new PlaguespreaderTag(getInstance().getClientManager().getClient(player), player, getRange(level), getDuration(level), item, getInstance(), getDamage(level)));
    }
}
