package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Events.Clans.CustomEvents.ClanDisbandEvent;
import me.marco.Handlers.ClientManager;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class DisbandCommand implements ICommand {

    private ClientManager clientManager;
    private Chat chat;
    private PluginManager pm;

    public DisbandCommand(Core instance){
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

        Clan clan = client.getClan();

        if(!clan.getOwner().equals(client.getUUID())){
            chat.sendClans(player, "Only clan owners can disband clans");
            return;
        }

        if(clan.isBeingPillaged()){
            chat.sendClans(player, "You can't disband your clan whilst being pillaged");
            return;
        }

        pm.callEvent(new ClanDisbandEvent(clan, client));

    }

    public String getName() {
        return "Disband";
    }

    public String getDescription() {
        return chat.commandDescription("Disband your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c disband");
    }
}
