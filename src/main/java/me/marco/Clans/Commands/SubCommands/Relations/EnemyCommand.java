package me.marco.Clans.Commands.SubCommands.Relations;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Client.Client;
import me.marco.Events.Clans.CustomEvents.Relations.ClanAllyEvent;
import me.marco.Events.Clans.CustomEvents.Relations.ClanEnemyEvent;
import me.marco.Events.Clans.CustomEvents.Relations.ClanNeutralEvent;
import me.marco.Events.Clans.CustomEvents.Relations.ClanTrustEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class EnemyCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClientManager clientManager;
    private ClanManager clanManager;
    private Chat chat;
    private PluginManager pm;

    public EnemyCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clientManager = instance.getClientManager();
        this.clanManager = instance.getClanManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify which clan you wish to wage war on" + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can wage war on other clans");
            return;
        }

        Clan toEnemy = clanManager.getClan(args[1], player);

        if(toEnemy == null){
            return;
        }

        if(toEnemy == clan){
            chat.sendClans(player, "You cannot wage war on your own clan");
            return;
        }

        if(clan.isEnemied(toEnemy)){
            chat.sendClans(player, "You have already waged war on clan " + chat.getClanRelation(clan, toEnemy) + toEnemy.getName());
        }

        if(clan.isAllied(toEnemy)){
            chat.sendClans(player, "You cannot wage war on clan " + chat.getClanRelation(clan, toEnemy) + toEnemy.getName() + chat.textColour + " as you are allies");
            return;
        }

        if(clan.isPillageRelation(toEnemy)){
            chat.sendClans(player, "You cannot wage war on clan " + chat.getClanRelation(clan, toEnemy) + toEnemy.getName() + chat.textColour + " while there is a pillage");
            return;
        }

        pm.callEvent(new ClanEnemyEvent(clan, toEnemy));

    }

    public String getName() {
        return "Enemy";
    }

    public String getDescription() {
        return chat.commandDescription("Wage war on another clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c enemy <clanname>");
    }
}
