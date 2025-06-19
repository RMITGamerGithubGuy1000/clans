package me.marco.Admin.Commands.SubCommands;

import me.marco.Admin.Commands.AdminCommand;
import me.marco.Base.Core;
import me.marco.Client.ClientRank;
import me.marco.Commands.CommandManager;
import me.marco.Events.Clans.CustomEvents.ClanAdminCreateEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanCreateEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AdminClanCreate extends AdminCommand implements ICommand {

    private int minLength = 3;
    private int maxLength = 12;

    private CommandManager commandManager;
    private ClanManager clanManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public AdminClanCreate(CommandManager commandManager, Core instance){
        this.commandManager = commandManager;
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {
        if(args.length == 1){
            chat.sendClans(player, "You must enter a clan name to create. " + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);

        String clanName = args[1];
        if (clanName.matches("^.*[^a-zA-Z0-9].*$")) {
            chat.sendClans(player, "The clan name contains an invalid character");
            return;
        }

        if(clanName.length() > maxLength){
            chat.sendClans(player, "The clan name must be below 13 characters");
            return;
        }

        if(clanName.length() < minLength){
            chat.sendClans(player, "The clan name must be above 3 characters");
            return;
        }

        if(clanManager.clanExistsAll(clanName)){
            chat.sendClans(player, "Clan " + ChatColor.GOLD + clanName + "" + chat.textColour + " already exists");
            return;
        }

        if(commandManager.getCommands().stream().anyMatch(command -> clanName.equalsIgnoreCase(command.getName()))){
            chat.sendClans(player, "Your clan name can not be a command name");
            return;
        }

        boolean isSafeZone = false;

        if(args.length > 2){
            isSafeZone = Boolean.parseBoolean(args[2]);
        }

        pm.callEvent(new ClanAdminCreateEvent(clanName, player, isSafeZone));

    }

    public String getName() {
        return "Create";
    }

    public String getDescription() {
        return chat.commandDescription("Create an admin clan");
    }

    public String getCommandExample() {
        return chat.commandExample("x create <clanname>");
    }

    @Override
    public ClientRank getRankRequired() {
        return ClientRank.ADMIN;
    }
}
