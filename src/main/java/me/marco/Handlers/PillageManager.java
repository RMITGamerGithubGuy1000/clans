package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Relations.Pillage;
import me.marco.Events.Clans.CustomEvents.Relations.ClanPillageEndEvent;

import java.util.ArrayList;
import java.util.List;

public class PillageManager {

    private Core instance;

    public PillageManager(Core instance){
        this.instance = instance;
    }

    private List<Pillage> pillageList = new ArrayList<Pillage>();
//    private final int pillageTime = 600;
    private final int pillageTime = 90;

    public void startPillage(Clan pillaging, Clan toPillage){
        instance.getEnemyManager().removeEnemy(pillaging, toPillage);
        instance.getSqlRepoManager().getEnemyRepo().removeEnemy(pillaging, toPillage);

        long startTime = System.currentTimeMillis();

        Pillage pillage = new Pillage(pillaging, toPillage, startTime);
        pillaging.addPillage(pillage);
        toPillage.addPillage(pillage);
        pillageList.add(pillage);

        instance.getSqlRepoManager().getPillageRepo().addPillage(pillaging, toPillage, startTime);

        instance.getClanManager().sendClanAnnouncement(pillaging, "Your pillage on clan " +
                instance.getChat().getClanRelation(pillaging, toPillage) + toPillage.getName() + "" +
                instance.getChat().textColour + " has started. You have " + instance.getChat().highlightText + "10 minutes" +
                instance.getChat().textColour + " left.");

        instance.getClanManager().sendClanAnnouncement(toPillage, "Clan " +
                instance.getChat().getClanRelation(pillaging, toPillage) + pillaging.getName() + "" +
                instance.getChat().textColour + " has started their pillage on your clan. There is " + instance.getChat().highlightText + "10 minutes" +
                instance.getChat().textColour + " remaining in the pillage.");
    }

    public void checkPillages(){
        if(pillageList.size() <= 0) return;
        double timestamp = System.currentTimeMillis();
        List<Pillage> toCall = new ArrayList<Pillage>();
        for(Pillage pillage : pillageList){
            double timePassed = (pillage.getStartTime() / 1000 + pillageTime) - timestamp / 1000;
            System.out.println(timePassed);
            if(timePassed <= 0){
                toCall.add(pillage);
            }
        }
        for(Pillage pillage : toCall){
            instance.getServer().getPluginManager().callEvent(new ClanPillageEndEvent(pillage));
        }
    }

    public void loadPillage(Pillage pillage){
        this.pillageList.add(pillage);
        pillage.getToPillage().addPillage(pillage);
        pillage.getPillaging().addPillage(pillage);
    }

    public void addPillage(Pillage pillage){
        this.pillageList.add(pillage);
    }

    public void removePillage(Pillage pillage){
        this.pillageList.removeIf(listPillage -> listPillage == pillage);
    }

    public void endPillage(Pillage pillage){
        this.pillageList.removeIf(listPillage -> listPillage == pillage);
        Clan pillaging = pillage.getPillaging();
        Clan toPillage = pillage.getToPillage();
        pillaging.removePillage(pillage);;
        toPillage.removePillage(pillage);

        instance.getClanManager().sendClanAnnouncement(pillaging, "Your pillage on clan " +
                instance.getChat().getClanRelation(pillaging, toPillage) + toPillage.getName() + "" +
                instance.getChat().textColour + " has ended");

        instance.getClanManager().sendClanAnnouncement(toPillage, "The pillage from clan " +
                instance.getChat().getClanRelation(pillaging, toPillage) + pillaging.getName() + "" +
                instance.getChat().textColour + " has ended");
    }

    public void wipePillages(Clan clan){
        for(Pillage pillage : clan.getPillages()){
            endPillage(pillage);
        }
    }

}
