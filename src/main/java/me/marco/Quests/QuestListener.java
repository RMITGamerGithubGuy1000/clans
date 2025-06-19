package me.marco.Quests;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Panda;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class QuestListener extends CListener<Core> {

    public QuestListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof Panda)) return;
        Panda panda = (Panda) event.getRightClicked();
        if(!panda.isCustomNameVisible()) return;
        String name = ChatColor.stripColor(panda.getCustomName());
        if(!event.getHand().equals(EquipmentSlot.HAND)) return;
        if(name.equalsIgnoreCase("Questie (Quest Panda)"))
        event.setCancelled(true);
        getInstance().getQuestManager().openQuestMenu(event.getPlayer());
    }

}
