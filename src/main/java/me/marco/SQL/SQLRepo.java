package me.marco.SQL;

import me.marco.Base.Core;
import me.marco.Client.Client;
import org.bukkit.Location;

public abstract class SQLRepo {

    private Core instance;

    public SQLRepo(Core instance){
        this.instance = instance;
    }

    public abstract void createRepo();
    public abstract void loadData();

    public SQLSlave getSQL() { return this.instance.getSQL(); }

    public Core getInstance() { return this.instance; }

}
