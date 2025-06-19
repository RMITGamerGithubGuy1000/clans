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

public class AdminManager {

    public ArrayList<AdminClan> adminClanList = new ArrayList<AdminClan>();

    private Core instance;

    public AdminManager(Core instance){
        this.instance = instance;
    }

    public void createClan(Client creator, AdminClan adminClan){
        this.instance.getClanManager().addClan(adminClan);
        instance.getChat().sendAdminMessage(creator.getPlayer(), "You created clan " +
                instance.getChat().highlightName + adminClan.getName() + "" +
                instance.getChat().textColour + " with Safe Zone set to " +
                instance.getChat().highlightText + adminClan.isSafe() + "");
    }

    public void disbandClan(Client disbander, AdminClan adminClan){
        this.adminClanList.remove(adminClan);
        instance.getChat().sendAdminMessage(disbander.getPlayer(), "You disbanded clan " +
                instance.getChat().highlightName + adminClan.getName());
    }

    public void toggleSafeZone(Client toggler, AdminClan adminClan){
        adminClan.toggleSafe();
        String onOff = adminClan.isSafe() ? ChatColor.DARK_GREEN + "ON" : ChatColor.DARK_RED + "OFF";
        instance.getChat().sendAdminMessage(toggler.getPlayer(), "You toggled SafeZone " +
                onOff + "" + instance.getChat().textColour + " for " + instance.getChat().highlightName + adminClan.getName());
    }

    public void claimLand(AdminClan adminClan, Land land){
        adminClan.addClaim(land);
    }

    public void unclaimLand(AdminClan adminClan, Chunk chunk){
        adminClan.removeClaim(chunk);
    }

}
