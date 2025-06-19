//package me.marco.CustomEntities.CustomPVPMobs.Events;
//
//import me.marco.CustomEntities.CustomPVPMobs.CustomPVPMob;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.bukkit.event.entity.EntityDamageEvent;
//
//public class CustomPVPMobDamageEvent extends Event {
//
//    private static final HandlerList handlers = new HandlerList();
//
//    public static HandlerList getHandlerList() {
//        return handlers;
//    }
//
//    private CustomPVPMob entity;
//    private EntityDamageEvent.DamageCause damageCause;
//    private double damage;
//
//    public CustomPVPMobDamageEvent(CustomPVPMob entity, EntityDamageEvent.DamageCause damageCause, double damage){
//        this.entity = entity;
//        this.damageCause = damageCause;
//        this.damage = damage;
//    }
//
//    public HandlerList getHandlers() {
//        return handlers;
//    }
//
//    public CustomPVPMob getEntity() {
//        return entity;
//    }
//
//    public EntityDamageEvent.DamageCause getDamageCause() {
//        return damageCause;
//    }
//
//    public double getDamage() {
//        return damage;
//    }
//}
