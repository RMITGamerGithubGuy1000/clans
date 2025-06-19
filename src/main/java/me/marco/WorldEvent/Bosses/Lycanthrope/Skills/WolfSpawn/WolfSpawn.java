package me.marco.WorldEvent.Bosses.Lycanthrope.Skills.WolfSpawn;

import me.marco.Base.Core;
import me.marco.CustomEntities.StaticKeys;
import me.marco.WorldEvent.BossSkill;
import me.marco.WorldEvent.Bosses.Lycanthrope.Lycanthrope;
import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.persistence.PersistentDataType;

public class WolfSpawn extends BossSkill {

    public WolfSpawn() {
        super("Wolf Call");
    }

    @Override
    public void cast(WorldBoss worldBoss, Core instance, int amplifier) {
        if(!(worldBoss instanceof Lycanthrope)) return;
        Lycanthrope lycanthrope = (Lycanthrope) worldBoss;
        LivingEntity livingEntity = lycanthrope.getEntity();
        for(Entity e : livingEntity.getNearbyEntities(40, 20, 40)){
            if(e instanceof Player){
                Player nearby = (Player) e;
                NMSWolfSpawn nmsWolfSpawn = new NMSWolfSpawn(instance, lycanthrope.getEntity().getLocation(), nearby, calcDamage(amplifier), calcSpeed(amplifier));
                Wolf wolf = (Wolf) nmsWolfSpawn.getBukkitEntity();
                wolf.setCustomNameVisible(true);
                wolf.setCustomName(ChatColor.RED + "Wolf");
                wolf.getAttribute(Attribute.MAX_HEALTH).setBaseValue(calcHealth(amplifier));
                wolf.getPersistentDataContainer().set(StaticKeys.damageableNMSKey, PersistentDataType.BYTE, (byte) (1));
            }
        }
    }

    private double calcHealth(int amp){
        return 2 * amp;
    }

    private double calcDamage(int amp){
        return 2 * amp;
    }

    private double calcSpeed(int amp){
        return 1.1 * amp;
    }



}
