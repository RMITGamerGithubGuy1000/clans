package me.marco.Handlers;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClanManager {

    public ArrayList<Clan> clanList = new ArrayList<Clan>();

    private Core instance;

    public ClanManager(Core instance){
        this.instance = instance;
    }

    public void addClan(Clan clan){
        this.clanList.add(clan);
    }

    public boolean canCastInSafeZone(Player player, String skill){
        Clan clan = getInstance().getLandManager().getOwningClan(player.getLocation().getChunk());
        if(clan != null){
            if(clan instanceof AdminClan){
                AdminClan adminClan = (AdminClan) clan;
                if(adminClan.isSafe() && !getInstance().getClientManager().getClient(player).hasCombatTag()){
                    getInstance().getChat().sendClans(player, "You cannot use " + ChatColor.GREEN + skill + "" +
                            getInstance().getChat().textColour + " in " + ChatColor.YELLOW + adminClan.getName() + "");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canBeAffected(){
        return false;
    }

    public void joinClan(Client client, Clan clan){
        client.setClan(clan);
        clan.addMember(client);
        client.setClanRank(ClanRank.RECRUIT);
        instance.getClanManager().sendClanAnnouncement(clan, ChatColor.AQUA + client.getName() + "" + instance.getChat().textColour + " has joined the clan");
    }

    public void leaveClan(Client client){
        Clan clan = client.getClan();
        client.setClan(null);
        clan.removeMember(client);
        client.setClanRank(ClanRank.NOMAD);
    }

    public void disbandClan(Clan clan, Client owner){
        instance.getChat().broadcastClanDisband(owner, clan);
        for(Client clanMember : clan.getMembers()){
            clanMember.setClan(null);
            clanMember.setClanRank(ClanRank.NOMAD);
        }
        clanList.remove(clan);
    }

    public void promoteMember(Clan clan, Client promotee, Client promoter, ClanRank clanRank){
        promotee.setClanRank(clanRank);
        sendClanAnnouncement(clan, ChatColor.AQUA + promotee.getName() + "" + instance.getChat().textColour + " has been promoted to clan " + instance.getChat().highlightText + clanRank.getName() +
                instance.getChat().textColour + " by " + ChatColor.AQUA + promoter.getName());
    }

    public void demoteMember(Clan clan, Client demotee, Client demoter, ClanRank clanRank){
        demotee.setClanRank(clanRank);
        sendClanAnnouncement(clan, ChatColor.AQUA + demotee.getName() + "" + instance.getChat().textColour + " has been demoted to clan " + instance.getChat().highlightText + clanRank.getName() +
                instance.getChat().textColour + " by " + ChatColor.AQUA + demoter.getName());
    }

    public void transferOwnership(Client newOwner, Client previousOwner, Clan clan){
        previousOwner.setClanRank(ClanRank.ADMIN);
        newOwner.setClanRank(ClanRank.OWNER);
        clan.setOwner(newOwner.getUUID());
        sendClanAnnouncement(clan, ChatColor.AQUA + previousOwner.getName() + "" + instance.getChat().textColour + " has transferred clan " + instance.getChat().highlightText
                + "ownership" + instance.getChat().textColour + " to " + ChatColor.AQUA + newOwner.getName());
    }

    public void claimLand(Clan clan, Land land){
        clan.addClaim(land);
    }

    public void unclaimLand(Clan clan, Chunk chunk){
        clan.removeClaim(chunk);
    }

    public boolean clanExists(String clanName){
        return getClan(clanName) != null;
    }

    public boolean adminClanExists(String clanName){
        return getAdminClan(clanName) != null;
    }

    public boolean clanExistsAll(String clanName){
        return getClan(clanName) != null || getAdminClan(clanName) != null;
    }

    public void sendClanSummary(Player player, Clan clan){
        Client client = instance.getClientManager().getClient(player);
        player.sendMessage(instance.getChat().prefixColour + "[Clan] " + ChatColor.BLUE + clan.getName());
        String block = ChatColor.RED + "| ";
        player.sendMessage(block + ChatColor.YELLOW + "Owner: " + ChatColor.GREEN + instance.getClientManager().getClient(clan.getOwner()).getName());
        player.sendMessage(block + ChatColor.YELLOW + "Claims: " + ChatColor.AQUA + clan.getClaimsAmount() + "/" + clan.getMaxClaims());
        player.sendMessage(block + ChatColor.YELLOW + "Members: " + instance.getChat().membersString(clan.getMembers()));
        player.sendMessage(block + ChatColor.YELLOW + "Allies: " + instance.getChat().alliesString(client, clan.getAlliances()));
        player.sendMessage(block + ChatColor.YELLOW + "Enemies: " + instance.getChat().enemiesString(client, clan.getEnemies()));
    }

    public void sendClanAnnouncement(Clan clan, String message){
        for(Client client : clan.getMembers()){
            Player player = client.getPlayer();
            if(player != null) {
                instance.getChat().sendClans(player, message);
            }
        }
    }

    public Clan getClan(String name){
        if(name == null){ return null; };
        List<Clan> matchList = new LinkedList<>();
        for(Clan clan : clanList){
            if(clan instanceof AdminClan) continue;
            String clanName = clan.getName().toLowerCase();
            if(clanName.equalsIgnoreCase(name.toLowerCase())){
                return clan;
            }
            if(clanName.contains(name.toLowerCase())){
                matchList.add(clan);
            }
        }
        if(matchList.size() == 1){
            return matchList.get(0);
        }
        List<Clan> playerMatchList = new LinkedList<>();
        for(Client client : instance.getClientManager().clientList){
            if(client.hasClan()){
                Clan clan = client.getClan();
                String clientName = client.getName().toLowerCase();
                if(clientName.equalsIgnoreCase(name.toLowerCase())){
                    return clan;
                }
                if(clientName.contains(name.toLowerCase())){
                    playerMatchList.add(clan);
                }
            }
        }
        if(playerMatchList.size() == 1){
            return playerMatchList.get(0);
        }
        return null;
    }

    public Clan getClan(String name, Player finder){
        if(name == null){ return null; };
        List<Clan> matchList = new LinkedList<>();
        for(Clan clan : clanList){
            if(clan instanceof AdminClan) continue;
            String clanName = clan.getName().toLowerCase();
            if(clanName.equalsIgnoreCase(name.toLowerCase())){
                return clan;
            }
            if(clanName.contains(name.toLowerCase())){
                matchList.add(clan);
            }
        }
        if(matchList.size() == 1){
            return matchList.get(0);
        }
        List<Clan> playerMatchList = new LinkedList<>();
        for(Client client : instance.getClientManager().clientList){
            String clientName = client.getName().toLowerCase();
            if(client.hasClan()){
                Clan clan = client.getClan();
                if(clientName.equalsIgnoreCase(name.toLowerCase())){
                    return clan;
                }
                if(clientName.contains(name.toLowerCase())){
                    playerMatchList.add(clan);
                }
            }else{
                if(clientName.equalsIgnoreCase(name.toLowerCase())){
                    instance.getChat().sendClans(finder, instance.getChat().highlightName + client.getName() + "" +
                            instance.getChat().textColour + " does not belong to a clan");
                    return null;
                }
            }
        }
        if(playerMatchList.size() == 1){
            return playerMatchList.get(0);
        }
        instance.getChat().sendClans(finder, "Could not find clan " + instance.getChat().highlightName + name);
        return null;
    }

    public AdminClan getAdminClan(String name){
        if(name == null){ return null; };
        List<AdminClan> matchList = new LinkedList<>();
        for(Clan clan : clanList) {
            if (clan instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) clan;
                String clanName = clan.getName().toLowerCase();
                if (clanName.equalsIgnoreCase(name.toLowerCase())) {
                    return adminClan;
                }
                if (clanName.contains(name.toLowerCase())) {
                    matchList.add(adminClan);
                }
            }
        }
        if(matchList.size() == 1){
            return matchList.get(0);
        }
        return null;
    }

    public Core getInstance() {
        return instance;
    }
}
