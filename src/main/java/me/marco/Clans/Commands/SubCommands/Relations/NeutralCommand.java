package me.marco.Clans.Commands.SubCommands.Relations;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Client.Client;
import me.marco.Events.Clans.CustomEvents.Relations.ClanAllyEvent;
import me.marco.Events.Clans.CustomEvents.Relations.ClanNeutralEvent;
import me.marco.Events.Clans.CustomEvents.Relations.ClanPillageEndEvent;
import me.marco.Handlers.ClanManager;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.InviteHandler;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Commands.ICommand;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class NeutralCommand implements ICommand {

    private InviteHandler inviteHandler;
    private ClientManager clientManager;
    private ClanManager clanManager;
    private Chat chat;
    private PluginManager pm;

    public NeutralCommand(Core instance){
        this.inviteHandler = instance.getInviteHandler();
        this.clientManager = instance.getClientManager();
        this.clanManager = instance.getClanManager();
        this.chat = instance.getChat();
        this.pm = instance.getServer().getPluginManager();
    }

    public void run(Player player, String[] args) {

        if(args.length == 1){
            chat.sendClans(player, "You must specify which clan you wish to neutral" + getCommandExample());
            return;
        }

        Client client = clientManager.getClient(player);
        if(!client.hasClan()){
            chat.sendClans(player, "You do not belong to a clan");
            return;
        }

        Clan clan = client.getClan();

        if(!client.isClanAdmin()){
            chat.sendClans(player, "Only clan owners and admins can neutral other clans");
            return;
        }

        Clan toNeutral = clanManager.getClan(args[1], player);

        if(toNeutral == null){
            return;
        }

        if(toNeutral == clan){
            chat.sendClans(player, "You cannot neutral your own clan");
            return;
        }

        if(clan.isPillaging(toNeutral)){
            if (inviteHandler.inviteExists(toNeutral, clan, InviteType.NEUTRAL)) {
                Invite existingInvite = inviteHandler.getInvite(toNeutral, clan, InviteType.NEUTRAL);
                pm.callEvent(new ClanPillageEndEvent(clan.getPillage(toNeutral)));
                pm.callEvent(new ClanNeutralEvent(clan, toNeutral, existingInvite));
                return;
            }
            pm.callEvent(new ClanPillageEndEvent(clan.getPillage(toNeutral)));
            pm.callEvent(new ClanNeutralEvent(clan, toNeutral));
            return;
        }

        if(clan.isNeutral(toNeutral)){
            chat.sendClans(player, "You are already neutral with clan " + chat.getClanRelation(clan, toNeutral) + toNeutral.getName());
            return;
        }

        if(clan.isAllied(toNeutral)){
            pm.callEvent(new ClanNeutralEvent(clan, toNeutral));
            return;
        }

        if(clan.isEnemied(toNeutral)) {
            if (inviteHandler.inviteExists(toNeutral, clan, InviteType.NEUTRAL)) {
                chat.sendClans(player, "A neutrality request has already been sent to clan " +
                        chat.getClanRelation(clan, toNeutral) + toNeutral.getName() + "");
                return;
            }
            if (inviteHandler.inviteExists(clan, toNeutral, InviteType.NEUTRAL)) {
                Invite existingInvite = inviteHandler.getInvite(clan, toNeutral, InviteType.NEUTRAL);
                pm.callEvent(new ClanNeutralEvent(clan, toNeutral, existingInvite));
                return;
            }
        }

        inviteHandler.createInvite(toNeutral, clan, InviteType.NEUTRAL);

    }

    public String getName() {
        return "Neutral";
    }

    public String getDescription() {
        return chat.commandDescription("Send a neutrality request to another clan");
    }

    public String getCommandExample() {
        return chat.commandExample("c neutral <clanname>");
    }
}
