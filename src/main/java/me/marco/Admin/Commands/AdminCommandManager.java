package me.marco.Admin.Commands;

import me.marco.Admin.Commands.SubCommands.*;
import me.marco.Admin.Commands.SubCommands.Fields.AdminFieldsMode;
import me.marco.Base.Core;
import me.marco.Clans.Commands.SubCommands.CreateCommand;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import me.marco.Client.ClientRank;
import me.marco.Commands.CommandManager;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommandManager extends CommandManager {

    private ClanManager clanManager;
    private ClientManager clientManager;

    public AdminCommandManager(Core core) {
        super(core);
        this.clanManager = core.getClanManager();
        this.clientManager = core.getClientManager();
        addCommand(new AdminClanCreate(this, getInstance()));
        addCommand(new AdminClanClaim(getInstance()));
        addCommand(new AdminClanUnclaim(getInstance()));
        addCommand(new AdminClanToggleSafe(getInstance()));
        addCommand(new AdminClanDisband(getInstance()));

        addCommand(new AdminFieldsMode(getInstance()));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        Client client = clientManager.getClient(player);

        if(args == null || args.length == 0){
            if(!client.hasAdminLevel(ClientRank.ADMIN)){
                getInstance().getChat().sendAdminPowerRequirement(player, client, ClientRank.ADMIN);
                return true;
            }
            clientManager.toggleAdminMode(client);
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){
            if(args.length == 1) {
                sendHelp(player, 1);
                return true;
            }
            if(args[1].matches("^[0-9]+$")) {
                int page = Integer.parseInt(args[1]);
                if (page <= getCommandPages()) {
                    sendHelp(player, page);
                    return true;
                }
            }
            getInstance().getChat().sendClans(player, "Please provide a valid number for the help page");
            return true;
        }

        ICommand com = getCommand(args[0]);
        if(com != null){
            AdminCommand adminCommand = (AdminCommand) com;
            ClientRank clientRank = adminCommand.getRankRequired();
            if(!client.hasAdminLevel(clientRank)){
                getInstance().getChat().sendAdminPowerRequirement(player, client, clientRank);
                return true;
            }
            com.run(player, args);
        }else{
            sendHelp(player, 1);
            return true;
        }
        return true;
    }

    public void addCommand(ICommand icommand){
        getCommands().add(icommand);
    }

}
