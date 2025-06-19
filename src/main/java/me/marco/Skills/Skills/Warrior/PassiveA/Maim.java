package me.marco.Skills.Skills.Warrior.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.Skills.SkillActivateEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class Maim  extends Skill implements PassiveSkill, PassiveA {

    private ArrayList<UUID> maimList = new ArrayList<>();

    public Maim(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    private void prepMaim(Player player, int level){
        maimList.add(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 5, 1);
        new BukkitRunnable(){
            public void run(){
                if(!maimList.contains(player.getUniqueId())) return;
                player.playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 5, 1);
                getInstance().getChat().sendModule(player, "You lost your " +
                        getInstance().getChat().highlightText + getName() + "" + getInstance().getChat().textColour + ".", getClassTypeName());
            }
        }.runTaskLater(getInstance(), calcExpiry(level) * 20L);
    }

    private long calcMaimTime(int level){
        return level * 5;
    }

    private void handleMaim(Player damager, Player target){
        maimList.remove(damager.getUniqueId());
        Item item = target.getWorld().dropItem(target.getLocation().add(0, 1.5, 0), new ItemStack(Material.REDSTONE));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Maimed");
        int level = getLevel(damager);
        getInstance().getChat().sendModule(damager, "You" + getInstance().getChat().highlightText + " Maimed " +
                getInstance().getChat().getClanRelation(damager, target) + target.getName() + "" + getInstance().getChat().textColour + ".", getClassTypeName());
        getInstance().getChat().sendModule(damager, "You were" + getInstance().getChat().highlightText + " Maimed " +
                getInstance().getChat().textColour + "by " + getInstance().getChat().getClanRelation(damager, target) +
                damager.getName() + "" + getInstance().getChat().textColour + ".", getClassTypeName());
        MaimTag maimTag = new MaimTag(getInstance().getClientManager().getClient(damager), target,
                calcReduction(level), calcMaimTime(level), item, getInstance(), getClassType());
        getInstance().getTagManager().addTag(getInstance().getClientManager().getClient(target), maimTag);

    }

    private double calcReduction(int level){
        return .1 * level;
    }

    private int calcExpiry(int level){
        return 2 + level;
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "After casting an ability gain " + ChatColor.AQUA + "Maim Strike" + ChatColor.YELLOW + ", which ",
                ChatColor.YELLOW + "lasts for " + ChatColor.GREEN + this.formatNumber(calcExpiry(level), 1) + "" +
                        ChatColor.YELLOW + " seconds before " + ChatColor.LIGHT_PURPLE + "expiring" + ChatColor.YELLOW + ". With ",
                ChatColor.AQUA + "Maim Strike" + ChatColor.YELLOW + ", the next enemy you hit will be " + ChatColor.AQUA + "Maimed ",
                ChatColor.YELLOW + "which will " + ChatColor.LIGHT_PURPLE + "reduce" + ChatColor.YELLOW + " their " +
                ChatColor.LIGHT_PURPLE + "Healing" + ChatColor.YELLOW + " by " + ChatColor.GREEN + this.formatNumber(calcReduction(level), 1) + "%" +
                ChatColor.YELLOW + "for " + ChatColor.GREEN + this.formatNumber(calcMaimTime(level), 1) + "" + ChatColor.YELLOW + " seconds. "
        };
    }


    @Override
    public double getCooldown(int level) {
        return 125 - level * 25;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player player = event.getDamager();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(!maimList.contains(player.getUniqueId())) return;
        Player target = event.getTarget();
        handleMaim(player, target);
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent  event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasTag("MaimTag")){
            getInstance().getUtilPvP().spawnHeal(player, event.getAmount());
            return;
        }
        MaimTag maimTag = (MaimTag) client.getTag("MaimTag");
        double reduction = maimTag.getReduction();
        double original = event.getAmount();
        double reduced = original * reduction;
        event.setAmount(reduced);
        getInstance().getUtilPvP().spawnHeal(player, event.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAbilityActivate(SkillActivateEvent event) {
        if(event.isCancelled()) return;
        Player player = event.getActivator();
        Client playerClient = getInstance().getClientManager().getClient(player);
        if(!playerClient.hasSkill(this)) return;
        if(getInstance().getCooldownManager().isCooling(player, getName())) return;
        int level = playerClient.getActiveBuild().getSkillLevel(this);
        getInstance().getCooldownManager().add(player, getName(), getClassTypeName(), level, getCooldown(level), true);
        prepMaim(player, level);
    }

}
