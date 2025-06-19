package me.marco.Admin.AdminClans;

import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;

import java.util.List;
import java.util.UUID;

public class AdminClan extends Clan {

    private boolean isSafe;

    public AdminClan(String name, Client owner, boolean isSafe) {
        super(name, owner);
        this.isSafe = isSafe;
    }

    public AdminClan(String name, UUID owner, List<Land> land, boolean isSafe) {
        super(name, owner, land);
        this.isSafe = isSafe;
    }

    public boolean isSafe(){
        return this.isSafe;
    }

    public void toggleSafe(){
        this.isSafe = !this.isSafe;
    }

}
