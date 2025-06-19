package me.marco.Skills.Builders;

import me.marco.Skills.Data.ISkills.WeaponTypes.*;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ClassBuild {

    private eClassType classType;

    private BuildSkill swordSkill;
    private BuildSkill axeSkill;
    private BuildSkill bowSkill;

    private BuildSkill passiveA;
    private BuildSkill passiveB;
    private BuildSkill globalPassive;

    private boolean isActiveBuild;

    private final int maxPoints = 10;

    public ClassBuild(eClassType eClassType){
        this.classType = eClassType;
    }

    public ClassBuild(eClassType classType, BuildSkill swordSkill, BuildSkill axeSkill,
                      BuildSkill bowSkill, BuildSkill passiveA, BuildSkill passiveB,
                      BuildSkill globalPassive, boolean isActiveBuild){
        this.classType = classType;
        this.swordSkill = swordSkill;
        this.axeSkill = axeSkill;
        this.bowSkill = bowSkill;
        this.passiveA = passiveA;
        this.passiveB = passiveB;
        this.globalPassive = globalPassive;
        this.isActiveBuild = isActiveBuild;
    }

    public boolean canLevelMore(){
        return getTotalPointsUsed() < maxPoints;
    }

    public int getSkillLevel(Skill skill){
        if(!this.hasSkill(skill)) return 0;
        return this.getBuildSkill(skill).getLevel();
    }

    public int getPointsRemaining(){
        return maxPoints - getTotalPointsUsed();
    }

    public int getTotalPointsUsed(){
        int used = 0;
        used += swordSkill != null ? swordSkill.getLevel() : 0;
        used += axeSkill != null ? axeSkill.getLevel() : 0;
        used += bowSkill != null ? bowSkill.getLevel() : 0;
        used += passiveA != null ? passiveA.getLevel() : 0;
        used += passiveB != null ? passiveB.getLevel() : 0;
        used += globalPassive != null ? globalPassive.getLevel() : 0;
        return used;
    }

    public eClassType getClassType(){
        return this.classType;
    }

    public BuildSkill getSwordSkill(){
        return this.swordSkill;
    }

    public void setSwordSkill(BuildSkill swordSkill){
        this.swordSkill = swordSkill;
    }

    public boolean hasSwordSkill(){
        return this.swordSkill != null;
    }

    public BuildSkill getAxeSkill(){
        return this.axeSkill;
    }

    public void setAxeSkill(BuildSkill axeSkill){
        this.axeSkill = axeSkill;
    }

    public boolean hasAxeSkill(){
        return this.axeSkill != null;
    }

    public BuildSkill getBowSkill(){
        return this.bowSkill;
    }

    public void setBowSkill(BuildSkill bowSkill){
        this.bowSkill = bowSkill;
    }

    public boolean hasBowSkill(){
        return this.bowSkill != null;
    }

    public BuildSkill getPassiveA(){
        return this.passiveA;
    }

    public void setPassiveA(BuildSkill passiveA){
        this.passiveA = passiveA;
    }

    public boolean hasPassiveA(){
        return this.passiveA != null;
    }

    public BuildSkill getPassiveB(){
        return this.passiveB;
    }

    public void setPassiveB(BuildSkill passiveB){
        this.passiveB = passiveB;
    }

    public boolean hasPassiveB(){
        return this.passiveB != null;
    }

    public BuildSkill getGlobalPassive(){
        return this.globalPassive;
    }

    public void setGlobalPassive(BuildSkill globalPassive){
        this.globalPassive = globalPassive;
    }

    public boolean hasGlobalPassive(){
        return this.globalPassive != null;
    }

    public void setActiveBuild(boolean isActiveBuild){
        this.isActiveBuild = isActiveBuild;
    }

    public boolean isActiveBuild() { return this.isActiveBuild; }

    public boolean hasSkill(Skill skill){
        BuildSkill buildSkill = this.getBuildSkill(skill);
        return buildSkill != null && buildSkill.getSkill() == skill;
    }

    public BuildSkill getBuildSkill(Skill skill){
        if(skill instanceof SwordSkill){
            return this.getSwordSkill();
        }
        if(skill instanceof AxeSkill){
            return this.getAxeSkill();
        }
        if(skill instanceof BowSkill){
            return this.bowSkill;
        }
        if(skill instanceof PassiveA){
            return this.passiveA;
        }
        if(skill instanceof PassiveB){
            return this.passiveB;
        }
        if(skill instanceof GlobalPassive){
            return this.globalPassive;
        }
        return null;
    }

    public void sendSummary(Player player){
        player.sendMessage(getClassType().getColour() + ChatColor.BOLD.toString() + getClassType().getName());
        if(this.hasSwordSkill()) player.sendMessage(ChatColor.GREEN + "Sword Skill: " +
                ChatColor.YELLOW + this.swordSkill.getSkill().getName());
        if(this.hasAxeSkill()) player.sendMessage(ChatColor.GREEN + "Axe Skill: " +
                ChatColor.YELLOW + this.axeSkill.getSkill().getName());
        if(this.hasBowSkill()) player.sendMessage(ChatColor.GREEN + "Bow Skill: " +
                ChatColor.YELLOW + this.bowSkill.getSkill().getName());
        if(this.hasPassiveA()) player.sendMessage(ChatColor.GREEN + "Passive A: " +
                ChatColor.YELLOW + this.passiveA.getSkill().getName());
        if(this.hasPassiveB()) player.sendMessage(ChatColor.GREEN + "Passive B: " +
                ChatColor.YELLOW + this.passiveB.getSkill().getName());
        if(this.hasGlobalPassive()) player.sendMessage(ChatColor.GREEN + "Global Passive: " +
                ChatColor.YELLOW + this.globalPassive.getSkill().getName());
        player.playSound(player.getLocation(),  Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
    }

    public void removeSkill(Skill skill) {
        if(skill instanceof SwordSkill){
            this.swordSkill = null;
        }
        if(skill instanceof AxeSkill){
            this.axeSkill = null;
        }
        if(skill instanceof BowSkill){
            this.bowSkill = null;
        }
        if(skill instanceof PassiveA){
            this.passiveA = null;
        }
        if(skill instanceof PassiveB){
            this.passiveB = null;
        }
        if(skill instanceof GlobalPassive){
            this.globalPassive = null;
        }
    }

    public void addSkill(BuildSkill buildSkill) {
        Skill skill = buildSkill.getSkill();
        if(skill instanceof SwordSkill){
            this.swordSkill = buildSkill;
        }
        if(skill instanceof AxeSkill){
            this.axeSkill = buildSkill;
        }
        if(skill instanceof BowSkill){
            this.bowSkill = buildSkill;
        }
        if(skill instanceof PassiveA){
            this.passiveA = buildSkill;
        }
        if(skill instanceof PassiveB){
            this.passiveB = buildSkill;
        }
        if(skill instanceof GlobalPassive){
            this.globalPassive = buildSkill;
        }
    }
}
