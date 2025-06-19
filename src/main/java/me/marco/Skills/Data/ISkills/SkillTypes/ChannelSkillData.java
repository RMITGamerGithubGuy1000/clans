package me.marco.Skills.Data.ISkills.SkillTypes;

import me.marco.Client.Client;
import me.marco.Skills.Builders.ClassBuild;
import org.bukkit.entity.Player;

public class ChannelSkillData {

    private Player caster;
    private int castItemSlot;
    private ChannelSkill toCast;
    private int ticksRequired;
    private int ticksAmassed = 0;
    private int level;
    private boolean isOff = false;
    private boolean isPassive = false;

    public ChannelSkillData(Player caster, int castItemSlot, ChannelSkill toCast, int level, int ticksRequired){
        this.caster = caster;
        this.castItemSlot = castItemSlot;
        this.toCast = toCast;
        this.ticksRequired = ticksRequired;
        this.level = level;
    }

    public ChannelSkillData(Player caster, ChannelSkill toCast, int level, int ticksRequired){
        this.caster = caster;
        this.toCast = toCast;
        this.ticksRequired = ticksRequired;
        this.level = level;
        this.isPassive = true;
    }

    public boolean handleTick(Client casterClient){
        ChannelSkill channelSkill = getToCast();
        ClassBuild classBuild = casterClient.getActiveBuild();
        if(classBuild == null){
            return false;
        }
        int level = classBuild.getBuildSkill(channelSkill).getLevel();
        if(!isPassive && (!casterClient.isChanneling() || casterClient.getIsChanneling() != channelSkill)) return false;
        Player caster = getCaster();
        if(!isPassive && channelSkill.needsHoldItem() && caster.getInventory().getHeldItemSlot() != getCastItemSlot()){
            return false;
        }
        drainEnergy(caster.getPlayer(), channelSkill.requiredEnergy(level));
        if(!isPassive && !channelSkill.canCastChannel(getCaster().getPlayer(), false, true, level)){
            return false;
        }
        this.ticksAmassed++;
        if(this.ticksAmassed < this.ticksRequired) return true;
        this.ticksAmassed = 0;
        return channelSkill.runTick(getCaster());
    }

    public void drainEnergy(Player player, float amount){
        float toSet = player.getExp() - amount;
        if(toSet < 0) toSet = .01f;
        player.setExp(toSet);
    }

    public int getCastItemSlot() {
        return castItemSlot;
    }

    public Player getCaster() {
        return caster;
    }

    public void setCaster(Player caster) {
        this.caster = caster;
    }

    public ChannelSkill getToCast() {
        return toCast;
    }

    public void setToCast(ChannelSkill toCast) {
        this.toCast = toCast;
    }

    public int getTicksRequired() {
        return ticksRequired;
    }

    public void setTicksRequired(int ticksRequired) {
        this.ticksRequired = ticksRequired;
    }

    public int getTicksAmassed() {
        return ticksAmassed;
    }

    public void setTicksAmassed(int ticksAmassed) {
        this.ticksAmassed = ticksAmassed;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    public boolean isPassive() {
        return isPassive;
    }
}
