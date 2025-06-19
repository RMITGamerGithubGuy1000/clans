package me.marco.Quests;

import org.bukkit.ChatColor;

public enum QuestDifficulty {

    EASY("Easy", ChatColor.GREEN, 1),
    NORMAL("Normal", ChatColor.BLUE, 2),
    HARD("Hard", ChatColor.RED, 3),
    INSANE("Insane", ChatColor.DARK_PURPLE, 4);

    private String name;
    private ChatColor chatColor;
    private int difficultyMultiplier;

    QuestDifficulty(String name, ChatColor chatColor, int difficultyMultiplier){
        this.name = name;
        this.chatColor = chatColor;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getDifficultyMultiplier() {
        return difficultyMultiplier;
    }
}
