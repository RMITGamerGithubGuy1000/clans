package me.marco.Quests;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class QuestWrapper {

    private Questable quest;
    private QuestDifficulty questDifficulty;
    private int progression;
    private int progressRequired;

    public QuestWrapper(Questable questable, QuestDifficulty questDifficulty){
        this.quest = questable;
        this.questDifficulty = questDifficulty;
        this.progressRequired = questable.getBaseProgressRequired() * questDifficulty.getDifficultyMultiplier();
        this.progression = 0;
    }

    public QuestWrapper(Questable questable, QuestDifficulty questDifficulty, int progress){
        this.quest = questable;
        this.questDifficulty = questDifficulty;
        this.progressRequired = questable.getBaseProgressRequired() * questDifficulty.getDifficultyMultiplier();
        this.progression = progress;
    }

    public Questable getQuest() {
        return quest;
    }

    public int getProgression() {
        return progression;
    }

    public int getProgressRequired() {
        return progressRequired;
    }

    public void addProgress(){
        this.progression++;
    }

    public void addProgress(int amount){
        this.progression+=amount;
    }

    public boolean isComplete(){
        return this.progression >= getProgressRequired();
    }

    public double getProgressPercentage(){
        double prog = (double) this.progression / (double) getProgressRequired();
        return Math.round(prog*100.0);
    }

    public QuestDifficulty getQuestDifficulty() {
        return questDifficulty;
    }

    public void sendSummary(Player player, boolean progress) {
        Questable quest = this.getQuest();
        for(String summary : quest.getDescription(this)){
            player.sendMessage(summary);
        }
        if(progress) player.sendMessage(ChatColor.BLUE + "Progress:" + getProgressionString() +
                ChatColor.DARK_GRAY + ChatColor.BOLD.toString() +
                " (" + questDifficulty.getChatColor() + ChatColor.BOLD.toString() +
                getProgression() + "/" + getProgressRequired() + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, .5f, 3f);
    }

    public void playProgressionEffect(Player player){
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + ChatColor.MAGIC + "|" + ChatColor.RESET +
                ChatColor.GREEN + ChatColor.BOLD.toString() + quest.getName() + ChatColor.MAGIC + "|" + ChatColor.RESET +
                ChatColor.YELLOW + " Quest Progression: " + getProgressionString());
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .5f, 3f);
        if(isComplete()){
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, .5f, 1f);
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, .5f, 1f);
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, .5f, 1f);
            return;
        }
    }

    public String getProgressionString(){
        int progression = getProgression();
        if(isComplete()) return ChatColor.GREEN + ChatColor.BOLD.toString() + "COMPLETE!";
        ChatColor colour = ChatColor.RED;
        if(progression < 33 && progression <= 66) colour = ChatColor.GOLD;
        if(progression > 66) colour = ChatColor.GREEN;
        return colour.toString() + getProgressPercentage() + "%";
    }

//    private Questable quest;
//    private QuestDifficulty questDifficulty;
//    private int progression;
//    private int progressRequired;

    public String asString(){
        return getQuest().getName() + "," + questDifficulty.getName() + "," + progression + "," + progressRequired;
    }

}
