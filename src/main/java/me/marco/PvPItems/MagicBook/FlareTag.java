package me.marco.PvPItems.MagicBook;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.ItemTask;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;

public class FlareTag extends ItemTask {

    private Arrow arrow;
    private final int toCountTo = 2;
    private int count = 0;

    public FlareTag(Client owner, Arrow arrow, Core instance) {
        super(owner, "FlareTag", 0, false, instance);
        this.arrow = arrow;
    }

    public boolean checkExpiry(){
        return this.arrow.isOnGround() || this.arrow.isDead();
    }

    @Override
    public void onTick() {
        if(count >= toCountTo){
            count = 0;
            playEffect();
            return;
        }
        count++;
    }


    public void playEffect(){
        UtilParticles.playBlockParticle(arrow.getLocation(), Material.LAVA, true);
    }

    @Override
    public void onExpiry() {

    }

}
