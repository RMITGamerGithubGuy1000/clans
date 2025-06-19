package me.marco.Quests.GUI;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.CosmeticMenuButton;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import me.marco.Quests.QuestProgression;
import me.marco.Quests.QuestWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestMenu extends DynamicMenu {

    public QuestMenu(Core core) {
        super(core, "Quests", 27);
        setInventory(generateInventory());
    }

    @Override
    public Inventory generateUniqueMenu(Player player) {
        Client client = getInstance().getClientManager().getClient(player);
        QuestProgression questProgression = client.getQuestProgression();
        List<Button> uniqueButtons = new ArrayList<Button>();
        for(int i = 0; i < 3; i++){
            if(i >= questProgression.getQuestList().size()){
                uniqueButtons.add(new QuestMenuButton(null, getInstance(), 10 + (i*3)));
            }else{
                uniqueButtons.add(new QuestMenuButton(questProgression.getQuestList().get(i), getInstance(), 10 + (i*3)));
            }
        }
        return generateInventory(client, uniqueButtons);
    }
}
