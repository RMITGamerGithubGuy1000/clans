package me.marco.Quests.GUI;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.GUI.Button;
import me.marco.Quests.QuestProgression;
import me.marco.Quests.QuestWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class QuestMenuButton extends Button {

    private QuestWrapper questWrapper;

    public QuestMenuButton(QuestWrapper questWrapper, Core core, int slot) {
        super(core, slot);
        this.questWrapper = questWrapper;
        this.setItem(createItem());
    }

    public ItemStack createItem(){
        return getInstance().getUtilItem().createItem(getName(), getDescription(), getMaterialIcon());
    }

    public String getName(){
        if(this.questWrapper == null){
            return ChatColor.GRAY + ChatColor.BOLD.toString() + "No Quest";
        }
        return questWrapper.isComplete() ?
                ChatColor.GREEN + ChatColor.BOLD.toString() + questWrapper.getQuest().getName() +
                        ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + " (" + ChatColor.BLUE + ChatColor.BOLD.toString() + "COMPLETE!" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")" :
                ChatColor.GOLD + ChatColor.BOLD.toString() + questWrapper.getQuest().getName() +
                        ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + " (" + ChatColor.BLUE + ChatColor.BOLD.toString() + "In Progress" + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")";
    }

    public List<String> getDescription(){
        if(this.questWrapper == null){
            return Arrays.asList(ChatColor.YELLOW + "Click to claim a new quest!");
        }
        return Arrays.asList(this.questWrapper.getQuest().getDescription(this.questWrapper));
    }

    public Material getMaterialIcon(){
        if(this.questWrapper == null){
            return Material.BLACK_STAINED_GLASS;
        }
        return this.questWrapper.isComplete() ? Material.GREEN_STAINED_GLASS : this.questWrapper.getQuest().getIcon();
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        if(questWrapper == null){
            getInstance().getQuestManager().assignRandomQuest(player);
            getInstance().getQuestManager().openQuestMenu(player);
            return;
        }
        Client client = getInstance().getClientManager().getClient(player);
        QuestProgression questProgression = client.getQuestProgression();
        if(questWrapper.isComplete()){
            player.sendMessage("complete");
            return;
        }
        if(!questWrapper.isComplete()){
            player.sendMessage("in progress");
            return;
        }
    }

}
