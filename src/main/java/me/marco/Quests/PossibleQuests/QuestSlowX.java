package me.marco.Quests.PossibleQuests;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Quests.QuestDifficulty;
import me.marco.Quests.QuestWrapper;
import me.marco.Quests.Questable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

public class QuestSlowX extends Questable {

    public QuestSlowX(Core instance) {
        super("Slow It Down!", Material.INK_SAC, instance);
    }

    @Override
    public int getBaseProgressRequired() {
        return 1;
    }

    @Override
    public String[] getDescription(QuestWrapper questWrapper) {
        QuestDifficulty questDifficulty = questWrapper.getQuestDifficulty();
        return new String[]{
                ChatColor.GREEN +  ChatColor.BOLD.toString() + this.getName() +
                        ChatColor.DARK_GRAY +  ChatColor.BOLD.toString() +
                        " (" + questDifficulty.getChatColor() + ChatColor.BOLD.toString() +
                        questDifficulty.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")",
                ChatColor.YELLOW + "Slow " + ChatColor.AQUA + getRequired(questWrapper) + "" + ChatColor.YELLOW + " players to complete this quest"
        };
    }

    @EventHandler
    public void onPotion(PotionEffectEvent event) {
        Player from = event.getPotionFrom();
        Client fromClient = getInstance().getClientManager().getClient(from);
        if(!fromClient.hasQuest(this)) return;
        if(event.getPotionEffectType() != PotionEffectType.SLOWNESS) return;
        getInstance().getQuestManager().handleProgress(from, this);
    }

}
