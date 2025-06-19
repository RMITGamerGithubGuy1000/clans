package me.marco.Cosmetics;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.Objects.CosmeticTypes.ArrowTrail;
import me.marco.Events.CListener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class CosmeticsListener extends CListener<Core> {

    public CosmeticsListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event){
        if(!(event.getEntity() instanceof Arrow)) return;
        if(!(event.getEntity().getShooter() instanceof Player)) return;
        Arrow arrow = (Arrow) event.getEntity();
        arrow.setCritical(false);
        Player shooter = (Player) arrow.getShooter();
        Client client = getInstance().getClientManager().getClient(shooter);
        ArrowTrail arrowTrail = client.getCosmeticProfile().getArrowTrail();
        if(arrowTrail == null) return;
        arrowTrail.playEffect(getInstance(), arrow);
    }

}
