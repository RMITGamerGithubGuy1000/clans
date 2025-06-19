package me.marco.Skills.BuilderGUI.ClassBuilder;

import me.marco.Base.Core;
import me.marco.GUI.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ClassBuildButton extends Button {

    public ClassBuildButton(Core core, int slot, String name, List<String> description, Material material) {
        super(core, slot, name, description, material);
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {

    }
}
