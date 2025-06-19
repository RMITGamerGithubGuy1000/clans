package me.marco.Clans.Objects.Clan;

public enum ClanRank {
    NOMAD("Nomad", "N/A"),
    RECRUIT("Recruit", "R"),
    MEMBER("Member", "M"),
    ADMIN("Admin", "A"),
    OWNER("Owner", "O");

    private String name;
    private String prefix;

    ClanRank(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName(){
        return this.name;
    }

    public String getPrefix(){
        return this.prefix;
    }

}
