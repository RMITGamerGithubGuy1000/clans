package me.marco.Quests.PossibleQuests;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Quests.QuestDifficulty;
import me.marco.Quests.QuestWrapper;
import me.marco.Quests.Questable;
import me.marco.Tags.PvPTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class QuestKillX extends Questable {

    public QuestKillX(Core instance) {
        super("Killing Spree", Material.IRON_SWORD, instance);
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
                        ChatColor.DARK_GRAY + ChatColor.BOLD.toString() +
                        " (" + questDifficulty.getChatColor() + ChatColor.BOLD.toString() +
                        questDifficulty.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")",
                ChatColor.YELLOW + "Kill " + ChatColor.AQUA + getRequired(questWrapper) + "" + ChatColor.YELLOW + " players to complete this quest"
        };
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Client deadClient = getInstance().getClientManager().getClient(dead);
        if (!deadClient.hasPvPTag()) return;
        PvPTag pvptag = deadClient.getPvPTag();
        Player killer = pvptag.getDamager();
        Client killerClient = getInstance().getClientManager().getClient(killer);
        if(!killerClient.hasQuest(this)) return;
        getInstance().getQuestManager().handleProgress(killer, this);
    }

}
