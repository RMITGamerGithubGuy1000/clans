package me.marco.Scoreboard;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Skills.Builders.eClassType;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class uScoreboard {

    private Client owner;
    private String[] content;
    private Scoreboard board;
    private Objective objective;
    private Core core;

    private final String SERVER_TITLE = ChatColor.GOLD + ChatColor.BOLD.toString() + "Test Server";
    private final String CLAN_PREFIX = ChatColor.BLUE + "Clan: ";
    private final String LAND_PREFIX = ChatColor.BLUE + "Land: ";
    private final String CLASS_PREFIX = ChatColor.BLUE + "Class: ";
    private final String MONEY_PREFIX = ChatColor.BLUE + "Money: ";

    public uScoreboard(Client owner, Core instance, String... content) {
        this.owner = owner;
        this.content = content;
        this.core = instance;
        initialiseBoard();
    }

    private void initialiseBoard() {
        this.board = core.getServer().getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("ServerName", "dummy", SERVER_TITLE);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective = obj;
        initialise();
    }

    public void initialise(){
        setClan();
        setLand(null);
        setClass(null);
        setMoney();
        updateClans();
        setBoard();
    }

    public void refresh(){
        setClan();
        updateClans();
        setBoard();
        setMoney();
    }

    public void updateClans(){
        core.getServer().getOnlinePlayers().forEach(online -> {
            Team team;
            if(board.getTeam(online.getName()) == null){
                team = this.board.registerNewTeam(online.getName());
            }
            team = this.board.getTeam(online.getName());
            Client onlineClient = getInstance().getClientManager().getClient(online);
            ChatColor relationColour = getInstance().getChat().getClanRelation(onlineClient, owner);
            String prefix =
                    onlineClient.hasClan() ?  relationColour + ChatColor.BOLD.toString() + onlineClient.getClan().getName() + " " : "";
            team.setPrefix(prefix);
            team.setColor(relationColour);
            team.addEntry(online.getName());
        });
    }

    public void updateLand(Clan clan){
        setLand(clan);
    }
    public void updateClass(eClassType classType){
        setClass(classType);
    }

    private void setMoney(){
        Team money = getTeam("money", MONEY_PREFIX);
        money.addEntry(MONEY_PREFIX);
        money.setSuffix(ChatColor.GREEN.toString() + owner.getMoney());
        this.objective.getScore(MONEY_PREFIX).setScore(0);
    }

    private void setClan() {
        Team clan = getTeam("clanName", CLAN_PREFIX);
        clan.addEntry(CLAN_PREFIX);
        clan.setSuffix(getClan(owner));
        this.objective.getScore(CLAN_PREFIX).setScore(4);
    }

    private void setLand(Clan clan) {
        Team land = getTeam("land", LAND_PREFIX);
        land.addEntry(LAND_PREFIX);
        land.setSuffix(getLand(clan));
        this.objective.getScore(LAND_PREFIX).setScore(3);
    }

    private void setClass(eClassType classType) {
        Team classTeam = getTeam("class", CLASS_PREFIX);
        classTeam.addEntry(CLASS_PREFIX);
        classTeam.setSuffix(getClassName(classType));
        this.objective.getScore(CLASS_PREFIX).setScore(2);
    }

    private Team getTeam(String teamName, String entry){
        Team team = this.board.getTeam(teamName);
        if(team != null) return team;
        team = board.registerNewTeam(teamName);
        team.addEntry(entry);
        return team;
    }

    private void setBoard() {
        getPlayer().setScoreboard(this.board);
    }

    private Core getInstance() {
        return this.core;
    }

    private String getClan(Client client) {
        if (!client.hasClan()) return ChatColor.YELLOW + "None";
        return ChatColor.DARK_AQUA + client.getClan().getName();
    }

    private String getLand(Clan clan){
        if(clan == null) return ChatColor.DARK_GREEN + "Wilderness";
        return getInstance().getChat().getClanRelation(owner, clan) + clan.getName();
    }

    private String getClassName(eClassType classType){
        if(classType == null) return ChatColor.YELLOW + "None";
        return classType.getColour() + classType.getName();
    }

    private Player getPlayer() {
        return core.getServer().getPlayer(owner.getUUID());
    }


}
