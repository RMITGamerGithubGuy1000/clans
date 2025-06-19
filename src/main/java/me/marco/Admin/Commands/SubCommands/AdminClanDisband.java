package me.marco.Admin.Commands.SubCommands;

import me.marco.Admin.Commands.AdminCommand;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.ClientRank;
import me.marco.Events.Clans.CustomEvents.ClanDisbandEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AdminClanDisband extends AdminCommand implements ICommand {

    private ClanManager clanManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public AdminClanDisband(Core instance){
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {
        if(args.length == 1){
            chat.sendClans(player, "You must enter a clan to disband." + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);

        String clanName = args[1];

        if(!clanManager.clanExistsAll(clanName)){
            chat.sendClans(player, "Clan " + ChatColor.GOLD + clanName + "" + chat.textColour + " doesn't exist");
            return;
        }

        Clan toDisband = null;

        if(clanManager.adminClanExists(clanName)){
            toDisband = clanManager.getAdminClan(clanName);
        }

        if(clanManager.clanExists(clanName)){
            toDisband = clanManager.getClan(clanName);
        }

        pm.callEvent(new ClanDisbandEvent(toDisband, client));

    }

    public String getName() {
        return "Disband";
    }

    public String getDescription() {
        return chat.commandDescription("Disband any clan (normal or admin)");
    }

    public String getCommandExample() {
        return chat.commandExample("x disband <clanname>");
    }

    @Override
    public ClientRank getRankRequired() {
        return ClientRank.ADMIN;
    }
}
