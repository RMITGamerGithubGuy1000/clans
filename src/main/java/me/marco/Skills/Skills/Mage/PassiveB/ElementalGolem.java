package me.marco.Skills.Skills.Mage.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.NMSBreeze;
import me.marco.CustomEntities.StaticKeys;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.Skills.SkillActivateEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ElementalGolem  extends Skill implements PassiveSkill, PassiveB {

    private HashMap<Player, Breeze> breezeMap = new HashMap<Player, Breeze>();

    public ElementalGolem(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAbilityActivate(SkillActivateEvent event) {
        if(event.isCancelled()) return;
        Player player = event.getActivator();
        Client playerClient = getInstance().getClientManager().getClient(player);
        if(!playerClient.hasSkill(this)) return;
        if(getInstance().getCooldownManager().isCooling(player, getName())) return;
        if(!hasManaSilent(player, getMana(getLevel(player)))) return;
        int level = playerClient.getActiveBuild().getSkillLevel(this);
        getInstance().getCooldownManager().add(player, getName(), getClassTypeName(), level, getCooldown(level), true);
        spawnGolem(player, level);
    }

    private void spawnGolem(Player player, int level){
        NMSBreeze nmsBreeze = new NMSBreeze(getInstance(), player.getLocation(), player);
        Breeze breeze = (Breeze) nmsBreeze.getBukkitEntity();
        AttributeInstance attribute = breeze.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(getHealth(level));
            breeze.setHealth(getHealth(level));
        }
        breeze.setCustomName(ChatColor.YELLOW + player.getName() + "'s " + getName());
        breeze.setCustomNameVisible(true);
        breeze.getPersistentDataContainer().set(StaticKeys.damageableNMSKey, PersistentDataType.BYTE, (byte) (1));
        breezeMap.put(player, breeze);
        new BukkitRunnable(){
            public void run(){
                handleDie(breeze, player, level);
            }
        }.runTaskLater(getInstance(), getLiveTime(level) * 20);
    }

    private int calcCount(int level){
        return 8 - level;
    }

    private double getDamage(int level){
        return .1 * level;
    }

    private int getRange(int level){
        return 1 + 2 * level;
    }

    private int getLiveTime(int level){
        return 5 + level * 5;
    }

    private double getLaunchAttack(int level){
        return .75;
    }

    private void handleDie(Breeze breeze, Player owner, int level){
        if(!breezeMap.containsKey(owner)) return;
        Location loc = breeze.getLocation();
        new BukkitRunnable(){
            int count = 0;
            public void run(){
                if(count >= 3){
                    dieCleanup(breeze, owner, level);
                    this.cancel();
                }
                count++;
                loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_CHARGE, 5, 1);
                loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1);
                loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc.add(0, 1, 0), 2);
            }
        }.runTaskTimer(getInstance(), 0, 5);

    }

    private void dieCleanup(Breeze breeze, Player owner, int level){
        breeze.remove();
        breezeMap.remove(owner);
        int range = getRange(level);
        UtilParticles.drawColourCircle(breeze.getLocation(), range, 0, 0, 250, 250, 250, 1);
        breeze.getWorld().playSound(breeze.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
        breeze.getWorld().playSound(breeze.getLocation(), Sound.ENTITY_BREEZE_INHALE, 5, 1);
        double damage = getDamage(level);
        for(Entity nearby : breeze.getNearbyEntities(range, range, range)){
            if(!(nearby instanceof Player)) continue;
            Player near = (Player) nearby;
            if(near.equals(owner)) continue;
            handlePush(owner, near, breeze, getLaunchAttack(level), damage);
            near.getWorld().spawnParticle(Particle.EXPLOSION, near.getLocation(), 1);
        }
    }

    private void handlePush(Player owner, Player target, Breeze breeze, double launchStrength, double damage) {
        Location targetLoc = target.getLocation();
        Location center = breeze.getLocation();
        Vector toPlayer = targetLoc.toVector().subtract(center.toVector());

        if (toPlayer.lengthSquared() < 0.01) return;

        // Normalize to keep direction but remove distance magnitude
        toPlayer.normalize();

        // Multiply horizontal strength (e.g., 1.5)
        double horizontalStrength = .7;

        // Add vertical launch (e.g., Y = 1.0)
        Vector launch = toPlayer.multiply(horizontalStrength);
        launch.setY(launchStrength); // You can tweak this to 1.5, 2.0 etc.

        // Apply push to target
        target.setVelocity(launch);

        // Trigger your custom event
        PhysicalDamageEvent physicalDamageEvent = new PhysicalDamageEvent(owner, target, damage, getName());
        physicalDamageEvent.setKnockbackCancelled(true);
        getInstance().getServer().getPluginManager().callEvent(physicalDamageEvent);
    }

    private void handleAttack(Player target, Breeze breeze, Player damager, int level){
        UtilParticles.drawLine(breeze.getLocation(), damager.getLocation(), 0.3f, 241, 210, 245, 0.3f);
        breeze.getWorld().playSound(breeze.getLocation(), Sound.ENTITY_BREEZE_DEFLECT, 5, 1);

        handlePush(target, damager, breeze, getLaunchAttack(level), getDamage(level));
    }

    @Override
    public void onRecharge(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_INHALE, 5, 1);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "After casting an ability, spawn a " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " that follows you ",
                ChatColor.YELLOW + "around. While your " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " is up, enemies that attack you will ",
                ChatColor.YELLOW + "be blown backwards and take " + ChatColor.GREEN + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " hearts of damage. When your ",
                ChatColor.YELLOW + "" + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + " dies, it will charge an " + ChatColor.LIGHT_PURPLE + "Explosion" +
                        ChatColor.YELLOW + ", blowing nearby enemies away.",
                "",
                ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Live Time: " + ChatColor.RESET + "" + ChatColor.AQUA + getLiveTime(level),
                ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Health: " + ChatColor.RESET + "" + ChatColor.AQUA + this.formatNumber(getHealth(level), 1),
        };
    }

    private double getHealth(int level){
        return 2 + level;
    }

    @Override
    public double getCooldown(int level) {
        return 125 - level * 25;
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player player = event.getTarget();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        if(!breezeMap.containsKey(player)) return;
        Player damager = event.getDamager();
        handleAttack(player, breezeMap.get(player), damager, getLevel(player));
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Breeze)) return;
        Player found = null;
        for(Player players : this.breezeMap.keySet()){
            Breeze breeze = breezeMap.get(players);
            Breeze dead = (Breeze) entity;
            if(dead.equals(breeze)){
                found = players;
            }
        }
        if(found != null) handleDie((Breeze) entity, found, getLevel(found));
    }

}
