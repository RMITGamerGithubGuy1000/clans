package me.marco.PvPItems.Consumeables.Enderpearl;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class EnderpearlTag extends ItemTask {

    private Player sitting;
    private Item pearl;
    private final int toCountTo = 5;
    private int count = 0;

    public EnderpearlTag(Player sitting, Client owner, Item pearl, Core instance) {
        super(owner, "EnderpearlTag", 0, false, instance);
        this.pearl = pearl;
        this.sitting = sitting;
        pearl.addPassenger(sitting);
    }

    public boolean isDeadClause(){
        return this.pearl.isDead();
    }

    public boolean checkExpiry(){
        return this.pearl.isOnGround() || this.pearl.isDead() || this.pearl.getPassengers().size() < 1;
    }

    @Override
    public void onTick() {
        if(count >= toCountTo){
            count = 0;
            this.pearl.setFallDistance(0);
            this.sitting.setFallDistance(0);
            playEffect();
            return;
        }
        count++;
    }

    public void burstPearl(){
        if(pearl.isDead()) return;
        this.sitting.setFallDistance(0);
        this.pearl.setFallDistance(0);
        pearl.remove();
        UtilParticles.playBlockParticle(pearl.getLocation(), Material.NETHER_PORTAL, true);
        pearl.getWorld().playEffect(pearl.getLocation(), Effect.ENDER_SIGNAL, 1);
        pearl.getWorld().playSound(pearl.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
    }

    public void playEffect(){
        UtilParticles.playEffect(pearl.getLocation(), Particle.FIREWORK, 0, 0, 0, 0, 1);
        UtilParticles.playBlockParticle(pearl.getLocation(), Material.NETHER_PORTAL, true);
    }

    @Override
    public void onExpiry() {
        burstPearl();
    }

}
