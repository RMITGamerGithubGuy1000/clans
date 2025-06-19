package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Scoreboard.uScoreboard;
import org.bukkit.entity.Player;

public class ScoreboardManager {

    private Core core;

    public ScoreboardManager(Core core){
        this.core = core;
    }

    public void loadScoreboards(){
        for(Player player : core.getServer().getOnlinePlayers()){
            Client client = core.getClientManager().getClient(player);
            setScoreboard(client);
        }
    }

    public void setScoreboard(Client client){
        if(client.hasScoreboard()) {
            client.updateScoreboard();
            return;
        }
        client.setScoreboard(new uScoreboard(client, core, ""));
    }

    public void globalRefresh() {
        for(Player player : core.getServer().getOnlinePlayers()){
            Client client = core.getClientManager().getClient(player);
            client.getScoreboard().updateClans();
        }
    }
}
