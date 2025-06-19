package me.marco.WorldEvent;

import me.marco.Base.Core;
import me.marco.WorldEvent.Bosses.WorldBoss;

public abstract class BossSkill {

    private String name;

    public BossSkill(String name){
        this.name = name;
    }

    public abstract void cast(WorldBoss worldBoss, Core instance, int amplifier);

    public String getName() {
        return name;
    }
}
