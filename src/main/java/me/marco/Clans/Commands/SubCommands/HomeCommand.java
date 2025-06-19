package me.marco.Clans.Commands.SubCommands;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Utility.Chat;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class HomeCommand implements ICommand {

    private LandManager landManager;
    private ClientManager clientManager;
    private Core instance;
    private Chat chat;
    private PluginManager pm;

    public HomeCommand(Core instance){
        this.landManager = instance.getLandManager();
        this.clientManager = instance.getClientManager();
        this.instance = instance;
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {
        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!clan.hasHome()){
            chat.sendClans(player, "Your clan does not have a home location set");
            return;
        }

        Location home = clan.getHome();

        Chunk homeChunk = home.getChunk();

        if(!landManager.isClaimed(homeChunk)){
            chat.sendClans(player, "Your home location is no longer in your clan's possession");
            return;
        }

        Land homeLand = landManager.getLand(homeChunk);
        Clan homeOwner = landManager.getOwningClan(homeLand);

        if(homeOwner != clan){
            chat.sendClans(player, "Your home location is no longer in your clan's possession");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        if(!landManager.isClaimed(chunk)){
            chat.sendClans(player, "You can only teleport home from " + instance.getChat().highlightText + "Spawn");
            return;
        }

        Land land = landManager.getLand(chunk);
        Clan landOwner = landManager.getOwningClan(land);

        if(!(landOwner instanceof AdminClan)){
            chat.sendClans(player, "You can only teleport home from " + instance.getChat().highlightText + "Spawn");
            return;
        }

        AdminClan adminClan = (AdminClan) landOwner;

        if(!adminClan.getName().equalsIgnoreCase("Spawn")){
            chat.sendClans(player, "You can only teleport home from " + instance.getChat().highlightText + "Spawn");
            return;
        }

        chat.sendClans(player, "You teleported to your clan " + chat.highlightText + "home");
        player.teleport(clan.getHome());

    }

    public String getName() {
        return "Home";
    }

    public String getDescription() {
        return chat.commandDescription("Teleport to your clan home");
    }

    public String getCommandExample() {
        return chat.commandExample("c home");
    }
}
