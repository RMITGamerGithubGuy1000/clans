package me.marco.Events;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Clans.Objects.Relations.Pillage;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Events.Clans.CustomEvents.*;
import me.marco.Events.Clans.CustomEvents.Relations.*;
import me.marco.Events.Clans.CustomEvents.Land.LandClaimEvent;
import me.marco.Events.Clans.CustomEvents.Land.LandOverclaimEvent;
import me.marco.Events.Clans.CustomEvents.Land.LandUnclaimEvent;
import me.marco.Events.Clans.TimedEvents.TimedThirtySecondsEvent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ClanListener extends CListener<Core> {

    public ClanListener(Core core) {
        super(core);
    }

    @EventHandler
    public void onClanCreate(ClanCreateEvent event){
        String clanName = event.getClanName();
        Player creator = event.getCreator();
        Client client = getInstance().getClientManager().getClient(creator);

        Clan clan = new Clan(clanName, client);
        getInstance().getClanManager().addClan(clan);

        client.setClan(clan);
        client.setClanRank(ClanRank.OWNER);
        clan.setOwner(client.getUUID());

        getInstance().getSqlRepoManager().getClanRepo().createClan(clan);
        getInstance().getSqlRepoManager().getClientRepo().setClan(client, client.getClanRank(), clan);
        getInstance().getChat().broadcastClanCreation(creator.getName(), clan);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onClanJoin(ClanJoinEvent event){
        Clan clan = event.getClan();
        getInstance().getInviteHandler().removeInvite(event.getInvite());
        Client joining = event.getJoining();
        getInstance().getClanManager().joinClan(joining, clan);
        getInstance().getSqlRepoManager().getClientRepo().setClan(joining, joining.getClanRank(), clan);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onClanLeave(ClanLeaveEvent event){
        Client leaver = event.getLeaver();
        Clan clan = event.getClan();
        getInstance().getClanManager().leaveClan(leaver);
        getInstance().getSqlRepoManager().getClientRepo().setClanLeave(leaver);
        getInstance().getClanManager().sendClanAnnouncement(clan, ChatColor.GOLD + leaver.getName() + "" + getInstance().getChat().textColour + " has left the clan");
        getInstance().getChat().sendLeftConfirmation(leaver.getPlayer(), clan);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onClanKick(ClanKickEvent event){
        Clan clan = event.getClan();
        Client kicked = event.getKicked();
        Client kicker = event.getKicker();
        getInstance().getClanManager().leaveClan(kicked);
        getInstance().getSqlRepoManager().getClientRepo().setClanLeave(kicked);
        getInstance().getClanManager().sendClanAnnouncement(clan, ChatColor.GOLD + kicked.getName() + "" + getInstance().getChat().textColour + " has been kicked from the clan by " + ChatColor.AQUA + kicker.getName());
        getInstance().getChat().sendLeftConfirmation(kicked.getPlayer(), clan);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onClanDisband(ClanDisbandEvent event){
        Clan clan = event.getClan();
        Client owner = event.getOwner();

        if(clan instanceof AdminClan){
            AdminClan adminClan = (AdminClan) clan;
            getInstance().getAdminManager().disbandClan(owner, adminClan);
            getInstance().getSqlRepoManager().getAdminRepo().deleteClan(adminClan);
            return;
        }

        getInstance().getSqlRepoManager().getClanRepo().deleteClan(clan);
        getInstance().getSqlRepoManager().getAllianceRepo().deleteAlliances(clan);
        getInstance().getSqlRepoManager().getEnemyRepo().deleteEnemies(clan);
        getInstance().getSqlRepoManager().getPillageRepo().wipePillages(clan);
        getInstance().getPillageManager().wipePillages(clan);
        for(Client client : clan.getMembers()){
            getInstance().getSqlRepoManager().getClientRepo().setClanLeave(client);
        }
        getInstance().getClanManager().disbandClan(clan, owner);
        getInstance().scoreboardManager().globalRefresh();
    }


    @EventHandler
    public void onClanPromote(ClanPromoteEvent event){
        Clan clan = event.getClan();
        Client promotee = event.getPromotee();
        Client promoter = event.getPromoter();
        ClanRank clanRank = event.getClanRank();
        getInstance().getClanManager().promoteMember(clan, promotee, promoter, clanRank);
        getInstance().getSqlRepoManager().getClientRepo().setClanRank(promotee, promotee.getClanRank());
    }

    @EventHandler
    public void onClanDemote(ClanDemoteEvent event){
        Clan clan = event.getClan();
        Client demotee = event.getDemotee();
        Client demoter = event.getDemoter();
        ClanRank clanRank = event.getClanRank();
        getInstance().getClanManager().demoteMember(clan, demotee, demoter, clanRank);
        getInstance().getSqlRepoManager().getClientRepo().setClanRank(demotee, demotee.getClanRank());
    }

    @EventHandler
    public void onClanTranferOwnership(ClanTransferOwnershipEvent event){
        Clan clan = event.getClan();
        Client previousOwner = event.getPreviousOwner();
        Client newOwner = event.getNewOwner();
        getInstance().getClanManager().transferOwnership(newOwner, previousOwner, clan);
        getInstance().getSqlRepoManager().getClanRepo().setClanOwner(clan, newOwner);
        getInstance().getSqlRepoManager().getClientRepo().setClanRank(previousOwner, previousOwner.getClanRank());
        getInstance().getSqlRepoManager().getClientRepo().setClanRank(newOwner, newOwner.getClanRank());
    }

    @EventHandler
    public void onLandClaim(LandClaimEvent event){
        Clan clan = event.getClan();
        Client claiming = event.getClaiming();
        Chunk chunk = event.getChunk();

        Land land = new Land(chunk);

        if(clan instanceof AdminClan){
            AdminClan adminClan = (AdminClan) clan;
            getInstance().getAdminManager().claimLand(adminClan, land);
            getInstance().getSqlRepoManager().getAdminRepo().updateLand(adminClan);
            getInstance().getChat().sendAdminMessage(claiming.getPlayer(), "You claimed land for clan " +
                    getInstance().getChat().highlightName + adminClan.getName() + "" + getInstance().getChat().textColour + " at " +
                    getInstance().getChat().highlightNumber + "X:" + chunk.getX() + "" + getInstance().getChat().textColour + ", " +
                    getInstance().getChat().highlightNumber + "Z:" + chunk.getZ());
            return;
        }

        getInstance().getClanManager().claimLand(clan, land);
        getInstance().getSqlRepoManager().getClanRepo().updateLand(clan);
        getInstance().getLandManager().getChunkOutlines(chunk, claiming.getPlayer());
        getInstance().getClanManager().sendClanAnnouncement(clan, ChatColor.AQUA + claiming.getName() + "" + getInstance().getChat().textColour + " has claimed land at " +
                getInstance().getChat().highlightNumber + "X:" + land.getX() + "" + getInstance().getChat().textColour + ", " + getInstance().getChat().highlightNumber + "Z:" + land.getZ());
    }


    @EventHandler
    public void onLandOverclaim(LandOverclaimEvent event){
        Clan losingClan = event.getLosingClan();
        if(losingClan instanceof AdminClan) return;

        Clan claimingClan = event.getClaimingClan();
        Client claiming = event.getClaiming();
        Chunk chunk = event.getChunk();

        Land land = new Land(chunk);
        getInstance().getClanManager().unclaimLand(losingClan, chunk);
        getInstance().getSqlRepoManager().getClanRepo().updateLand(losingClan);

        getInstance().getClanManager().sendClanAnnouncement(losingClan, getInstance().getChat().getClanRelation(claimingClan, losingClan) + claiming.getName() + "" + getInstance().getChat().textColour + " has overclaimed land from your clan " +
                "at " + getInstance().getChat().highlightNumber + "X:" + land.getX() + "" + getInstance().getChat().textColour + ", " + getInstance().getChat().highlightNumber + "Z:" + land.getZ());

        getInstance().getClanManager().claimLand(claimingClan, land);
        getInstance().getSqlRepoManager().getClanRepo().updateLand(claimingClan);

        getInstance().getClanManager().sendClanAnnouncement(claimingClan, ChatColor.AQUA + claiming.getName() + "" + getInstance().getChat().textColour + " has overclaimed land from "
                        + getInstance().getChat().getClanRelation(claimingClan, losingClan) + losingClan.getName() + "" + getInstance().getChat().textColour + " at " +
                        getInstance().getChat().highlightNumber + "X:" + land.getX() + "" + getInstance().getChat().textColour + ", " + getInstance().getChat().highlightNumber + "Z:" + land.getZ());

        getInstance().getLandManager().getChunkOutlines(chunk, claiming.getPlayer());
    }

    @EventHandler
    public void onLandUnclaim(LandUnclaimEvent event){
        Clan clan = event.getClan();
        Client unclaiming = event.getUnclaiming();
        Chunk chunk = event.getChunk();

        if(clan instanceof AdminClan){
            AdminClan adminClan = (AdminClan) clan;
            getInstance().getAdminManager().unclaimLand(adminClan, chunk);
            getInstance().getSqlRepoManager().getAdminRepo().updateLand(adminClan);
            getInstance().getChat().sendAdminMessage(unclaiming.getPlayer(), "You unclaimed land for clan " +
                    getInstance().getChat().highlightName + adminClan.getName() + "" + getInstance().getChat().textColour + " at " +
                    getInstance().getChat().highlightNumber + "X:" + chunk.getX() + "" + getInstance().getChat().textColour + ", " +
                    getInstance().getChat().highlightNumber + "Z:" + chunk.getZ());
            return;
        }

        getInstance().getClanManager().unclaimLand(clan, chunk);
        getInstance().getSqlRepoManager().getClanRepo().updateLand(clan);
        getInstance().getClanManager().sendClanAnnouncement(clan, ChatColor.AQUA + unclaiming.getName() + "" + getInstance().getChat().textColour + " has unclaimed land at " +
                getInstance().getChat().highlightNumber + "X:" + chunk.getX() + "" + getInstance().getChat().textColour + ", " + getInstance().getChat().highlightNumber + "Z:" + chunk.getZ());
    }

    @EventHandler
    public void onAlly(ClanAllyEvent event){
        Clan allyOwner = event.getAllyOwner();
        Clan toAlly = event.getToAlly();
        getInstance().getInviteHandler().removeInvite(event.getInvite());
        getInstance().getAllianceManager().addAlliance(allyOwner, toAlly);
        getInstance().getSqlRepoManager().getAllianceRepo().addAlliance(allyOwner, toAlly);
        getInstance().getClanManager().sendClanAnnouncement(allyOwner, "You are now allied with clan " + getInstance().getChat().getClanRelation(allyOwner, toAlly) + toAlly.getName());
        getInstance().getClanManager().sendClanAnnouncement(toAlly, "You are now allied with clan " + getInstance().getChat().getClanRelation(allyOwner, toAlly) + allyOwner.getName());
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onTrust(ClanTrustEvent event){
        Clan trustOwner = event.getTrustOwner();
        Clan toTrust = event.getToTrust();
        boolean trust = event.getTrust();
        getInstance().getAllianceManager().setTrust(trustOwner, toTrust, trust);
        getInstance().getSqlRepoManager().getAllianceRepo().updateTrust(trust, trustOwner, toTrust);
        if(trust){
            getInstance().getClanManager().sendClanAnnouncement(trustOwner, "You are now trusted with clan " +
                    getInstance().getChat().getClanRelation(trustOwner, toTrust) + toTrust.getName());
            getInstance().getClanManager().sendClanAnnouncement(toTrust, "You are now trusted with clan " +
                    getInstance().getChat().getClanRelation(trustOwner, toTrust) + trustOwner.getName());
            return;
        }
        getInstance().getClanManager().sendClanAnnouncement(trustOwner, "Your trust with clan " +
                getInstance().getChat().getClanRelation(trustOwner, toTrust) + toTrust.getName() +
                getInstance().getChat().textColour + " has been revoked");
        getInstance().getClanManager().sendClanAnnouncement(toTrust, "Your trust with clan " +
                getInstance().getChat().getClanRelation(trustOwner, toTrust) + trustOwner.getName() +
                getInstance().getChat().textColour + " has been revoked");
        getInstance().scoreboardManager().globalRefresh();
        }

    @EventHandler
    public void onNeutral(ClanNeutralEvent event){
        Clan neutralOwner = event.getNeutralOwner();
        Clan toNeutral = event.getToNeutral();
        Invite invite = event.getInvite();
        if(invite == null){
            getInstance().getAllianceManager().removeAlliance(neutralOwner, toNeutral);
            getInstance().getSqlRepoManager().getAllianceRepo().removeAlliance(neutralOwner, toNeutral);
            getInstance().getClanManager().sendClanAnnouncement(neutralOwner, "Your alliance with clan " +
                    getInstance().getChat().getClanRelation(neutralOwner, toNeutral) + toNeutral.getName() +
                    getInstance().getChat().textColour + " has been revoked");
            getInstance().getClanManager().sendClanAnnouncement(toNeutral, "Your alliance with clan " +
                    getInstance().getChat().getClanRelation(neutralOwner, toNeutral) + neutralOwner.getName() +
                    getInstance().getChat().textColour + " has been revoked");
            getInstance().scoreboardManager().globalRefresh();
            return;
        }
        getInstance().getInviteHandler().removeInvite(invite);
        getInstance().getEnemyManager().removeEnemy(neutralOwner, toNeutral);
        getInstance().getSqlRepoManager().getEnemyRepo().removeEnemy(neutralOwner, toNeutral);
            getInstance().getClanManager().sendClanAnnouncement(neutralOwner, "You are now neutral with clan " +
                getInstance().getChat().getClanRelation(neutralOwner, toNeutral) + toNeutral.getName());
        getInstance().getClanManager().sendClanAnnouncement(toNeutral, "You are now neutral with clan " +
                getInstance().getChat().getClanRelation(neutralOwner, toNeutral) + neutralOwner.getName());
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onEnemy(ClanEnemyEvent event){
        Clan enemyOwner = event.getEnemyWaging();
        Clan toEnemy = event.getToEnemy();
        getInstance().getEnemyManager().addEnemy(enemyOwner, toEnemy);
        getInstance().getSqlRepoManager().getEnemyRepo().addEnemy(enemyOwner, toEnemy);
        getInstance().getClanManager().sendClanAnnouncement(enemyOwner, "You have waged war on clan " + getInstance().getChat().getClanRelation(enemyOwner, toEnemy) + toEnemy.getName());
        getInstance().getClanManager().sendClanAnnouncement(toEnemy, "Clan " + getInstance().getChat().getClanRelation(enemyOwner, toEnemy) + enemyOwner.getName() + " " + getInstance().getChat().textColour + " has waged war on you");
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onDomUpdate(ClanDominanceUpdateEvent event){
        Clan gaining = event.getGainingClan();
        Clan losing = event.getLosingClan();
        getInstance().getEnemyManager().adjustDom(gaining, losing);
        if(gaining.canPillage(losing)){
            getInstance().getServer().getPluginManager().callEvent(new ClanStartPillageEvent(gaining, losing));
        }
    }

    @EventHandler
    public void onStartPillage(ClanStartPillageEvent event){
        Clan pillaging = event.getPillagingClan();
        Clan toPillage = event.getToPillage();

        getInstance().getPillageManager().startPillage(pillaging, toPillage);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onPillageEnd(ClanPillageEndEvent event){
        Pillage pillage = event.getPillage();

        getInstance().getSqlRepoManager().getPillageRepo().deletePillage(pillage);
        getInstance().getPillageManager().endPillage(pillage);
        getInstance().scoreboardManager().globalRefresh();
    }

    @EventHandler
    public void onClanTimedThirtySeconds(TimedThirtySecondsEvent event){
        getInstance().getPillageManager().checkPillages();
    }

    @EventHandler
    public void onAdminClanCreate(ClanAdminCreateEvent event){
        String clanName = event.getClanName();
        Player creator = event.getCreator();
        Client client = getInstance().getClientManager().getClient(creator);

        AdminClan adminClan = new AdminClan(clanName, client, event.isSafeZone());
        getInstance().getAdminManager().createClan(client, adminClan);
        getInstance().getSqlRepoManager().getAdminRepo().createClan(adminClan);
    }

    @EventHandler
    public void onAdminClanToggleSafeZone(ClanAdminSafeZoneToggleEvent event){
        AdminClan adminClan = event.getAdminClan();
        Client toggler = event.getToggler();
        getInstance().getAdminManager().toggleSafeZone(toggler, adminClan);
        getInstance().getSqlRepoManager().getAdminRepo().updateSafeZone(adminClan);
    }

}
