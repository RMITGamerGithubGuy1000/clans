package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;

public class InviteCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClientManager clientManager;
    private Chat chat;

    public InviteCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify who you want to invite " + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can invite people clans");
            return;
        }

        Client toInvite = clientManager.getClient(args[1], player);

        if(toInvite == null){
            chat.sendClans(player, "Cannot find player " + chat.highlightName + args[1]);
            return;
        }

        if(toInvite == client){
            chat.sendClans(player, "You cannot invite yourself");
            return;
        }

        if(inviteHandler.inviteExists(toInvite, clan, InviteType.CLAN)) {
            chat.sendClans(player,
                    chat.getPlayerRelationMain(client, toInvite) + toInvite.getName() + "" + chat.textColour + " has already been invited to your clan");
            return;
        }

        inviteHandler.createInvite(toInvite, clan, InviteType.CLAN);

    }

    public String getName() {
        return "Invite";
    }

    public String getDescription() {
        return chat.commandDescription("Invite someone to your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c invite <playername>");
    }
}
