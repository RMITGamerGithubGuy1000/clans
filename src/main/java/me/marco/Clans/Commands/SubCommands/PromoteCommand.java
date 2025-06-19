package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanPromoteEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class PromoteCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public PromoteCommand(Core instance){
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        Client client = clientManager.getClient(player);

        if (args.length == 1) {
            chat.sendClans(player, "You must specify who you want to promote " + getCommandExample());
            return;
        }

        if (!client.hasClan()) {
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        if (!client.isClanAdmin()) {
            chat.sendClans(player, "Only clan admins and owners can promote players");
            return;
        }

        Client toPromote = clientManager.getClient(args[1], player);
        if (toPromote == null) {
            return;
        }

        if(!toPromote.hasClan()){
            chat.sendClans(player, chat.highlightName + toPromote.getName() + chat.textColour + " does not belong to a clan");
            return;
        }

        if(toPromote.getClan() != client.getClan()){
            chat.sendClans(player, chat.highlightName + toPromote.getName() + chat.textColour + " does not belong to your clan");
            return;
        }

        if (toPromote == client) {
            chat.sendClans(player, "You cannot promote yourself");
            return;
        }

        if (toPromote.isClanAdmin()) {
            chat.sendClans(player, "You cannot promote someone of equal authority to you");
            return;
        }

        ClanRank clanRank = null;

        if (toPromote.getClanRank() == ClanRank.RECRUIT) {
            clanRank = ClanRank.MEMBER;
        } else if (toPromote.getClanRank() == ClanRank.MEMBER) {
            clanRank = ClanRank.ADMIN;
        }

        if(client.getClanRank() != ClanRank.OWNER && clanRank == ClanRank.ADMIN){
            chat.sendClans(player, "Only clan owners can promote people to admin");
            return;
        }

        pm.callEvent(new ClanPromoteEvent(toPromote.getClan(), toPromote, client, clanRank));

    }

    public String getName() {
        return "Promote";
    }

    public String getDescription() {
        return chat.commandDescription("Promote a member of your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c promote <playername>");
    }

}
