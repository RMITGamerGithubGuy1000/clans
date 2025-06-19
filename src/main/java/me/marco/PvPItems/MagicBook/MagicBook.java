package me.marco.PvPItems.MagicBook;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.PvPItems.AbilityItem;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MagicBook extends AbilityItem {

    public MagicBook(Core core) {
        super(core, "Magic Book", Material.BOOK);
    }

    private List<Snowball> snowballList = new ArrayList<Snowball>();
    private List<Arrow> flarelist = new ArrayList<Arrow>();

    @Override
    public String getName(Action action) {
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            return "Frostbolt";
        }
        return "Flare";
    }

    @Override
    public boolean useageCheck(Player player, Action action) {
        if(!hasMana(player, action)) return false;
        return this.getCDManager().add(player, this, action, true);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            useFrostbolt(player, action);
        }
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            useFlare(player, action);
        }
    }

    public void useFlare(Player player, Action action){
        if(!useageCheck(player, action)) return;
        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setCustomName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Flare");
        arrow.setCustomNameVisible(true);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(1);
        arrow.setVelocity(tosend);
        UtilParticles.playBlockParticle(player, Material.LAVA, true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 5f);
        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getPvPItemManager().addTask(new FlareTag(client, arrow, getInstance()));
        this.flarelist.add(arrow);
    }

    public void useFrostbolt(Player player, Action action){
        if(!useageCheck(player, action)) return;
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setCustomName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Frostbolt");
        snowball.setCustomNameVisible(true);
        UtilParticles.playBlockParticle(player, Material.SNOW_BLOCK, true);
        player.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_ARROW_SHOOT, 0.5f, 5f);
        this.snowballList.add(snowball);
    }

    public void implodeFlare(Arrow arrow, Player shooter){
        arrow.remove();
        UtilParticles.playEffect(arrow.getLocation().add(0, 1, 0).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(2, 1, 0).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(0, 1, 2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(-2, 1, 0).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(0, 1, -2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(-2, 1, -2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(-2, 1, 2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(2, 1, -2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(arrow.getLocation().add(2, 1, 2).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        for(Entity entity : arrow.getNearbyEntities(3, 3, 3)){
            if(!(entity instanceof Player)) continue;
            Player nearbyPlayer = (Player) entity;
            nearbyPlayer.setFireTicks(40);
        }
    }

    @Override
    public double getCooldown(Action action) {
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            return 1.5;
        }
        return 16;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Snowball) {
            if (!(event.getEntity() instanceof Player)) return;
            Snowball snowball = (Snowball) event.getDamager();
            if (!(snowball.getShooter() instanceof Player)) return;
            if (!this.snowballList.contains(snowball)) return;
            this.snowballList.remove(snowball);
            Player shooter = (Player) snowball.getShooter();
            Player target = (Player) event.getEntity();
            Client client = getInstance().getClientManager().getClient(shooter);
            if (client.isFriendly(getInstance().getClientManager().getClient(target))) return;
            target.setSprinting(false);
//            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(target, shooter, PotionEffectType.SLOWNESS, 60, 0));
            Block block = target.getLocation().getBlock();
            if (block.getType() != Material.AIR) return;
            getInstance().getBlockChangeManager().addBlockChange(new BlockChange(block, Material.SNOW, 3));
            return;
        }
        if(event.getDamager() instanceof Arrow){
            if (!(event.getEntity() instanceof Player)) return;
            Arrow arrow = (Arrow) event.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            if (!this.flarelist.contains(arrow)) return;
            Player player = (Player) arrow.getShooter();
            this.flarelist.remove(arrow);
            event.setCancelled(true);
            implodeFlare(arrow, player);
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if(!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();
        if(!this.flarelist.contains(arrow)) return;
        if(!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();
        implodeFlare(arrow, player);
    }

    public int getMana(Action action){
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            return 5;
        }
        return 10;
    }

}
