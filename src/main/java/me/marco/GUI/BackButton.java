package me.marco.GUI;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.List;

public class BackButton extends Button{

    private Menu backTo;

    public BackButton(Core core, int slot, Menu backTo) {
        super(core, slot, ChatColor.RED + ChatColor.BOLD.toString() + "Back", Arrays.asList(ChatColor.YELLOW + "Back to last"), Material.REDSTONE_BLOCK);
        this.backTo = backTo;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        this.backTo.openMenu(player);
    }
}
