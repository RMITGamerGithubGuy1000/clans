package me.marco.Events;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.StaticKeys;
import me.marco.Events.Clans.CustomEvents.Relations.ClanDominanceUpdateEvent;
import me.marco.Events.PvP.MobDamageEvent;
import me.marco.Events.WorldBosses.BossDamageEvent;
import me.marco.Utility.UtilVelocity;
import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class MobListener extends CListener<Core> {

    public MobListener(Core instance) {
        super(instance);
    }

//    Byte value = entity.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
//    boolean isDamageable = value != null && value == 1;

    @EventHandler
    public void onMobDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) return;
        Entity damaged = event.getEntity();
        if(damaged instanceof Player) return;
        if(!(damaged instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) damaged;
        if (!(livingEntity.getPersistentDataContainer().has(StaticKeys.damageableNMSKey, PersistentDataType.BYTE))) return;
        byte damageNMS = livingEntity.getPersistentDataContainer().get(StaticKeys.damageableNMSKey, PersistentDataType.BYTE);
        if(damageNMS == 0) return;
        if (!(livingEntity.getPersistentDataContainer().has(StaticKeys.damageableWordBossKey, PersistentDataType.BYTE))) return;
        byte damageBossNMS = livingEntity.getPersistentDataContainer().get(StaticKeys.damageableWordBossKey, PersistentDataType.BYTE);
        if(damageBossNMS == 0) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMobDamage(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (!(damaged instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) damaged;
        if (livingEntity.getPersistentDataContainer().has(StaticKeys.damageableWordBossKey, PersistentDataType.BYTE)) {
            byte value = livingEntity.getPersistentDataContainer().get(StaticKeys.damageableWordBossKey, PersistentDataType.BYTE);
            if (value == 1) {
                WorldBoss worldBoss = getInstance().getWorldBossManager().getByEntity(livingEntity);
                if (worldBoss == null) return;
                event.setCancelled(true);
                Entity hitter = event.getDamager();
                if (hitter instanceof Player) {
                    Player damager = (Player) hitter;
                    getInstance().getServer().getPluginManager().callEvent(new BossDamageEvent(worldBoss, worldBoss.getDamageValue(damager), damager));
                } else if (hitter instanceof Arrow) {
                    Arrow arrow = (Arrow) hitter;
                    ProjectileSource shotFrom = arrow.getShooter();
                    if (shotFrom instanceof Player) {
                        Player shooter = (Player) shotFrom;
                        arrow.remove();
                        getInstance().getServer().getPluginManager().callEvent(new BossDamageEvent(worldBoss, worldBoss.getDamageValue(arrow), shooter));
                    }
                }
            }
        }else if (livingEntity.getPersistentDataContainer().has(StaticKeys.damageableNMSKey, PersistentDataType.BYTE)) {
            byte value = livingEntity.getPersistentDataContainer().get(StaticKeys.damageableNMSKey, PersistentDataType.BYTE);
            if (value == 1) {
                event.setCancelled(true);
                Entity hitter = event.getDamager();
                if (hitter instanceof Player) {
                    Player damager = (Player) hitter;

                    if (livingEntity.isCustomNameVisible() && ChatColor.stripColor(livingEntity.getCustomName()).contains(damager.getName()))
                        return;

                    getInstance().getServer().getPluginManager().callEvent(new MobDamageEvent(livingEntity, damager, 1));
                }
                if (hitter instanceof Arrow) {
                    Arrow arrow = (Arrow) hitter;
                    ProjectileSource shotFrom = arrow.getShooter();
                    if (shotFrom instanceof Player) {
                        Player shooter = (Player) shotFrom;
                        if (livingEntity.isCustomNameVisible() && ChatColor.stripColor(livingEntity.getCustomName()).contains(shooter.getName()))
                            return;
                        arrow.remove();
                        getInstance().getServer().getPluginManager().callEvent(new MobDamageEvent(livingEntity, shooter, 1));
                    }
                }
            }
        }
    }

    private String getMobCDName(LivingEntity hitMob){
        return hitMob.getUniqueId().toString() + "_HITCD";
    }

    @EventHandler
    public void onMobDamage(MobDamageEvent event){
        LivingEntity hitMob = event.getHitMob();
        Player damager = event.getDamager();
        Client playerClient = getInstance().getClientManager().getClient(damager);

        if(!getInstance().getCooldownManager().add(damager, getMobCDName(hitMob), null, 0, .5, false)) return;

//        if(!hitMob.isCustomNameVisible()) return;
//        String name = hitMob.getCustomName();
//        if(ChatColor.stripColor(name).contains(damager.getName())) return;
        spawnBloodMob(hitMob, event.getDamage());
        Vector trajectory = UtilVelocity.getTrajectory2d(damager, hitMob);
        // used to be .8
        trajectory.multiply(0.3D);
        trajectory.setY(Math.abs(trajectory.getY()));
        getInstance().getUtilInvisibility().checkInvisibility(damager);

        if(!event.isKnockbackCancelled()) {
            UtilVelocity.velocity(hitMob,
                    trajectory, 0.3D + trajectory.length() * 0.1D, false, 0.0D,
                    .1d, 1.2d, true);
        }

    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getCustomName() != null && entity.isCustomNameVisible()){
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    public void spawnBloodMob(LivingEntity livingEntity, double damage){
        double amount = Math.round(damage*100.0)/100.0;
        int time = 5990;
        Location spawnLoc = !livingEntity.isOnGround() ? livingEntity.getLocation().add(0, -.75, 0) :
                livingEntity.getLocation().add(0, 1.5, 0);
        Item item = livingEntity.getWorld().dropItem(spawnLoc, new ItemStack(Material.BLUE_DYE));
        item.setPickupDelay(time);
        item.setTicksLived(time);
        double maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
        String health = ChatColor.GREEN.toString() + maxHealth + "";
        String toBe = ChatColor.RED.toString() + (livingEntity.getHealth() - damage) + "";
        item.setCustomName(toBe + ChatColor.YELLOW + "/" + health);
        livingEntity.damage(damage);
        livingEntity.playHurtAnimation((float) damage);
        item.setCustomNameVisible(true);
        livingEntity.getWorld().playSound(livingEntity.getLocation(), livingEntity.getHurtSound(), 1f, 1f);
    }


}
