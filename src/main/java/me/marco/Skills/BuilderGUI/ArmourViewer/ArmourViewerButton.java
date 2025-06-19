package me.marco.Skills.BuilderGUI.ArmourViewer;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.GUI.Button;
import me.marco.Skills.BuilderGUI.ClassBuilder.ClassBuildMenu;
import me.marco.Skills.Builders.BuildsContainer;
import me.marco.Skills.Builders.eClassType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ArmourViewerButton extends Button {

    private eClassType armourClass;

    public ArmourViewerButton(eClassType armourClass, Core core, int slot, String name, List<String> description, Material material) {
        super(core, slot, name, description, material);
        this.armourClass = armourClass;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        Client client = getInstance().getClientManager().getClient(player);
        BuildsContainer buildsContainer = client.getBuildsContainer();
        ClassBuildMenu classBuildMenu = (ClassBuildMenu) getInstance().getMenuManager().getMenu(this.armourClass.getName() + " Class Builder");
        classBuildMenu.openMenu(player);
    }
}
