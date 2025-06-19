package me.marco.Quests.PossibleQuests;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.Shops.ItemBuyEvent;
import me.marco.Events.Shops.ItemSellEvent;
import me.marco.Quests.QuestDifficulty;
import me.marco.Quests.QuestWrapper;
import me.marco.Quests.Questable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class QuestSellX extends Questable {

    public QuestSellX(Core instance) {
        super("Sleazy Salesman", Material.SUNFLOWER, instance);
    }

    @Override
    public int getBaseProgressRequired() {
        return 10;
    }

    @Override
    public String[] getDescription(QuestWrapper questWrapper) {
        QuestDifficulty questDifficulty = questWrapper.getQuestDifficulty();
        return new String[]{
                ChatColor.GREEN + ChatColor.BOLD.toString() + this.getName() +
                        ChatColor.DARK_GRAY + ChatColor.BOLD.toString() +
                        " (" + questDifficulty.getChatColor() + ChatColor.BOLD.toString() +
                        questDifficulty.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + ")",
                ChatColor.YELLOW + "Sell " + ChatColor.AQUA + getRequired(questWrapper) + "" + ChatColor.YELLOW + " items to complete this quest"
        };
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSell(ItemSellEvent event) {
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if (!client.hasQuest(this) || event.isCancelled()) return;
        getInstance().getQuestManager().handleProgress(player, this, event.getAmount());
    }

}
