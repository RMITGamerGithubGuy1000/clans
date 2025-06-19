package me.marco.WorldEvent.Bosses.Lycanthrope.Skills.BoneRush;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.StaticKeys;
import me.marco.PvPItems.Consumeables.ImpulseGrenade.ImpulseGrenadeTag;
import me.marco.WorldEvent.BossSkill;
import me.marco.WorldEvent.Bosses.Lycanthrope.Lycanthrope;
import me.marco.WorldEvent.Bosses.Lycanthrope.Skills.WolfSpawn.NMSWolfSpawn;
import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Bonerush extends BossSkill {

    public Bonerush() {
        super("Bone Rush");
    }

    @Override
    public void cast(WorldBoss worldBoss, Core instance, int amplifier) {
        if(!(worldBoss instanceof Lycanthrope)) return;
        Lycanthrope lycanthrope = (Lycanthrope) worldBoss;
        LivingEntity livingEntity = lycanthrope.getEntity();
        for(Entity e : livingEntity.getNearbyEntities(40, 20, 40)){
            if(e instanceof Player){
                Player nearby = (Player) e;
                throwBone(instance, livingEntity, nearby, amplifier);
            }
        }
    }

    private void throwBone(Core instance, LivingEntity livingEntity, Player player, int amp){
        Item item = livingEntity.getWorld().dropItem(livingEntity.getLocation().add(0, 2.5, 0), new ItemStack(Material.BONE));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Bone Rush");
        item.setCustomNameVisible(true);
        Vector toPlayer = player.getLocation().add(0, 2, 0).toVector().subtract(livingEntity.getLocation().add(0, 2, 0).toVector()).normalize().multiply(1.6);
        item.setVelocity(toPlayer);
        long distance = (long) (player.getLocation().distance(livingEntity.getLocation()));
        new BukkitRunnable(){
            public void run(){
                item.remove();
                player.damage(calcDamage(amp));
            }
        }.runTaskLater(instance, distance);
    }

    private double calcDamage(int amp){
        return 2 * amp;
    }

}
