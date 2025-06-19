package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.Clans.CustomEvents.ClanKickEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class KickCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public KickCommand(Core instance){
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify who you want to kick " + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can kick people from clans");
            return;
        }

        Client toKick = clientManager.getClient(args[1], player);

        if(toKick == null){
            chat.sendClans(player, "Cannot find player " + chat.highlightName + args[1]);
            return;
        }

        if(toKick == client){
            chat.sendClans(player, "You cannot kick yourself");
            return;
        }

        if(toKick.getClan() != client.getClan()){
            chat.sendClans(player, chat.highlightName + toKick.getName() + "" + chat.textColour + " is not in your clan");
            return;
        }

        if(toKick.isClanAdmin() && !client.isClanOwner()){
            chat.sendClans(player, "You cannot kick someone of equal authority to you");
            return;
        }

        pm.callEvent(new ClanKickEvent(clan, toKick, client));

    }

    public String getName() {
        return "Kick";
    }

    public String getDescription() {
        return chat.commandDescription("Kick a member of your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c kick <playername>");
    }
}
