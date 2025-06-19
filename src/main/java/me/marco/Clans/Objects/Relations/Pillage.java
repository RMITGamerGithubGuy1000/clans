package me.marco.Clans.Objects.Relations;

import me.marco.Clans.Objects.Clan.Clan;

public class Pillage {

    private Clan pillaging;
    private Clan toPillage;
    private double startTime;

    public Pillage(Clan pillaging, Clan toPillage, double startTime){
        this.pillaging = pillaging;
        this.toPillage = toPillage;
        this.startTime = startTime;
    }

    public Clan getPillaging() {
        return pillaging;
    }

    public Clan getToPillage() {
        return toPillage;
    }

    public double getStartTime() {
        return startTime;
    }
}
