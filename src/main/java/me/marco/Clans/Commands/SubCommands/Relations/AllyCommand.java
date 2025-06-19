package me.marco.Clans.Commands.SubCommands.Relations;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Client.Client;
import me.marco.Events.Clans.CustomEvents.Relations.ClanAllyEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AllyCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClientManager clientManager;
    private ClanManager clanManager;
    private Chat chat;
    private PluginManager pm;

    public AllyCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clientManager = instance.getClientManager();
        this.clanManager = instance.getClanManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify which clan you wish to ally" + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can form ally other clans");
            return;
        }

        Clan toAlly = clanManager.getClan(args[1], player);

        if(toAlly == null){
            return;
        }

        if(toAlly == clan){
            chat.sendClans(player, "You cannot ally your own clan");
            return;
        }

        if(clan.isAllied(toAlly)){
            chat.sendClans(player, "You are already allied with clan " + chat.getClanRelation(clan, toAlly) + toAlly.getName());
            return;
        }

        if(clan.isPillageRelation(toAlly)){
            chat.sendClans(player, "You cannot ally clan " + chat.getClanRelation(clan, toAlly) + toAlly.getName() + chat.textColour + " while there is a pillage");
            return;
        }

        if(inviteHandler.inviteExists(toAlly, clan, InviteType.ALLY)) {
            chat.sendClans(player, "An alliance request has already been sent to clan " +
                    chat.getClanRelation(clan, toAlly) + toAlly.getName() + "");
            return;
        }

        if(inviteHandler.inviteExists(clan, toAlly, InviteType.ALLY)) {
            Invite existingInvite = inviteHandler.getInvite(clan, toAlly, InviteType.ALLY);
            pm.callEvent(new ClanAllyEvent(clan, toAlly, existingInvite));
            return;
        }

        inviteHandler.createInvite(toAlly, clan, InviteType.ALLY);

    }

    public String getName() {
        return "Ally";
    }

    public String getDescription() {
        return chat.commandDescription("Form an alliance with another clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c ally <clanname>");
    }
}
