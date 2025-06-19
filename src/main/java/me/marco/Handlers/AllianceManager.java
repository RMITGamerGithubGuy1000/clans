package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Relations.Alliance;
import me.marco.Clans.Objects.Clan.Clan;

public class AllianceManager {

    private Core instance;

    public AllianceManager(Core instance){
        this.instance = instance;
    }

    public void addAlliance(Clan allyOwner, Clan toAlly){
        allyOwner.addAlliance(new Alliance(allyOwner, toAlly));
        toAlly.addAlliance(new Alliance(toAlly, allyOwner));
    }

    public void removeAlliance(Clan allyOwner, Clan toNeutral){
        allyOwner.removeAlliance(toNeutral);
        toNeutral.removeAlliance(allyOwner);
    }

    public void setTrust(Clan clan1, Clan clan2, boolean trust){
        Alliance clan1Alliance = clan1.getAlliance(clan2);
        Alliance clan2Alliance = clan2.getAlliance(clan1);
        clan1Alliance.setTrust(trust);
        clan2Alliance.setTrust(trust);
    }

}
