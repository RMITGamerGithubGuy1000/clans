package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Relations.Enemy;
import org.bukkit.ChatColor;

public class EnemyManager {

    private Core instance;

    public EnemyManager(Core instance){
        this.instance = instance;
    }

    public void addEnemy(Clan enemyOwner, Clan toEnemy){
        enemyOwner.addEnemy(new Enemy(enemyOwner, toEnemy));
        toEnemy.addEnemy(new Enemy(toEnemy, enemyOwner));
    }

    public void removeEnemy(Clan enemyOwner, Clan toEnemy){
        enemyOwner.removeEnemy(toEnemy);
        toEnemy.removeEnemy(enemyOwner);
    }

    public void adjustDom(Clan gaining, Clan losing){
        Enemy gainingEnemy = gaining.getEnemy(losing);
        Enemy losingEnemy = losing.getEnemy(gaining);
        int gainingDom = gainingEnemy.getDominance();
        int losingDom = losingEnemy.getDominance();
        if(gainingDom == 0 && losingDom == 0){
            gainingEnemy.addDominance();
        } else if(gainingDom > losingDom){
            gainingEnemy.addDominance();
        }else if(losingDom > gainingDom) {
            losing.reduceDominance(gaining);
            if(losing.getEnemy(gaining).getDominance() == 0){
                gainingEnemy.addDominance();
            }
        }
        instance.getClanManager().sendClanAnnouncement(gaining, "Dominance gained on clan " +
                instance.getChat().getClanRelation(gaining, losing) + losing.getName() + "" + instance.getChat().textColour + ": " +
                instance.getChat().dominanceString(gainingEnemy, losingEnemy));

        instance.getClanManager().sendClanAnnouncement(losing, "Dominance lost to clan " +
                instance.getChat().getClanRelation(losing, gaining) + gaining.getName() + "" + instance.getChat().textColour + ": " +
                instance.getChat().dominanceString(losingEnemy, gainingEnemy));

        instance.getSqlRepoManager().getEnemyRepo().updateDominance(gaining, losing);
    }

}
