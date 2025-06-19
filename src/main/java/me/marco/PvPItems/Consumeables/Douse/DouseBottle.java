package me.marco.PvPItems.Consumeables.Douse;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.PvPItems.Consumeables.Consumeable;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DouseBottle extends Consumeable {

    public DouseBottle(Core core) {
        super(core, "Douse Bottle", Material.POTION);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        useDouse(player);
    }

    public void useDouse(Player player){
        player.setFireTicks(0);
//        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0));
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.FIRE_RESISTANCE, 60, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_SWIM, 1f, 5f);
        UtilParticles.playBlockParticle(player.getLocation().add(0, 1.5, 0), Material.WATER);
    }

    @Override
    public double getCooldown(Action action) {
        return 8;
    }

    @Override
    public int getMana(Action action) {
        return 0;
    }
}
