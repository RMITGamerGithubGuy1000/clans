package me.marco.Events.PvPItem;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import me.marco.PvPItems.PvPItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PvPItemListener extends CListener<Core> {

    public PvPItemListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onPvPItemCast(PvPItemCastEvent event){
        Player player = event.getActivator();
        PvPItem pvpItem = event.getPvPItem();
        if(!pvpItem.isCanCastSafeZone() &&
                !getInstance().getClanManager().canCastInSafeZone(player, pvpItem.getName(event.getAction()))) return;
        pvpItem.onUseage(player, event.getAction());
    }

    @EventHandler
    public void onPvPItemInteract(PlayerInteractEvent event){
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        PvPItem pvpItem = getInstance().getPvPItemManager().isConsumeable(getItemInHand(player, EquipmentSlot.HAND));
        if(pvpItem != null){
            event.setCancelled(true);
            getInstance().getServer().getPluginManager().callEvent(new PvPItemCastEvent(player, pvpItem, event.getAction()));
            return;
        }
    }

    public ItemStack getItemInHand(Player player, EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.HAND) {
            return player.getInventory().getItemInMainHand();
        }
        if (equipmentSlot == EquipmentSlot.OFF_HAND) {
            return player.getInventory().getItemInOffHand();
        }
        return null;
    }

}
