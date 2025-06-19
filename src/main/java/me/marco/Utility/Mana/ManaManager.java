package me.marco.Utility.Mana;

import me.marco.Base.Core;
import org.bukkit.entity.Player;

public class ManaManager {

    private Core instance;

    public ManaManager(Core instance){
        this.instance = instance;
    }

    public void handleMana(){
        for(Player player : getInstance().getServer().getOnlinePlayers()){
            if(player.getLevel() < 100){
                player.setLevel(player.getLevel() + 1);
            }
            if(player.getLevel() > 100){
                player.setLevel(100);
            }
        }
    }

    public void handleChannelBar(){
        for(Player player : getInstance().getServer().getOnlinePlayers()){
            float toBe = player.getExp() + .005f;
            if(toBe > .99f) toBe = .99f;
            player.setExp(toBe);
        }

    }

    public Core getInstance() {
        return instance;
    }

}
