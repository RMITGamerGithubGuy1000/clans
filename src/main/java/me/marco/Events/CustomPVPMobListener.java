//package me.marco.Events;
//
//import me.marco.Base.Core;
//import me.marco.CustomEntities.CustomPVPMobs.CustomPVPMob;
//import me.marco.CustomEntities.CustomPVPMobs.Events.CustomPVPMobDamageEvent;
//import me.marco.CustomEntities.CustomPVPMobs.Events.CustomPVPMobHitEvent;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.entity.Mob;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.entity.EntityDeathEvent;
//
//public class CustomPVPMobListener extends CListener<Core> {
//
//    public CustomPVPMobListener(Core instance) {
//        super(instance);
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onDamage(EntityDamageByEntityEvent event){
//        Entity eDmger = event.getDamager();
//        if(!(eDmger instanceof LivingEntity)) return;
//        CustomPVPMob customEntity = getInstance().getCustomPVPMobManager().getCustomEntity(event.getEntity());
//        if(customEntity == null) return;
//        event.setCancelled(true);
//        if(!customEntity.canBeHit()) return;
//        getInstance().getServer().getPluginManager().callEvent(new CustomPVPMobHitEvent((LivingEntity) eDmger, customEntity, 1));
//    }
//
//    @EventHandler
//    public void onCustomMobHit(CustomPVPMobHitEvent event){
//        LivingEntity le = event.getTarget().getEntity();
//        double dmg = event.getDamage();
//        event.getTarget().registerLastHitTime();
//        le.damage(0);
//        le.setHealth(le.getHealth() - dmg);
//        getInstance().getServer().broadcastMessage("curr health - " + event.getTarget().getEntity().getHealth());
//        le.setLastDamageCause(new EntityDamageByEntityEvent(event.getDamager(), le,
//                EntityDamageEvent.DamageCause.ENTITY_ATTACK, dmg));
//        event.getTarget().onHit();
//        if(le instanceof Mob){
//            Mob mob = (Mob) le;
//            if(mob.getTarget() == null) mob.setTarget(event.getDamager());
//        }
//    }
//
//    @EventHandler
//    public void onDamageUpdate(EntityDamageEvent event){
//        CustomPVPMob customEntity = getInstance().getCustomPVPMobManager().getCustomEntity(event.getEntity());
//        if(customEntity == null) return;
//        if(event.getEntity() != customEntity.getEntity()){
//            event.setCancelled(true);
//            return;
//        }
//        getInstance().getServer().getPluginManager().callEvent(new CustomPVPMobDamageEvent(customEntity, event.getCause(), event.getDamage()));
//    }
//
//    @EventHandler
//    public void onCustomMobDamage(CustomPVPMobDamageEvent event){
//        CustomPVPMob customEntity = event.getEntity();
//        customEntity.getHealthStand().setCustomName(customEntity.healthString(event.getDamage()));
//    }
//
//    @EventHandler
//    public void onDeath(EntityDeathEvent event){
//        CustomPVPMob customEntity = getInstance().getCustomPVPMobManager().getCustomEntity(event.getEntity());
//        if(customEntity == null) return;
//        event.getDrops().clear();
//        customEntity.onDeath();
//    }
//
//}
