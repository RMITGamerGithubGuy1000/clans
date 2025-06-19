package me.marco.Admin.Commands.SubCommands;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Admin.Commands.AdminCommand;
import me.marco.Base.Core;
import me.marco.Client.ClientRank;
import me.marco.Commands.CommandManager;
import me.marco.Events.Clans.CustomEvents.ClanAdminSafeZoneToggleEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AdminClanToggleSafe extends AdminCommand implements ICommand {

    private ClanManager clanManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public AdminClanToggleSafe(Core instance){
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {
        if(args.length == 1){
            chat.sendClans(player, "You must enter a clan to toggle safe zones for." + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);

        String clanName = args[1];

        if(!clanManager.adminClanExists(clanName)){
            chat.sendClans(player, "Clan " + ChatColor.GOLD + clanName + "" + chat.textColour + " doesn't exist");
            return;
        }

        AdminClan adminClan = clanManager.getAdminClan(clanName);

        pm.callEvent(new ClanAdminSafeZoneToggleEvent(adminClan, client));

    }

    public String getName() {
        return "SafeZone";
    }

    public String getDescription() {
        return chat.commandDescription("Toggle a safe zone ON or OFF for an admin clan");
    }

    public String getCommandExample() {
        return chat.commandExample("x safezone <clanname>");
    }

    @Override
    public ClientRank getRankRequired() {
        return ClientRank.ADMIN;
    }
}
