package me.marco.Events;

import me.marco.Base.Core;
import me.marco.CustomEntities.StaticKeys;
import me.marco.Events.WorldBosses.BossDamageEvent;
import me.marco.Events.WorldBosses.BossDeathEvent;
import me.marco.Events.WorldBosses.BossSpawnEvent;
import me.marco.Utility.UtilVelocity;
import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WorldEventListener extends CListener<Core>{

    public WorldEventListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onBossSpawn(BossSpawnEvent event){
        WorldBoss worldBoss = event.getSpawned();
        worldBoss.handleEntitySpawn();
        Location location = worldBoss.getEntity().getLocation();
        getInstance().getChat().broadcastMessage(getInstance().getChat().highlightText + worldBoss.getName() +
                getInstance().getChat().textColour + " has been spawned at: " +
                getInstance().getChat().highlightNumber + "X:" + ((int) location.getX()) + ", " +
                getInstance().getChat().highlightNumber + "Z:" + ((int) location.getZ()) + "" +
                getInstance().getChat().textColour + ".", "World Boss");
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 1);
        worldBoss.onSummon();
    }

    @EventHandler
    public void onBossDamage(BossDamageEvent event){
        WorldBoss worldBoss = event.getHit();
        LivingEntity entity = worldBoss.getEntity();
        Player damager = event.getDamager();
        if(!getInstance().getCooldownManager().add(damager, getMobCDName(entity), null, 0, .5, false)) return;
        worldBoss.getEntity().damage(event.getDamage());
        worldBoss.onDamage(event.getDamager());
        worldBoss.spawnBlood(event.getDamage());

        Vector trajectory = UtilVelocity.getTrajectory2d(damager, entity);
        // used to be .8
        trajectory.multiply(0.3D);
        trajectory.setY(Math.abs(trajectory.getY()));

        UtilVelocity.velocity(entity,
                trajectory, 0.3D + trajectory.length() * 0.1D, false, 0.0D,
                .1d, 1.5d, true);

        if(entity.getHealth() <= 0){
            getInstance().getServer().getPluginManager().callEvent(new BossDeathEvent(worldBoss, event.getDamager()));
        }

        worldBoss.updateBossBar();

    }

    @EventHandler
    public void onBossDeath(BossDeathEvent event){
        WorldBoss worldBoss = event.getSpawned();
        Location location = worldBoss.getEntity().getLocation();
        getInstance().getChat().broadcastMessage(getInstance().getChat().highlightText + worldBoss.getName() +
                getInstance().getChat().textColour + " has been slain by " + ChatColor.WHITE + event.getKiller().getName() +
                getInstance().getChat().textColour + " at: " +
                getInstance().getChat().highlightNumber + "X:" + ((int) location.getX()) + ", " +
                getInstance().getChat().highlightNumber + "Z:" + ((int) location.getZ()) + "" +
                getInstance().getChat().textColour + ".", "World Boss");
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 1);
        worldBoss.onDeath(event.getKiller());
        getInstance().getLegendaryItemManager().getCustomItemList().get(0).dropLegendaryItem(worldBoss.getEntity().getLocation().add(0, 5, 0));
        worldBoss.getBossBar().setProgress(worldBoss.getHealth());
    }

    private String getMobCDName(LivingEntity hitMob){
        return hitMob.getUniqueId().toString() + "_HITCD";
    }

}
