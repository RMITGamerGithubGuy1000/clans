package me.marco.Cosmetics.Objects.CosmeticTypes;

import me.marco.Cosmetics.Objects.Cosmetic;
import org.bukkit.Material;

public abstract class KillStreak  extends Cosmetic {

    private int killStreak;

    public KillStreak(String cosmeticTag, Material representation) {
        super(cosmeticTag, representation);
        this.killStreak = 0;
    }

    public abstract void playEffect();

    public void addKill(){
        this.killStreak++;
    }

    public void resetKillStreak(){
        this.killStreak = 0;
    }

}
