package me.marco.Admin.Commands.SubCommands;

import me.marco.Admin.Commands.AdminCommand;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.ClientRank;
import me.marco.Events.Clans.CustomEvents.Land.LandClaimEvent;
import me.marco.Events.Clans.CustomEvents.Land.LandUnclaimEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AdminClanUnclaim extends AdminCommand implements ICommand {

    private ClanManager clanManager;
    private ClientManager clientManager;
    private LandManager landManager;
    private Chat chat;
    private PluginManager pm;

    public AdminClanUnclaim(Core instance){
        this.clanManager = instance.getClanManager();
        this.clientManager = instance.getClientManager();
        this.landManager = instance.getLandManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {
        Chunk chunk = player.getLocation().getChunk();

        Land land = landManager.getLand(chunk);
        if(land == null){
            chat.sendClans(player, "This land isn't claimed");
            return;
        }

        Clan clan = landManager.getOwningClan(land);

        Client client = clientManager.getClient(player);

        pm.callEvent(new LandUnclaimEvent(clan, client, chunk));

    }

    public String getName() {
        return "Unclaim";
    }

    public String getDescription() {
        return chat.commandDescription("Unclaim land for any clan");
    }

    public String getCommandExample() {
        return chat.commandExample("x unclaim");
    }

    @Override
    public ClientRank getRankRequired() {
        return ClientRank.ADMIN;
    }
}
