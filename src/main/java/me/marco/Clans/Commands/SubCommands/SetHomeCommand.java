package me.marco.Clans.Commands.SubCommands;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class SetHomeCommand implements ICommand {

    private LandManager landManager;
    private ClientManager clientManager;
    private Core instance;
    private Chat chat;
    private PluginManager pm;

    public SetHomeCommand(Core instance){
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

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can set the clan home");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        if(!landManager.isClaimed(chunk)){
            chat.sendClans(player, "You can only set home in land claimed by your clan");
            return;
        }

        Land land = landManager.getLand(chunk);
        Clan landOwner = landManager.getOwningClan(land);

        if(landOwner != client.getClan()){
            chat.sendClans(player, "You can only set home in land claimed by your clan");
            return;
        }

        Location home = player.getLocation();
        clan.setHome(home);
        instance.getClanManager().sendClanAnnouncement(clan, ChatColor.AQUA + player.getName() + "" + chat.textColour + " has set the clan home at:");
        instance.getClanManager().sendClanAnnouncement(clan, ChatColor.GOLD + "X:" + (int) home.getX() + ", Y:" + (int) home.getY() + ", Z:" + (int) home.getZ() + "");
                instance.getSqlRepoManager().getClanRepo().setClanHome(clan, home);

    }

    public String getName() {
        return "Sethome";
    }

    public String getDescription() {
        return chat.commandDescription("Set the home of your clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c sethome");
    }
}
