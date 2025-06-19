package me.marco.Skills.Skills.Mage.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import me.marco.Utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class PlaguespreaderTag extends ItemTask {

    private Item item;
    private int toCountTo = 20;
    private int count = 0;
    private double range;
    private Player playerOwner;
    private double damage;

    public PlaguespreaderTag(Client owner, Player playerOwner, double range, long expiry, Item item, Core instance, double damage) {
        super(owner, "PlaguespreaderTag", expiry, instance);
        this.item = item;
        this.range = range;
        this.playerOwner = playerOwner;
        this.damage = damage;
    }

    @Override
    public void onTick() {
        dragTo();
        if (count >= toCountTo) {
            playAlarm();
            makePoison();
            count = 0;
            return;
        }
        if(count % 5 == 0) playSound();
        count++;
    }
    //    public static void smallerCircle(Location center, double radius, double pitch, double yaw, int r, int g, int b, float size) {
    private void makePoison(){
        UtilParticles.playPotionSwirl(item.getLocation(), PotionEffectType.REGENERATION);

        UtilParticles.smallerCircle(item.getLocation(), range, 0, 0, 1, 200, 1, 1f);
        for(Entity entity : item.getNearbyEntities(range, range, range)){
            if(!(entity instanceof Player)) continue;
            Player player = (Player) entity;
            if(player == playerOwner) continue;
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, playerOwner, PotionEffectType.POISON, 80, 0));
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, playerOwner, PotionEffectType.SLOWNESS, 80, 0));
            getInstance().getServer().getPluginManager().callEvent(new PureDamageEvent(playerOwner, player, this.damage, "Plaguespreader"));
        }
    }

    private void dragTo(){
        if(playerOwner == null) return;

        BlockIterator iterator = new BlockIterator(playerOwner.getWorld(), playerOwner
                .getLocation().add(0, 1, 0).toVector(), playerOwner.getEyeLocation()
                .getDirection(), 0, 6);
        Block lastBlock = null;
        while(iterator.hasNext()){
            lastBlock = iterator.next();
        }
        if(lastBlock == null) return;
        Vector direction = item.getLocation().toVector().subtract(lastBlock.getLocation().toVector()).normalize();
        direction.setX(direction.getX() * -0.3D);
        direction.setZ(direction.getZ() * -0.3D);
        direction.setY(direction.getY() * -.1);
        if(direction.getY() < -.1) direction.setY(-0.1);
        item.setVelocity(direction);
        //UtilParticles.drawParticleCircle(item.getLocation(), Particle.SPELL, range, 0, 0, 100, 1, 100, 1);
        Location loc = item.getLocation();
        item.getWorld().spawnParticle(Particle.WITCH, loc.getX(), loc.getY(), loc.getZ(), 1);
    }

    private void poisonNearby(){
        for(Entity e : item.getNearbyEntities(range, range, range)){
            if(!(e instanceof Player)) continue;
            Player player = (Player) e;
            if(player.getUniqueId() == this.getOwner().getUUID()) continue;
        }

    }

    public void playAlarm(){
        item.getWorld().playSound(item.getLocation().add(0, .5, 0), Sound.BLOCK_GRASS_BREAK, 1, 5);
        UtilParticles.playEffect(item.getLocation(), Particle.CRIMSON_SPORE, 0, 0, 0, 0, 1);
    }

    public void playSound() {
        UtilParticles.playEffect(item.getLocation().add(0, .5, 0), Particle.CLOUD, 0, 0, 0, 0, 1);
        item.getWorld().playSound(item.getLocation(), Sound.BLOCK_SOUL_SAND_BREAK, 1, 5);
    }

    public void implode(){
        Location center = item.getLocation().add(0, .5, 0);
        UtilParticles.playBlockParticle(center, Material.BEDROCK, true);
        UtilParticles.drawColourCircle(center,2, 0, 0, 200, 150, 200, .5f);
//        for(Entity near : grenade.getNearbyEntities(2, 2, 2)){
//            if(!(near instanceof Player)) continue;
//            Player player = (Player) near;
//            Vector direction = UtilVelocity.getVector(player.getLocation().toVector().subtract(grenade.getLocation().toVector()).normalize());
//            player.setVelocity(direction);
//        }
    }

    @Override
    public void onExpiry() {
        implode();
        item.remove();
    }

}