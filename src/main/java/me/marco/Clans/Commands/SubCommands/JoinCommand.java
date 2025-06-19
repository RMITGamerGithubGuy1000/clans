package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Handlers.ClanManager;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Client.Client;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanJoinEvent;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class JoinCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClanManager clanManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public JoinCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify which clan you want to join " + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(client.hasClan()){
            chat.sendClans(player, "You are already in a clan. You must leave your current clan before joining another.");
            return;
        }

        Clan toJoin = clanManager.getClan(args[1]);

        if(toJoin == null){
            chat.sendClans(player, "Cannot find clan " + chat.highlightName + args[1]);
            return;
        }

        Invite invite = inviteHandler.getInvite(client, toJoin, InviteType.CLAN);

        if(invite == null){
            chat.sendClans(player, "You are not invited to clan " + ChatColor.GOLD + toJoin.getName());
            return;
        }

        pm.callEvent(new ClanJoinEvent(toJoin, client, invite));

    }

    public String getName() {
        return "Join";
    }

    public String getDescription() {
        return chat.commandDescription("Join a clan you have been invited to");
    }

    public String getCommandExample() {
        return chat.commandExample("c join <clanname>");
    }
}
