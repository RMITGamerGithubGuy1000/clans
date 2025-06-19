package me.marco.Clans.Objects.Relations;

import me.marco.Clans.Objects.Clan.Clan;

public class Enemy {

    private Clan enemyOwner, enemyWith;
    private int dominance;

    public Enemy(Clan enemyOwner, Clan enemyWith){
        this.enemyOwner = enemyOwner;
        this.enemyWith = enemyWith;
        this.dominance = 0;
    }

    public Enemy(Clan enemyOwner, Clan enemyWith, int dominance){
        this.enemyOwner = enemyOwner;
        this.enemyWith = enemyWith;
        this.dominance = dominance;
    }

    public Clan getEnemyOwner() {
        return enemyOwner;
    }

    public Clan getEnemyWith() {
        return enemyWith;
    }

    public int getDominance() {
        return dominance;
    }

    public void addDominance(){
        this.dominance++;
    }

    public void removeDominance(){
        this.dominance--;
    }

    public void setDominance(int dominance) {
        this.dominance = dominance;
    }
}
