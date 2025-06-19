package me.marco.Clans.Objects.Clan;

import me.marco.Clans.Objects.Relations.Alliance;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Clans.Objects.Relations.Enemy;
import me.marco.Clans.Objects.Relations.Pillage;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Invites.Inviteable;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan implements Inviteable {

    private String name;
    private List<Land> land;
    private UUID owner;
    private List<Client> members;

    private List<Alliance> allianceList;
    private List<Enemy> enemyList;
    private List<Pillage> pillageList;

    private Location home;

    public Clan(String name, Client owner){
        this.name = name;
        this.land = new ArrayList<Land>();
        this.owner = owner.getUUID();
        this.members = new ArrayList<Client>();
        this.members.add(owner);
        this.allianceList = new ArrayList<Alliance>();
        this.enemyList = new ArrayList<Enemy>();
        this.pillageList = new ArrayList<Pillage>();
    }

    public Clan(String name, UUID owner, List<Land> land){
        this.name = name;
        this.land = land;
        this.owner = owner;
        this.members = new ArrayList<Client>();
        this.allianceList = new ArrayList<Alliance>();
        this.enemyList = new ArrayList<Enemy>();
        this.pillageList = new ArrayList<Pillage>();
    }

    public Clan(String name, UUID owner, List<Land> land, Location home){
        this.name = name;
        this.land = land;
        this.owner = owner;
        this.members = new ArrayList<Client>();
        this.allianceList = new ArrayList<Alliance>();
        this.enemyList = new ArrayList<Enemy>();
        this.pillageList = new ArrayList<Pillage>();
        this.home = home;
    }

    public UUID getOwner(){ return this.owner; }

    public void setOwner(UUID owner){
        this.owner = owner;
    }

    public String getName(){
        return this.name;
    }

    public List<Client> getMembers(){ return this.members; }

    public boolean containsMember(Client client){
        return this.members.contains(client);
    }

    public void addMember(Client client){
        this.members.add(client);
    }

    public void removeMember(Client client){
        this.members.remove(client);
    }

    public List<Land> getLand(){
        return this.land;
    }

    public boolean canClaim(){
        return this.land.size() < this.getMaxClaims();
    }

    public boolean isOverClaimed(){
        return this.land.size() > this.getMaxClaims();
    }

    public int getMaxClaims(){
        int maxClaims = 2 + this.members.size();
        if(maxClaims > 8){
            maxClaims = 8;
        }
        return maxClaims;
    }

    public int getClaimsAmount(){
        return this.land.size();
    }

    public void addClaim(Land land){
        this.land.add(land);
    }

    public void removeClaim(Chunk chunk){
        land.removeIf(land -> (land.getX() == chunk.getX() && land.getZ() == chunk.getZ()));
    }

    public String membersToString(){
        int size = this.getMembers().size();
        if(size < 1) return "";
        if(size == 1) return this.getMembers().get(0).getUUID().toString();
        String members = "";
        for(int i = 0; i < size; i++){
            Client client = this.getMembers().get(0);
            if(i == size - 1){
                members += client.getUUID().toString();
            }else{
                members += client.getUUID().toString() + ",";
            }
        }
        return members;
    }

    public String landToString(){
        int size = this.getLand().size();

        if(size < 1) return "";
        if(size == 1){
            Land land = this.getLand().get(0);
            return land.getX() + "," + land.getZ();
        }

        String landString = "";
        for(int i = 0; i < size; i++){
            Land land = this.getLand().get(i);
            if(i == size - 1){
                landString += land.getX() + "," + land.getZ() + "";
            }else{
                landString += land.getX() + "," + land.getZ() + " ";
            }
        }
        return landString;
    }

    public List<Alliance> getAlliances(){ return this.allianceList; }

    public void addAlliance(Alliance alliance){
        this.allianceList.add(alliance);
    }

    public void removeAlliance(Clan toNeutral){
        this.allianceList.removeIf(listAlliance -> listAlliance.getAllianceWith() == toNeutral);
    }

    public boolean isAllied(Clan toAlly){
        return this.allianceList.stream().anyMatch(listAlliance -> (
                listAlliance.getAllianceOwner() == this
                        && listAlliance.getAllianceWith() == toAlly));
    }

    public boolean isNeutral(Clan toNeutral){
        return !isAllied(toNeutral) && !isEnemied(toNeutral);
    }

    public boolean isTrusted(Clan toAlly){
        return this.allianceList.stream().anyMatch(listAlliance ->
                        listAlliance.getAllianceWith() == toAlly
                        && listAlliance.isTrusted());
    }

    public Alliance getAlliance(Clan clan){
        return this.allianceList.stream().filter(listAlliance -> listAlliance.getAllianceWith() == clan).findFirst().orElse(null);
    }

    public List<Enemy> getEnemies(){ return this.enemyList; }

    public void addEnemy(Enemy enemy){
        this.enemyList.add(enemy);
    }

    public void removeEnemy(Clan toRemove){
        this.enemyList.removeIf(listEnemy -> listEnemy.getEnemyWith() == toRemove);
    }

    public boolean isEnemied(Clan toCheck){
        return this.enemyList.stream().anyMatch(listEnemy -> listEnemy.getEnemyWith() == toCheck);
    }

    public Enemy getEnemy(Clan toGet){
        return this.enemyList.stream().filter(listEnemy -> listEnemy.getEnemyWith() == toGet).findFirst().orElse(null);
    }

    public void addDominance(Clan clan){
        getEnemy(clan).addDominance();
    }

    public void reduceDominance(Clan clan){
        getEnemy(clan).removeDominance();
    }

    public int getDominance(Clan toCheck){
        return getEnemy(toCheck).getDominance();
    }

    public boolean canPillage(Clan toCheck){
        return (isEnemied(toCheck) && getDominance(toCheck) >= 2);
    }

    public void addPillage(Pillage toAdd){
        this.pillageList.add(toAdd);
    }

    public void removePillage(Pillage toRemove){
        this.pillageList.removeIf(listPillage -> listPillage == toRemove);
    }

    public Pillage getPillage(Clan toGet){
        return this.pillageList.stream().filter(listPillage -> listPillage.getToPillage() == toGet).findFirst().orElse(null);
    }

    public boolean isBeingPillagedBy(Clan toCheck){
        return this.pillageList.stream().anyMatch(listPillage -> listPillage.getPillaging() == toCheck);
    }

    public boolean isPillaging(Clan toCheck){
        return this.pillageList.stream().anyMatch(listPillage -> listPillage.getToPillage() == toCheck);
    }

    public boolean isPillageRelation(Clan toCheck){
        return isPillaging(toCheck) || isBeingPillagedBy(toCheck);
    }

    public boolean isBeingPillaged(){
        return this.pillageList.stream().anyMatch(listPillage -> listPillage.getToPillage() == this);
    }

    public List<Pillage> getPillages(){ return this.pillageList; }

    public void setHome(Location location) {
        this.home = location;
    }

    public boolean hasHome(){
        return this.home != null;
    }

    public Location getHome() {
        return this.home;
    }
}
