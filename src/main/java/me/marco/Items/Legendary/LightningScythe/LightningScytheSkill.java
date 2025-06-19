package me.marco.Items.Legendary.LightningScythe;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Items.Legendary.LegendaryItemSkill;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class LightningScytheSkill extends LegendaryItemSkill {

    public LightningScytheSkill(Core instance) {
        super(instance, "Lightning Scythe", true, true, false, false);
    }

    @Override
    public boolean useageCheck(Player player) {
        return this.hasMana(player);
    }

    @Override
    public int getMana() {
        return 20;
    }

    @Override
    public double getCooldown() {
        return 120;
    }

    @Override
    public void activate(Player player) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        World world = player.getWorld();

        // Ray trace up to 50 blocks to find target
        RayTraceResult result = world.rayTraceBlocks(eyeLoc, direction, 50);
        if (result == null || result.getHitPosition() == null) return;

        Location targetLoc = result.getHitPosition().toLocation(world);

        // Strike fake lightning
        targetLoc.getWorld().strikeLightningEffect(targetLoc);
        world.playSound(targetLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5.0f, 1.0f);

        // Damage nearby players (excluding the caster)
        double radius = 2.0;
        for (Player nearby : getInstance().getServer().getOnlinePlayers()) {
            if (nearby.equals(player)) continue;
            if (nearby.getWorld().equals(world) && nearby.getLocation().distance(targetLoc) <= radius) {
                getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(nearby, player, PotionEffectType.SLOWNESS, 160, 4));
                getInstance().getServer().getPluginManager().callEvent(new PureDamageEvent(player, nearby, 3, "Lightning Scythe"));
                handleShock(nearby);
            }
        }
    }

    private void handleShock(Player player){
        new BukkitRunnable() {
            double c = 20;

            public void run() {
                if (c <= 0) {
                    this.cancel();
                }
                c--;
                player.damage(0.01);
                player.setNoDamageTicks(0);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 2, 1);
                player.setVelocity(new Vector(0, 0, 0));
            }
        }.runTaskTimer(getInstance(), 0, 1);
    }

}
