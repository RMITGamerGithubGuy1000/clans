package me.marco.PvPItems.Consumeables.MushroomSoup;

import me.marco.Base.Core;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.PvPItems.Consumeables.Consumeable;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MushieSoup extends Consumeable {

    public MushieSoup(Core core) {
        super(core, "Mushie Soup", Material.MUSHROOM_STEW);
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        eatSoup(player);
    }

    public void eatSoup(Player player){
        double toBe = player.getHealth() + 4.5;
        if(toBe > 20) toBe = 20;
        getInstance().getUtilPvP().setPvPHealth(player, toBe);
        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.REGENERATION, 60, 0));
//        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
        UtilParticles.playBlockParticle(player, Material.RED_MUSHROOM, true);
        UtilParticles.playBlockParticle(player, Material.BROWN_MUSHROOM, true);
    }

    @Override
    public double getCooldown(Action action) {
        return 20;
    }

    @Override
    public int getMana(Action action) {
        return 0;
    }
}
