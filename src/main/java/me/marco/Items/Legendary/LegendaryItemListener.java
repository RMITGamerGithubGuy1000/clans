package me.marco.Items.Legendary;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.CListener;
import me.marco.Events.Skills.SkillActivateEvent;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Data.ISkills.ISkill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LegendaryItemListener extends CListener<Core> {

    public LegendaryItemListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        ItemStack itemStack = getItemInHand(player, EquipmentSlot.HAND);
        if(!itemStack.hasItemMeta()) return;
        ItemMeta im = itemStack.getItemMeta();
        if(!im.hasDisplayName()) return;
        String name = ChatColor.stripColor(im.getDisplayName());
        CustomItem customItem = getInstance().getLegendaryItemManager().findByName(name);
        if(customItem == null) return;
        if(!getInstance().getCooldownManager().add(player, name, "Legendary", 0, customItem.getAbility().getCooldown(), true)) return;
        customItem.getAbility().activate(player);

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
