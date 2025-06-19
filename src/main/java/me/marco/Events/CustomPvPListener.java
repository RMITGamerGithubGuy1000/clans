package me.marco.Events;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.*;
import me.marco.Skills.Builders.eClassType;
import me.marco.Utility.UtilVelocity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public class CustomPvPListener extends CListener<Core> {

    public CustomPvPListener(Core core) {
        super(core);
    }

    public final String TAKE_DAMAGE_PREFIX = "TAKEDMGCD";
    private final String TAKE_PURE_DAMAGE_PREFIX = "TAKEPUREDMGCD";

    private Material[] customSwords = { Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD };

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhysicalPvP(PhysicalDamageEvent event){
        Player target = event.getTarget();
        Player damager = event.getDamager();
        if(!getInstance().getCooldownManager().add(target, TAKE_DAMAGE_PREFIX,null, 0, .5, false)){
            event.setCancelled(true);
            return;
        }
        if(event.isCancelled()) return;

        if(event.getTarget() != null && event.getDamager() != null && event.getCause() != null){
            getInstance().getTagManager().handleDamageTagging(damager, target, event.getCause());
        }
        event.setDamage(calculateArmourResistance(event.getDamage(), target));
        playDamage(target, event.getDamage());

        Vector trajectory = UtilVelocity.getTrajectory2d(damager, target);
        // used to be .8
        trajectory.multiply(0.3D);
        trajectory.setY(Math.abs(trajectory.getY()));

        if(!event.isKnockbackCancelled()) {
            UtilVelocity.velocity(target,
                    trajectory, 0.3D + trajectory.length() * 0.1D, false, 0.0D,
                    .1d, 1.5d, true);
        }

        getInstance().getUtilInvisibility().checkInvisibility(damager);

        playArmourSound(target);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArrowPvP(ArrowDamageEvent event){
        Player target = event.getTarget();
        Player shooter = event.getShooter();
        Arrow arrow = event.getArrow();
        Client shooterClient = getInstance().getClientManager().getClient(shooter);
        Client targetClient = getInstance().getClientManager().getClient(target);
        arrow.remove();
        if(shooterClient.isFriendly(targetClient)){
            getInstance().getChat().sendClans(shooter, "You cannot harm " +
                    getInstance().getChat().getClanRelation(shooterClient, targetClient) + target.getName() + "");
            return;
        }
        if(getInstance().getLandManager().isInSafezone(target)){
            getInstance().getChat().sendNoHarmInSafeZone(shooter, target);
            return;
        }

        if(!getInstance().getCooldownManager().add(target, TAKE_DAMAGE_PREFIX,null, 0, .5, false)){
            event.setCancelled(true);
            return;
        }
        if(event.isCancelled()) return;

        if(event.getTarget() != null && event.getShooter() != null){
            getInstance().getTagManager().handleDamageTagging(shooter, target, event.getCause());
        }
        event.setDamage(calculateArmourResistance(event.getDamage(), target));
        playDamage(target, event.getDamage());

        Vector trajectory = UtilVelocity.getTrajectory2d(arrow, target);
        // used to be .8
        trajectory.multiply(0.3D);
        trajectory.setY(Math.abs(trajectory.getY()));

        if(!event.isKnockbackCancelled()) {
            UtilVelocity.velocity(target,
                    trajectory, 0.3D + trajectory.length() * 0.1D, false, 0.0D,
                    .1d, 1.5d, true);
        }

        getInstance().getUtilInvisibility().checkInvisibility(shooter);

        playArmourSound(target);
    }

    public double calculateArmourResistance(double damage, Player target){
        Client targetClient = getInstance().getClientManager().getClient(target);
        if(!targetClient.hasActiveBuild()) return damage;
        return damage - (targetClient.getActiveBuild().getClassType().getResistancePoints() * damage);
    }

    public void playArmourSound(Player target){
        Client targetClient = getInstance().getClientManager().getClient(target);
        if(targetClient.hasActiveBuild()){
            eClassType classType = targetClient.getActiveBuild().getClassType();
            target.getWorld().playSound(target.getLocation(), classType.getSound(), 1, classType.getPitch());
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPureDamage(PureDamageEvent event){
        Player target = event.getTarget();
        Player damager = event.getDamager();
        if(event.isCancelled()) return;
        if(!getInstance().getCooldownManager().add(target, TAKE_PURE_DAMAGE_PREFIX,null, 0, .5, false)){
            event.setCancelled(true);
            return;
        }
        if(event.getTarget() != null && event.getDamager() != null && event.getCause() != null){
            getInstance().getTagManager().handleDamageTagging(damager, target, event.getCause());
        }
        playDamage(target, event.getDamage());
        playArmourSound(target);
    }

    public void playDamage(Player player, double damage){
        player.damage(damage);
        spawnBlood(player, damage);
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private final int PICKUP_MAX_DELAY = Integer.MAX_VALUE;

    public void spawnBlood(Player player, double damage){
        double amount = Math.round(damage*100.0)/100.0;
        int time = 5990;
        Item item = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.RED_DYE));
        item.setPickupDelay(time);
        item.setTicksLived(time);
        item.setCustomName(ChatColor.RED + "-" + amount + "");
        item.setCustomNameVisible(true);
    }

}
