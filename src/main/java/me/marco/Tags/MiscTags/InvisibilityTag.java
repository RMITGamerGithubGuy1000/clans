package me.marco.Tags.MiscTags;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Tags.Tag;
import me.marco.Utility.UtilParticles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class InvisibilityTag extends Tag {

    private Player toHide;
    private int count = 0;
    private final int toCountTo = 5;

    public InvisibilityTag(Player playerToHide, Client owner, long expiry, Core instance) {
        super(owner, "Invisibility", expiry, instance);
        toHide = playerToHide;
    }

    @Override
    public void onTick() {
        if(count >= toCountTo){
            count = 0;
            playEffect(toHide);
            return;
        }
        count++;
    }

    public void playEffect(Player player){
        Location loc = player.getLocation().getBlock().getLocation().add(.5, 1, .5);
        UtilParticles.playEffect(loc, Particle.SMOKE, 0, 0, 0, 0, 1);
        UtilParticles.playEffect(loc, Particle.CAMPFIRE_COSY_SMOKE, 0, 0, 0, 0, 1);
    }

    @Override
    public void onExpiry() {
        getInstance().getUtilInvisibility().revealPlayer(toHide);
    }
}
