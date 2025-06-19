package me.marco.Utility.Cooldowns;

import me.marco.Utility.UtilTime;

public class Cooldown {

    private String ability;

    private double time;
    private long systime;
    private boolean removeOnDeath;
    private boolean inform;

    public Cooldown(String ability, double time, long systime, boolean removeOnDeath, boolean inform){
        this.ability = ability;
        this.time = time * 1000;
        this.systime = systime;
        this.removeOnDeath = removeOnDeath;
        this.inform = inform;
    }

    public String getAbility() {
        return ability;
    }

    public double getTime() {
        return time;
    }

    public long getSystime() {
        return systime;
    }

    public boolean isRemoveOnDeath() {
        return removeOnDeath;
    }

    public boolean isInform() {
        return inform;
    }

    public double getRemaining() {
        return UtilTime.convert((getTime() + getSystime()) - System.currentTimeMillis(), UtilTime.TimeUnit.SECONDS, 1);
    }

    public void setTime(double newTime){
        this.time = newTime;
    }

}
