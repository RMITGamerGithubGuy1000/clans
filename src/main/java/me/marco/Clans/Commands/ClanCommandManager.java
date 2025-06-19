package me.marco.Clans.Commands;

import me.marco.Base.Core;
import me.marco.Clans.Commands.SubCommands.*;
import me.marco.Clans.Commands.SubCommands.Relations.AllyCommand;
import me.marco.Clans.Commands.SubCommands.Land.ClaimCommand;
import me.marco.Clans.Commands.SubCommands.Land.UnclaimCommand;
import me.marco.Clans.Commands.SubCommands.Relations.EnemyCommand;
import me.marco.Clans.Commands.SubCommands.Relations.NeutralCommand;
import me.marco.Clans.Commands.SubCommands.Relations.TrustCommand;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import me.marco.Commands.CommandManager;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanCommandManager extends CommandManager {

    private ClanManager clanManager;
    private ClientManager clientManager;

    public ClanCommandManager(Core core) {
        super(core);
        this.clanManager = core.getClanManager();
        this.clientManager = core.getClientManager();

        addCommand(new CreateCommand(this, getInstance()));
        addCommand(new JoinCommand(getInstance()));
        addCommand(new LeaveCommand(getInstance()));
        addCommand(new KickCommand(getInstance()));
        addCommand(new DisbandCommand(getInstance()));
        addCommand(new InviteCommand(getInstance()));
        addCommand(new HomeCommand(getInstance()));
        addCommand(new SetHomeCommand(getInstance()));
        addCommand(new PromoteCommand(getInstance()));
        addCommand(new DemoteCommand(getInstance()));
        addCommand(new OwnershipCommand(getInstance()));

        addCommand(new ClaimCommand(getInstance()));
        addCommand(new UnclaimCommand(getInstance()));

        addCommand(new AllyCommand(getInstance()));
        addCommand(new TrustCommand(getInstance()));

        addCommand(new NeutralCommand(getInstance()));

        addCommand(new EnemyCommand(getInstance()));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if(args == null || args.length == 0){
            Client client = clientManager.getClient(player);
            if(!client.hasClan()){
                sendHelp(player, 1);
                return true;
            }
            clanManager.sendClanSummary(player, client.getClan());
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
            com.run(player, args);
        }else{
            Clan clan = getInstance().getClanManager().getClan(args[0], player);
            if(clan != null){
                clanManager.sendClanSummary(player, clan);
                return true;
            }
            return true;
        }

        return true;
    }

}
