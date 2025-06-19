package me.marco.Skills.BuilderGUI;

import me.marco.Base.Core;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BackButton extends Button {

    private Menu prevMenu;

    public BackButton(Menu prevMenu, Core core, int slot) {
        super(core, slot, ChatColor.RED + "Back", Arrays.asList(ChatColor.YELLOW + "Back to;", ChatColor.RED + prevMenu.getName()), Material.REDSTONE_BLOCK);
        this.prevMenu = prevMenu;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        this.prevMenu.openMenu(player);
    }
}
