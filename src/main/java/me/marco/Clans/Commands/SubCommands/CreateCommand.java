package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Commands.CommandManager;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanCreateEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import me.marco.Utility.Cooldowns.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class CreateCommand implements ICommand {

    private int minLength = 3;
    private int maxLength = 12;

    private CommandManager commandManager;
    private ClanManager clanManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;
    private CooldownManager cdm;

    public CreateCommand(CommandManager commandManager, Core instance){
        this.commandManager = commandManager;
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
        this.cdm = instance.getCooldownManager();
    }

    public void run(Player player, String[] args) {
        if(args.length == 1){
            chat.sendClans(player, "You must enter a clan name to create. " + getCommandExample());
            return;
        }

        if(!cdm.add(player, this.getName(), "Command", 0, 5, false)){
            this.cdm.sendRemaining(player, this.getName());
            return;
        }

        Client client = clientManager.getClient(player);
        if(client.hasClan()){
            chat.sendClans(player, "You are already in a clan. You must leave your clan before making another one");
            return;
        }

        String clanName = args[1];
        if (clanName.matches("^.*[^a-zA-Z0-9].*$")) {
            chat.sendClans(player, "Your clan name contains an invalid character");
            return;
        }

        if(clanName.length() > maxLength){
            chat.sendClans(player, "Your clan name must be below 13 characters");
            return;
        }

        if(clanName.length() < minLength){
            chat.sendClans(player, "Your clan name must be above 3 characters");
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

        pm.callEvent(new ClanCreateEvent(clanName, player));

    }

    public String getName() {
        return "Create";
    }

    public String getDescription() {
        return chat.commandDescription("Create your own clan");
    }

    public String getCommandExample() {
        return chat.commandExample("clan create <clanname>");
    }
}
