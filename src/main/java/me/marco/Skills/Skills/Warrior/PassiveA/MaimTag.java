package me.marco.Skills.Skills.Warrior.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.PvPItems.ItemTask;
import me.marco.Skills.Builders.eClassType;
import me.marco.Tags.Tag;
import me.marco.Utility.UtilParticles;
import me.marco.Utility.UtilVelocity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class MaimTag extends Tag {

    private Item item;
    private int count = 0;
    private double reduction;
    private Player target;
    private eClassType classType;

    public MaimTag(Client owner, Player target, double reduction, long expiry, Item item, Core instance, eClassType classType) {
        super(owner, "MaimTag", expiry, instance);
        this.item = item;
        this.target = target;
        this.classType = classType;
        this.reduction = reduction;
    }

    public double getReduction(){
        return this.reduction;
    }

    @Override
    public void onTick() {
        dragTo();
        if(count % 15 == 0) {
            count = 0;
            playSound();
        }
        count++;
    }


    private void dragTo(){
        if(target == null) return;

        Vector direction = item.getLocation().toVector().subtract(this.target.getLocation().add(0, 2.5, 0).toVector()).normalize();
//        direction.setX(direction.getX() * -0.1D);
//        direction.setZ(direction.getZ() * -0.1D);
        direction.setY(direction.getY() * .2);
        if(direction.getY() <= 0) direction.setY(0.2);
        item.setVelocity(direction);
        Location loc = item.getLocation();
        UtilParticles.drawRedstoneParticle(loc, 250, 10, 10, 1);
    }

    public void playSound() {
        spawnMaim(target);
        item.getWorld().playSound(item.getLocation().add(0, .5, 0), Sound.ENTITY_SILVERFISH_DEATH, 5, 1);
        UtilParticles.playEffect(item.getLocation(), Particle.CRIMSON_SPORE, 0, 0, 0, 0, 1);
    }

    public void implode(){
        Location center = item.getLocation().add(0, .5, 0);
        UtilParticles.playBlockParticle(center, Material.REDSTONE_BLOCK, false);
    }

    @Override
    public void onExpiry() {
        implode();
        item.remove();
        getInstance().getChat().sendModule(target, "You are no longer " + getInstance().getChat().highlightText + "Maimed" + getInstance().getChat().textColour + ".", classType.getName());
    }

    public void spawnMaim(Player player){
        int time = 5990;
        Item item = player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.REDSTONE));
        item.setPickupDelay(time);
        item.setTicksLived(time);
        item.setCustomName(ChatColor.DARK_RED + "Maimed");
        item.setCustomNameVisible(true);
    }

}