package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanDemoteEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class DemoteCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public DemoteCommand(Core instance){
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        Client client = clientManager.getClient(player);

        if(args.length == 1){
            chat.sendClans(player, "You must specify who you want to demote " + getCommandExample());
            return;
        }

        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan admins and owners can demote players");
            return;
        }

        Client toDemote = clientManager.getClient(args[1], player);
        if(toDemote == null){
            return;
        }

        if(!toDemote.hasClan()){
            chat.sendClans(player, chat.highlightName + toDemote.getName() + chat.textColour + " does not belong to a clan");
            return;
        }

        if(toDemote.getClan() != client.getClan()){
            chat.sendClans(player, chat.highlightName + toDemote.getName() + chat.textColour + " does not belong to your clan");
            return;
        }

        if(toDemote == client){
            chat.sendClans(player, "You cannot demote yourself");
            return;
        }

        if(toDemote.isClanAdmin() && !client.isClanOwner()){
            chat.sendClans(player, "You cannot demote someone of equal authority to you");
            return;
        }

        ClanRank clanRank = null;

        if (toDemote.getClanRank() == ClanRank.ADMIN) {
            clanRank = ClanRank.MEMBER;
        } else if (toDemote.getClanRank() == ClanRank.MEMBER) {
            clanRank = ClanRank.RECRUIT;
        }

        pm.callEvent(new ClanDemoteEvent(toDemote.getClan(), toDemote, client, clanRank));

    }

    public String getName() {
        return "Demote";
    }

    public String getDescription() {
        return chat.commandDescription("Demote a member of your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c demote <playername>");
    }

}
