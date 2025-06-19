package me.marco.Tags;

import me.marco.Base.Core;
import me.marco.Client.Client;
import org.bukkit.entity.Player;

public class PvPTag extends Tag{

    private Player target, damager;
    private String cause;

    public PvPTag(Client owner, Player target, Player damager, String cause, Core instance){
        super(owner, "PvP", 10, instance);
        this.target = target;
        this.damager = damager;
        this.cause = cause;
    }

    public Player getTarget() {
        return target;
    }

    public Player getDamager() {
        return damager;
    }

    public String getCause() {
        return cause;
    }

    public boolean isExpired(){
        return System.currentTimeMillis() >= this.getTimestamp() + this.getExpiry() * 1000;
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onExpiry() {

    }
}
