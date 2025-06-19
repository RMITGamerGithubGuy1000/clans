package me.marco.PvPItems.Consumeables.Incendiary;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class IncendiaryGrenadeTag extends ItemTask {

    private Item grenade;
    private final int toCountTo = 10;
    private int count = 0;

    public IncendiaryGrenadeTag(Client owner, Item grenade, Core instance) {
        super(owner, "IncendiaryGrenadeTag", 5, instance);
        this.grenade = grenade;
    }

    public boolean isDeadClause(){
        return this.grenade.isDead();
    }

    @Override
    public void onTick() {
        if(count >= toCountTo){
            count = 0;
            burn();
            return;
        }
        count++;
    }

    public void burn(){
        if(grenade.isDead()) return;
        for(Entity entity : grenade.getNearbyEntities(1.5, 1.5, 1.5)){
            if(!(entity instanceof Player)) continue;
            entity.setFireTicks(60);
        }
        grenade.getWorld().playSound(grenade.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, .1f, .5f);
        UtilParticles.playBlockParticle(grenade.getLocation(), Material.LAVA, true);
        grenade.getWorld().playEffect(grenade.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
        grenade.getWorld().playEffect(grenade.getLocation().add(1, 1, 0), Effect.MOBSPAWNER_FLAMES, 1);
        grenade.getWorld().playEffect(grenade.getLocation().add(-1, 1, 0), Effect.MOBSPAWNER_FLAMES, 1);
        grenade.getWorld().playEffect(grenade.getLocation().add(0, 1, 1), Effect.MOBSPAWNER_FLAMES, 1);
        grenade.getWorld().playEffect(grenade.getLocation().add(0, 1, -1), Effect.MOBSPAWNER_FLAMES, 1);
    }

    @Override
    public void onExpiry() {
        grenade.remove();
    }
}