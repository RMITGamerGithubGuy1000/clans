package me.marco.Clans.Objects.Invites;

public enum InviteType {
    CLAN(60, "invite"),
    ALLY(120, "alliances"),
    TRUST(120, "trust"),
    NEUTRAL(120, "neutrality");

    private int duration;
    private String name;

    InviteType(int duration, String name){
        this.duration = duration;
        this.name = name;
    }

    public int getDuration(){
        return this.duration;
    }

    public String getName() { return this.name; }

}
