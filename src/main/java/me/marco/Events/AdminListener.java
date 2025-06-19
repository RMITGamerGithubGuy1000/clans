package me.marco.Events;

import me.marco.Base.Core;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdminListener extends CListener<Core>{

    public AdminListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(getInstance().getFieldsManager().isFieldsModeActive(player)){
                Block block = event.getClickedBlock();
                event.setCancelled(true);
                if(getInstance().getFieldsManager().isFieldsBlock(block)){
                    getInstance().getChat().sendModule(player, "This is already a fields block", "Fields");
                    return;
                }
                getInstance().getChat().sendModule(player, "Fields block added", "Fields");
                getInstance().getFieldsManager().addFieldBlockAndSQL(block);
            }
        }else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(getInstance().getFieldsManager().isFieldsModeActive(player)){
                Block block = event.getClickedBlock();
                event.setCancelled(true);
                if(getInstance().getFieldsManager().isFieldsBlock(block)){
                    getInstance().getChat().sendModule(player, "Fields block removed", "Fields");
                    getInstance().getFieldsManager().removeFieldsBlockAndSQL(block);
                    return;
                }

            }
        }
    }
}
