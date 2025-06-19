package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Invites.Inviteable;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InviteHandler {

    private Core instance;

    public InviteHandler(Core instance) {
        this.instance = instance;
    }

    public List<Invite> inviteList = new ArrayList<Invite>();

    public void createInvite(Inviteable toInvite, Inviteable inviter, InviteType inviteType){
        Invite invite = new Invite(toInvite, inviter, inviteType);
        inviteList.add(invite);
        instance.getChat().sendInviteMessage(invite);
        new BukkitRunnable(){
            public void run(){
                if(inviteExists(invite)){
                    instance.getChat().sendInviteExpiry(invite);
                    removeInvite(invite);
                }
            }
        }.runTaskLater(instance, inviteType.getDuration() * 20);
    }

    public boolean inviteExists(Inviteable inviting, Inviteable inviter, InviteType inviteType) {
        return inviteList.stream().anyMatch(invite ->
                invite.getInvited().equals(inviting) &&
                        invite.getInviter().equals(inviter) &&
                        invite.getInviteType().equals(inviteType));
    }

    public Invite getInvite(Inviteable inviting, Inviteable inviter, InviteType inviteType) {
        return inviteList.stream().filter(invite ->
                invite.getInvited().equals(inviting) &&
                        invite.getInviter().equals(inviter) &&
                        invite.getInviteType().equals(inviteType)).findFirst().orElse(null);
    }

    public void removeInvite(Inviteable inviting, Inviteable inviter, InviteType inviteType) {
        inviteList.removeIf(invite ->
                invite.getInvited().equals(inviting) &&
                        invite.getInviter().equals(inviter) &&
                        invite.getInviteType().equals(inviteType));
    }

    public boolean inviteExists(Invite invite) {
        return inviteList.contains(invite);
    }

    public void removeInvite(Invite invite) {
        inviteList.remove(invite);
    }

}
