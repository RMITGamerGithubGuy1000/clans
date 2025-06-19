package me.marco.Skills.Skills.Guardian.PassiveA;

public class RamData {

    private int ticksPassed;
    private int level;

    public RamData(){
        this.ticksPassed = 0;
        this.level = 0;
    }

    public int increment(int speedInSeconds){
        this.ticksPassed++;
        if (this.ticksPassed % (speedInSeconds * 20) == 0) {
            if(this.level < 3) this.level++;
        }
        return level;
    }

    public int getTicksPassed() {
        return ticksPassed;
    }

    public void setTicksPassed(int ticksPassed) {
        this.ticksPassed = ticksPassed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float soundPitch(){
        if(this.level == 1) return .5f;
        if(this.level == 2) return .7f;
        return 1f;
    }

}
