package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanTransferOwnershipEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class OwnershipCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public OwnershipCommand(Core instance){
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        Client client = clientManager.getClient(player);

        if (args.length == 1) {
            chat.sendClans(player, "You must specify who you want to transfer ownership to " + getCommandExample());
            return;
        }

        if (!client.hasClan()) {
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        if (!client.isClanOwner()) {
            chat.sendClans(player, "Only clan owners can transfer ownership");
            return;
        }

        Client newOwner = clientManager.getClient(args[1], player);
        if (newOwner == null) {
            return;
        }

        if(!newOwner.hasClan()){
            chat.sendClans(player, chat.highlightName + newOwner.getName() + chat.textColour + " does not belong to a clan");
            return;
        }

        if(newOwner.getClan() != client.getClan()){
            chat.sendClans(player, chat.highlightName + newOwner.getName() + chat.textColour + " does not belong to your clan");
            return;
        }

        if (newOwner == client) {
            chat.sendClans(player, "You already own the clan");
            return;
        }

        pm.callEvent(new ClanTransferOwnershipEvent(client.getClan(), client, newOwner));

    }

    public String getName() {
        return "Ownership";
    }

    public String getDescription() {
        return chat.commandDescription("Transfer ownership a member of your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c ownership <playername>");
    }

}
