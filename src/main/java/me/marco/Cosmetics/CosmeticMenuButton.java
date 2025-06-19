package me.marco.Cosmetics;

import me.marco.Base.Core;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import me.marco.Quests.GUI.DynamicMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CosmeticMenuButton extends Button {

    private Menu toOpen;

    public CosmeticMenuButton(Menu toOpen, Core core, ItemStack itemStack, int slot) {
        super(core, itemStack, slot);
        this.toOpen = toOpen;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        this.toOpen.openMenu(player);
    }
}
