package me.marco.Quests;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Quests.GUI.QuestMenu;
import me.marco.Quests.PossibleQuests.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestManager {

    private Core instance;
    private Random rand = new Random();
    private QuestMenu questMenu;

    public QuestManager(Core instance){
        this.instance  = instance;
    }

    private List<Questable> questableList = new ArrayList<Questable>();

    public void initialiseQuests(){
        this.questMenu = new QuestMenu(getInstance());
        getInstance().getMenuManager().addMenu(this.questMenu);
        addQuest(new QuestKillX(getInstance()));
        addQuest(new QuestSlowX(getInstance()));
        addQuest(new QuestActivateX(getInstance()));
        addQuest(new QuestBuyX(getInstance()));
        addQuest(new QuestSellX(getInstance()));
        spawnQuestPanda();
    }

    public void spawnQuestPanda(){
        Location location = new Location(getInstance().getServer().getWorld("world"), -228.5, 71.1, -228.5);
        EntityQuestie questie = new EntityQuestie(ChatColor.AQUA + ChatColor.BOLD.toString() + "Questie (Quest Panda)", location);
        getInstance().getCustomEntityManager().addCustomEntity(questie);
    }

    public void openQuestMenu(Player player){
        Client client = getInstance().getClientManager().getClient(player);
//        getInstance().getMenuManager().removeMenuOnClose(player, questMenu.getName());
//        getInstance().getMenuManager().addMenu(questMenu);
        questMenu.openMenu(player);
    }

    public void assignRandomQuest(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        QuestProgression questProgression = client.getQuestProgression();
        if(questProgression.isCooling()){
            getInstance().getChat().sendModule(player, "You can claim a new " + ChatColor.GOLD + ChatColor.BOLD.toString() + "Quest" + ChatColor.RESET +
                    getInstance().getChat().textColour + " in " + getInstance().getChat().highlightNumber + questProgression.getRemaining() + " seconds", "Quest");
            return;
        }
        if(questProgression.isFull()) return;
        List<Questable> availableQuests = new ArrayList<Questable>();
        for(Questable questable : getQuestableList()){
            if(questProgression.hasQuest(questable)) continue;
            availableQuests.add(questable);
        }
        if(availableQuests.isEmpty()) return;
        Questable questable = availableQuests.get(rand.nextInt(availableQuests.size()));
        QuestDifficulty questDifficulty = QuestDifficulty.values()[rand.nextInt(QuestDifficulty.values().length)];
        QuestWrapper questWrapper = new QuestWrapper(questable, questDifficulty);
        questProgression.addQuest(questWrapper);
        getInstance().getChat().sendModule(player, "You have been assigned a quest", "Quest");
        questWrapper.sendSummary(player, false);
        getInstance().getSqlRepoManager().getQuestRepo().setProfile(client);
    }

    public void addQuest(Questable questable){
        this.questableList.add(questable);
    }

    public Core getInstance() {
        return instance;
    }

    public List<Questable> getQuestableList() {
        return questableList;
    }

    public void handleProgress(Player player, Questable quest) {
        QuestProgression questProgression = getInstance().getClientManager().getClient(player).getQuestProgression();
        QuestWrapper questWrapper = questProgression.getWrapper(quest);
        questProgression.addProgress(quest);
        questWrapper.playProgressionEffect(player);
    }

    public void handleProgress(Player player, Questable quest, int newProgress) {
        QuestProgression questProgression = getInstance().getClientManager().getClient(player).getQuestProgression();
        QuestWrapper questWrapper = questProgression.getWrapper(quest);
        questProgression.addProgress(quest, newProgress);
        questWrapper.playProgressionEffect(player);
        getInstance().getSqlRepoManager().getQuestRepo().setProfile(player.getUniqueId().toString(), questProgression);
    }

    public Questable getQuestableFromString(String questableName){
        return this.questableList.stream().filter(questable -> questable.getName().equalsIgnoreCase(questableName)).findFirst().orElse(null);
    }

    public QuestWrapper questFromString(String questString) {
        // QUESTNAME,PROGRESSION,DIFFICULTY
        String[] questStringSplit = questString.split("\\|");
        return new QuestWrapper(getQuestableFromString(
                questStringSplit[0]),
                QuestDifficulty.valueOf(questStringSplit[2]),
                Integer.valueOf(questStringSplit[1])
        );
    }

    public String questToString(QuestWrapper questWrapper){
        String name = questWrapper.getQuest().getName();
        int progression = questWrapper.getProgression();
        QuestDifficulty difficulty = questWrapper.getQuestDifficulty();
        return name + "|" + progression + "|" + difficulty.toString();
    }

    public QuestProgression questProfileFromString(String profileString){
        QuestProgression questProgression = new QuestProgression();
        if(profileString == null) return questProgression;
        String[] questProfileSplit = profileString.split(",");
        for(String quest : questProfileSplit){
            questProgression.addQuest(questFromString(quest));
        }
        return questProgression;
    }

    public String questProfileToString(QuestProgression questProgression){
        List<QuestWrapper> questList = questProgression.getQuestList();
        if(questList.size() == 1){
            return questToString(questList.get(0));
        }
        if(questList.size() == 2){
            return questToString(questList.get(0)) + "," + questToString(questList.get(1)) ;
        }
        if(questList.size() == 3){
            return questToString(questList.get(0)) + "," + questToString(questList.get(1)) + "," + questToString(questList.get(2));
        }
        return null;
    }

}
