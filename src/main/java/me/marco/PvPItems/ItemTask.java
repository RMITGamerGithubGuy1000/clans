package me.marco.PvPItems;

import me.marco.Base.Core;
import me.marco.Client.Client;

public abstract class ItemTask {

    private Client owner;
    private String name;
    private long timestamp;
    private long expiry;
    private boolean canExpire = true;
    private Core instance;
    private boolean forceExpire = false;

    public ItemTask(Client owner, String name, long expiry, Core instance){
        this.owner = owner;
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.expiry = expiry;
        this.instance = instance;
    }

    public ItemTask(Client owner, String name, long expiry, boolean canExpire, Core instance){
        this.owner = owner;
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.expiry = expiry;
        this.canExpire = canExpire;
        this.instance = instance;
    }

    public boolean isDeadClause(){
        return false;
    }

    public boolean runTick(){
        if(checkExpiry()) {
            onExpiry();
            return true;
        }
        onTick();
        return false;
    }

    public abstract void onTick();

    public boolean checkExpiry(){
        return isDeadClause() || forceExpire || (canExpire && System.currentTimeMillis() >= this.timestamp + expiry * 1000);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getExpiry() {
        return expiry;
    }

    public abstract void onExpiry();

    public boolean isCanExpire() {
        return canExpire;
    }

    public Client getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public Core getInstance() {
        return instance;
    }

    public boolean isForceExpire() {
        return forceExpire;
    }

    public void setForceExpire(boolean forceExpire) {
        this.forceExpire = forceExpire;
    }
}
