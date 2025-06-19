package me.marco.Events.Clans.CustomEvents.Relations;

import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanEnemyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan enemyWaging;
    private final Clan toEnemy;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ClanEnemyEvent(Clan enemyWaging, Clan toEnemy) {
        this.enemyWaging = enemyWaging;
        this.toEnemy = toEnemy;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clan getEnemyWaging() {
        return this.enemyWaging;
    }

    public Clan getToEnemy() {
        return this.toEnemy;
    }

}
