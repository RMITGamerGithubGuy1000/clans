package me.marco.Clans.Commands.SubCommands.Relations;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Client.Client;
import me.marco.Events.Clans.CustomEvents.Relations.ClanTrustEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class TrustCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClientManager clientManager;
    private ClanManager clanManager;
    private Chat chat;
    private PluginManager pm;

    public TrustCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clientManager = instance.getClientManager();
        this.clanManager = instance.getClanManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify which clan you wish to trust" + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can give trust to allies");
            return;
        }

        Clan toTrust = clanManager.getClan(args[1], player);

        if(toTrust == null){
            return;
        }

        if(toTrust == clan){
            chat.sendClans(player, "You cannot trust your own clan");
            return;
        }

        if(!clan.isAllied(toTrust)){
            chat.sendClans(player, "You are not allied with clan " + chat.getClanRelation(clan, toTrust) + toTrust.getName());
            return;
        }

        if(inviteHandler.inviteExists(toTrust, clan, InviteType.TRUST)) {
            chat.sendClans(player, "A trust request has already been sent to clan " +
                    chat.getClanRelation(clan, toTrust) + toTrust.getName() + "");
            return;
        }

        if(clan.isTrusted(toTrust)){
            pm.callEvent(new ClanTrustEvent(clan, toTrust, false));
            return;
        }

        if(inviteHandler.inviteExists(clan, toTrust, InviteType.TRUST)) {
            Invite existingInvite = inviteHandler.getInvite(clan, toTrust, InviteType.TRUST);
            inviteHandler.removeInvite(existingInvite);
            pm.callEvent(new ClanTrustEvent(clan, toTrust, true));
            return;
        }

        inviteHandler.createInvite(toTrust, clan, InviteType.TRUST);

    }

    public String getName() {
        return "Trust";
    }

    public String getDescription() {
        return chat.commandDescription("Give trust to an ally");
    }

    public String getCommandExample() {
        return chat.commandExample("c trust <clanname>");
    }
}
