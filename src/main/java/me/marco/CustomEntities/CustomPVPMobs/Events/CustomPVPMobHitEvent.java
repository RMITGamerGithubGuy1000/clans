//package me.marco.CustomEntities.CustomPVPMobs.Events;
//
//import me.marco.CustomEntities.CustomPVPMobs.CustomPVPMob;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//
//public class CustomPVPMobHitEvent extends Event {
//
//    private static final HandlerList handlers = new HandlerList();
//
//    public static HandlerList getHandlerList() {
//        return handlers;
//    }
//
//    private LivingEntity damager;
//    private CustomPVPMob target;
//    private double damage;
//
//    public CustomPVPMobHitEvent(LivingEntity damager, CustomPVPMob target, double damage){
//        this.damager = damager;
//        this.target = target;
//        this.damage = damage;
//    }
//
//    public HandlerList getHandlers() {
//        return handlers;
//    }
//
//    public void setDamage(double damage) {
//        this.damage = damage;
//    }
//
//    public LivingEntity getDamager() {
//        return damager;
//    }
//
//    public CustomPVPMob getTarget() {
//        return target;
//    }
//
//    public double getDamage() {
//        return damage;
//    }
//}
