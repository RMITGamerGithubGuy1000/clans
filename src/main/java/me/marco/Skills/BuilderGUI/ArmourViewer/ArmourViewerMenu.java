package me.marco.Skills.BuilderGUI.ArmourViewer;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.GUI.Button;
import me.marco.Quests.GUI.DynamicMenu;
import me.marco.Skills.Builders.eClassType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmourViewerMenu extends DynamicMenu {

    public ArmourViewerMenu(Core core) {
        super(core, ChatColor.DARK_AQUA + "Class Builder", 27);
    }

    @Override
    public Inventory generateUniqueMenu(Player player) {
        Client client = getInstance().getClientManager().getClient(player);
        //eClassType armourClass, Core core, int slot, String name, List<String> description, Material material
        ArmourViewerButton rogue = new ArmourViewerButton(eClassType.ROGUE, getInstance(), 9, eClassType.ROGUE.getColour() + ChatColor.BOLD.toString() + eClassType.ROGUE.getName(),
                Arrays.asList(eClassType.ROGUE.getColour() + eClassType.ROGUE.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.ROGUE.getHelmet());

        ArmourViewerButton archer = new ArmourViewerButton(eClassType.RANGER, getInstance(), 10, eClassType.RANGER.getColour() + ChatColor.BOLD.toString() + eClassType.RANGER.getName(),
                Arrays.asList(eClassType.RANGER.getColour() + eClassType.RANGER.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.RANGER.getHelmet());

        ArmourViewerButton guardian = new ArmourViewerButton(eClassType.GUARDIAN, getInstance(), 12, eClassType.GUARDIAN.getColour() + ChatColor.BOLD.toString() + eClassType.GUARDIAN.getName(),
                Arrays.asList(eClassType.GUARDIAN.getColour() + eClassType.GUARDIAN.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.GUARDIAN.getHelmet());

        ArmourViewerButton samurai = new ArmourViewerButton(eClassType.SAMURAI, getInstance(), 14, eClassType.SAMURAI.getColour() + ChatColor.BOLD.toString() + eClassType.SAMURAI.getName(),
                Arrays.asList(eClassType.SAMURAI.getColour() + eClassType.SAMURAI.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.SAMURAI.getHelmet());

        ArmourViewerButton mage = new ArmourViewerButton(eClassType.MAGE, getInstance(), 16, eClassType.MAGE.getColour() + ChatColor.BOLD.toString() + eClassType.MAGE.getName(),
                Arrays.asList(eClassType.MAGE.getColour() + eClassType.MAGE.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.MAGE.getHelmet());

        ArmourViewerButton warrior = new ArmourViewerButton(eClassType.WARRIOR, getInstance(), 17, eClassType.WARRIOR.getColour() + ChatColor.BOLD.toString() + eClassType.WARRIOR.getName(),
                Arrays.asList(eClassType.WARRIOR.getColour() + eClassType.WARRIOR.getName() + "" + ChatColor.YELLOW + " class builder"), eClassType.WARRIOR.getHelmet());
        List<Button> uniqueButtons = new ArrayList<Button>();
        uniqueButtons.add(rogue);
        uniqueButtons.add(archer);
        uniqueButtons.add(guardian);
        uniqueButtons.add(samurai);
        uniqueButtons.add(mage);
        uniqueButtons.add(warrior);
        return generateInventory(client, uniqueButtons);
    }
}