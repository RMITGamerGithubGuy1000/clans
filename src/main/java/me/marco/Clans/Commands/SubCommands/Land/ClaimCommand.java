package me.marco.Clans.Commands.SubCommands.Land;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Events.Clans.CustomEvents.Land.LandOverclaimEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.Land.LandClaimEvent;
import me.marco.Utility.Chat;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class ClaimCommand implements ICommand {

    private LandManager landManager;
    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public ClaimCommand(Core instance){
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

        Clan clan = client.getClan();

        if(!clan.canClaim()){
            chat.sendClans(player, "Your clan has reached it's maximum claim amount");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        Land land = landManager.getLand(chunk);
        if(land != null){
            Clan landOwner = landManager.getOwningClan(land);
            if(!landOwner.isOverClaimed()){
                chat.sendClans(player, "This land is already claimed");
                return;
            }else{
                pm.callEvent(new LandOverclaimEvent(landOwner, clan, client, chunk));
                return;
            }
        }

        if(landManager.nearbyClaims(chunk, clan)){
            chat.sendClans(player, "You can not claim land this close to other clans");
            return;
        }

        pm.callEvent(new LandClaimEvent(clan, client, chunk));

    }

    public String getName() {
        return "Claim";
    }

    public String getDescription() {
        return chat.commandDescription("Claim land where you are currently standing");
    }

    public String getCommandExample() {
        return chat.commandExample("c claim");
    }
}
