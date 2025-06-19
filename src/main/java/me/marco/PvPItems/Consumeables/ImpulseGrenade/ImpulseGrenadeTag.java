package me.marco.PvPItems.Consumeables.ImpulseGrenade;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import me.marco.Utility.UtilVelocity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ImpulseGrenadeTag extends ItemTask {

    private Item grenade;
    private int toCountTo = 20;
    private int count = 0;

    public ImpulseGrenadeTag(Client owner, Item grenade, Core instance) {
        super(owner, "ImpulseGrenadeTag", 2, instance);
        this.grenade = grenade;
    }

    public boolean isDeadClause(){
        return this.grenade.isDead();
    }

    @Override
    public void onTick() {
        if (count >= toCountTo) {
            playAlarm();
            count = 0;
            return;
        }
        if(count % 10 == 0) playSound();
        count++;
    }

    public void playAlarm(){
        grenade.getWorld().playSound(grenade.getLocation().add(0, .5, 0), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 5);
        UtilParticles.playEffect(grenade.getLocation(), Particle.FIREWORK, 0, 0, 0, 0, 1);
    }

    public void playSound() {
        UtilParticles.playEffect(grenade.getLocation().add(0, .5, 0), Particle.FIREWORK, 0, 0, 0, 0, 1);
        grenade.getWorld().playSound(grenade.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 3);
    }

    public void implode(){
        if(grenade.isDead()) return;
        Location center = grenade.getLocation().add(0, .5, 0);
        UtilParticles.playBlockParticle(center, Material.BEDROCK, true);
        UtilParticles.drawColourCircle(center,2, 0, 0, 200, 150, 200, .5f);
        for(Entity near : grenade.getNearbyEntities(2, 2, 2)){
            if(!(near instanceof Player)) continue;
            Player player = (Player) near;
            Vector direction = UtilVelocity.getVector(player.getLocation().toVector().subtract(grenade.getLocation().toVector()).normalize());
            player.setVelocity(direction);
        }
    }

    @Override
    public void onExpiry() {
        implode();
        grenade.remove();
    }

}