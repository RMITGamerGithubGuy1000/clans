package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.Client;
import me.marco.Utility.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ClientManager {

    private Core instance;

    public ClientManager(Core instance){
        this.instance = instance;
    }

    public ArrayList<Client> clientList = new ArrayList<Client>();

    public void addClient(Client client){
        this.clientList.add(client);
    }

    public Client getClientAlsoNew(Player player) {
        if(!clientExists(player)){
            Client client = new Client(player);
            addClient(client);
            //getInstance().getSqlRepoManager().getClientRepo().createClient(client);
            return client;
        }
        return getClient(player);
    }

    public Client getClient(Player player){
        for(Client client : clientList){
            if(client.getUUID().equals(player.getUniqueId())){
                return client;
            }
        }
        return null;
    }

    public Client getClient(UUID uuid){
        for(Client client : clientList){
            if(client.getUUID().equals(uuid)){
                return client;
            }
        }
        return null;
    }

    public Client getClient(String uuid){
        for(Client client : clientList){
            if(client.getUUID().toString().equalsIgnoreCase(uuid)){
                return client;
            }
        }
        return null;
    }

    public Client getClient(String name, Player finder){
        List<Client> matchList = new LinkedList<>();
        for(Client client : clientList){
            String clientName = client.getName().toLowerCase();
            if(clientName.equalsIgnoreCase(name.toLowerCase())){
                return client;
            }
            if(clientName.contains(name.toLowerCase())){
                matchList.add(client);
            }
        }
        if(matchList.size() == 1){
            return matchList.get(0);
        }
        if(matchList.size() > 1) {
            instance.getChat().sendModule(finder, "Matches for " + instance.getChat().highlightText + name +
                    instance.getChat().textColour + ": " + instance.getChat().findPlayerMatchesString(matchList), "Find");
        }
        return null;
    }

    public boolean clientExists(Player player){
        for(Client client : clientList){
            if(client.getUUID().equals(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public void toggleAdminMode(Client client){
        client.toggleAdminMode();
        instance.getChat().sendAdminModeToggle(client);
    }

    private Core getInstance(){
        return this.instance;
    }

}
