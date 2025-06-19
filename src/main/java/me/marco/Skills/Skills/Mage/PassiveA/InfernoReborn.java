package me.marco.Skills.Skills.Mage.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.NMSBlaze;
import me.marco.CustomEntities.StaticKeys;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class InfernoReborn  extends Skill implements PassiveSkill, PassiveA {

    private HashMap<Player, Blaze> blazeMap = new HashMap<Player, Blaze>();

    public InfernoReborn(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    public void onEquip(Player player){
        spawnGolem(player, getLevel(player));
    }

    public void onDequip(Player player){
        if(!blazeMap.containsKey(player)) return;
        Blaze blaze = blazeMap.get(player);
        blazeMap.remove(player);
        blaze.remove();
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() +
                getInstance().getChat().textColour + " vanished.", getClassTypeName());
        blaze.getWorld().playEffect(blaze.getLocation().add(1, 1, 0), Effect.MOBSPAWNER_FLAMES, 1);
        blaze.getWorld().playSound(blaze.getLocation(), Sound.ENTITY_BLAZE_DEATH, 5, 1);

    }

    private void spawnGolem(Player player, int level){
        if(getInstance().getCooldownManager().isCooling(player, getName())) return;
        NMSBlaze nmsBlaze = new NMSBlaze(player.getLocation(), player);
        Blaze blaze = (Blaze) nmsBlaze.getBukkitEntity();
        blaze.setCustomName(ChatColor.YELLOW + player.getName() + "'s " + getName());
        blaze.setCustomNameVisible(true);
        blazeMap.put(player, blaze);
        AttributeInstance attribute = blaze.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(getHealth(level));
            blaze.setHealth(getHealth(level));
        }
        blaze.getPersistentDataContainer().set(StaticKeys.damageableNMSKey, PersistentDataType.BYTE, (byte) (1));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_BURN, 5, 1);
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() +
                getInstance().getChat().textColour + " has returned to you.", getClassTypeName());
    }

    private double getDamage(int level){
        return .1 * level;
    }


    @Override
    public void onRecharge(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_BURN, 5, 1);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Spawns an " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " that follows you around. ",
                ChatColor.YELLOW + "When reaching " + ChatColor.GREEN + this.formatNumber(getHealth(level), 1) + "" + ChatColor.YELLOW +
                        " health, your " + ChatColor.AQUA + getName() + ChatColor.YELLOW + " will ",
                ChatColor.YELLOW + "sacrifice itself and " + ChatColor.LIGHT_PURPLE + "Respawn" + ChatColor.YELLOW + " in " +
                        ChatColor.GREEN + this.formatNumber(getCooldown(level), 1) + "" + ChatColor.YELLOW + " seconds. ",
                "",
                ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Health: " + ChatColor.RESET + "" + ChatColor.AQUA + this.formatNumber(getHealth(level), 1),
        };
    }

    private double getHealth(int level){
        return 2 + level;
    }

    @Override
    public double getCooldown(int level) {
        return 150 - level * 30;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private int calcHealth(int level){
        return 4 + level * 2;
    }

    private void handleReborn(Player player){
        int level = getLevel(player);
        Blaze blaze = blazeMap.get(player);
        blazeMap.remove(player);
        blaze.remove();
        blaze.getWorld().playEffect(blaze.getLocation().add(1, 1, 0), Effect.MOBSPAWNER_FLAMES, 1);
        blaze.getWorld().playSound(blaze.getLocation(), Sound.ENTITY_BLAZE_DEATH, 5, 1);
        getInstance().getUtilPvP().setPvPHealth(player, calcHealth(level));
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() +
                getInstance().getChat().textColour + " sacrificed itself for you.", getClassTypeName());
        getInstance().getCooldownManager().add(player, this, this.getClassTypeName(), level, this.getCooldown(level), false);
        new BukkitRunnable(){
            public void run(){
                spawnGolem(player, level);
            }
        }.runTaskLater(getInstance(), (long) getCooldown(level) * 20 + 1);
    }

    private void handleInfernoDie(Player player){
        int level = getLevel(player);
        Blaze blaze = blazeMap.get(player);
        blazeMap.remove(player);
        blaze.remove();
        blaze.getWorld().playEffect(blaze.getLocation().add(1, 1, 0), Effect.MOBSPAWNER_FLAMES, 1);
        blaze.getWorld().playSound(blaze.getLocation(), Sound.ENTITY_BLAZE_DEATH, 5, 1);
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() +
                getInstance().getChat().textColour + " has died.", getClassTypeName());
        getInstance().getCooldownManager().add(player, this, this.getClassTypeName(), level, this.getCooldown(level), false);
        new BukkitRunnable(){
            public void run(){
                spawnGolem(player, level);
            }
        }.runTaskLater(getInstance(), (long) getCooldown(level) * 20 + 1);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(event.isCancelled()) return;
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(!blazeMap.containsKey(player)) return;
        if(player.getHealth() - event.getDamage() <= 0  ){
            event.setCancelled(true);
            handleReborn(player);
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Blaze)) return;
        Player found = null;
        for(Player players : this.blazeMap.keySet()){
            Blaze blaze = blazeMap.get(players);
            Blaze dead = (Blaze) entity;
            if(dead.equals(blaze)){
                found = players;
            }
        }
        if(found != null) handleInfernoDie(found);
    }

}
