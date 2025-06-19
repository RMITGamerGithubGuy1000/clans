package me.marco.Quests;

import me.marco.Utility.UtilTime;

import java.util.ArrayList;
import java.util.List;

public class QuestProgression {

    private final int maxQuests = 3;
    private long lastClaim;
    private final int COOLDOWN = 14400;

    private List<QuestWrapper> questList = new ArrayList<QuestWrapper>();

    public boolean isCooling(){
        return System.currentTimeMillis() <= this.lastClaim + (COOLDOWN * 1000);
    }

    public double getRemaining(){
        return UtilTime.convert((COOLDOWN * 1000 + System.currentTimeMillis()) - System.currentTimeMillis(), UtilTime.TimeUnit.SECONDS, 1);
    }

    public boolean handleCooldown(){
        if(isCooling()) return false;
        this.lastClaim = System.currentTimeMillis();
        return true;
    }

    public void addQuest(QuestWrapper questWrapper){
        if(this.isFull()) return;
        this.questList.add(questWrapper);
    }

    public boolean hasQuest(Questable questable){
        return this.questList.stream().anyMatch(questWrapper -> questWrapper.getQuest().equals(questable));
    }

    public boolean hasQuest(String name){
        return this.questList.stream().anyMatch(questWrapper -> questWrapper.getQuest().getName().equalsIgnoreCase(name));
    }

    public List<QuestWrapper> getQuestList() {
        return questList;
    }

    public void addProgress(Questable quest) {
        QuestWrapper questWrapper = getWrapper(quest);
        if(questWrapper == null) return;
        questWrapper.addProgress();
    }

    public void addProgress(Questable quest, int amount) {
        QuestWrapper questWrapper = getWrapper(quest);
        if(questWrapper == null) return;
        questWrapper.addProgress(amount);
    }

    public QuestWrapper getWrapper(Questable questable) {
        return this.questList.stream().filter(questWrapper -> questWrapper.getQuest().equals(questable)).findFirst().orElse(null);
    }

    public boolean isFull() {
        return this.questList.size() >= maxQuests;
    }

    public String asString() {
        String string = "";
        for(int i = 0; i < 3; i++){
            if(this.questList.size() - 1 < i){

            }
        }
        return string;
    }

}
