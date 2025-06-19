package me.marco.Clans.Objects.Relations;

import me.marco.Clans.Objects.Clan.Clan;

public class Alliance {

    private Clan allianceOwner, allianceWith;
    private boolean trusted;

    public Alliance(Clan allianceOwner, Clan allianceWith){
        this.allianceOwner = allianceOwner;
        this.allianceWith = allianceWith;
        this.trusted = false;
    }

    public Alliance(Clan allianceOwner, Clan allianceWith, boolean trusted){
        this.allianceOwner = allianceOwner;
        this.allianceWith = allianceWith;
        this.trusted = trusted;
    }

    public Clan getAllianceOwner() {
        return allianceOwner;
    }

    public Clan getAllianceWith() {
        return allianceWith;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrust(boolean trust){
        this.trusted = trust;
    }

}
