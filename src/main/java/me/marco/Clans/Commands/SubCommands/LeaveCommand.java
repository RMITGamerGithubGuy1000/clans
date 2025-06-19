package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanLeaveEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class LeaveCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public LeaveCommand(Core instance){
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(client.isClanOwner()){
            chat.sendClans(player, "You must transfer ownership of your clan or disband it before leaving");
            chat.sendClans(player, "To transfer ownership type: " + ChatColor.BLUE + "/clan ownership <player>");
            return;
        }

        pm.callEvent(new ClanLeaveEvent(clan, client));

    }

    public String getName() {
        return "Leave";
    }

    public String getDescription() {
        return chat.commandDescription("Leave your current clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c leave");
    }
}
