package me.marco.Clans.Commands.SubCommands.Land;

import me.marco.Base.Core;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.Land.LandUnclaimEvent;
import me.marco.Utility.Chat;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class UnclaimCommand implements ICommand {

    private LandManager landManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public UnclaimCommand(Core instance){
        this.landManager = instance.getLandManager();
        this.clientManager = instance.getClientManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan admins can claim land");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        if(!landManager.isClaimed(chunk)){
            chat.sendClans(player, "This land is not claimed");
            return;
        }

        Land land = landManager.getLand(chunk);
        Clan landOwner = landManager.getOwningClan(land);
        if(landOwner != client.getClan()){
            chat.sendClans(player, "This land does not belong to your clan");
            return;
        }

        pm.callEvent(new LandUnclaimEvent(landOwner, client, chunk));

    }

    public String getName() {
        return "Unclaim";
    }

    public String getDescription() {
        return chat.commandDescription("Unclaim land where you are currently standing");
    }

    public String getCommandExample() {
        return chat.commandExample("c unclaim");
    }
}
