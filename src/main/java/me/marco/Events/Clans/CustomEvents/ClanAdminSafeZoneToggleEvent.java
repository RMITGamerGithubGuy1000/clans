package me.marco.Events.Clans.CustomEvents;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Client.Client;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanAdminSafeZoneToggleEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final AdminClan adminClan;
    private final Client toggler;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanAdminSafeZoneToggleEvent(AdminClan adminClan, Client toggler) {
        this.adminClan = adminClan;
        this.toggler = toggler;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public AdminClan getAdminClan(){
        return this.adminClan;
    }

    public Client getToggler(){
        return this.toggler;
    }

}
