package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Client.Client;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class EconomyManager {

    private Core instance;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public EconomyManager(Core instance){
        this.instance = instance;
    }

    public void handleDeathSteal(Player killed, Player killer){
        Client killedClient = getInstance().getClientManager().getClient(killed);
        Client killerClient = getInstance().getClientManager().getClient(killer);
        double toSteal = killedClient.getMoney() * .1;
        double amount = Math.round(toSteal*100.0)/100.0;

        killedClient.removeMoney(amount);
        killerClient.addMoney(amount);
        getInstance().getChat().handleDeathStealMessage(killed, killer, amount);
        getInstance().getSqlRepoManager().getClientRepo().updateMoney(killedClient);
        getInstance().getSqlRepoManager().getClientRepo().updateMoney(killerClient);
        killedClient.getScoreboard().refresh();
        killerClient.getScoreboard().refresh();
    }

    public Core getInstance(){
        return this.instance;
    }

}
