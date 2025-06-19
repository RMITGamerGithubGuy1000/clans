package me.marco.Skills.Builders;

import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.Arrays;

public class BuildSkill {

    private Skill skill;
    private int level;

    public BuildSkill(Skill skill, int level){
        this.skill = skill;
        this.level = level;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void activateInteractSkill(Player player){
        if(!(this.skill instanceof InteractSkill)) return;
        InteractSkill interactSkill = (InteractSkill) this.skill;
        interactSkill.activate(player, skill.getLevel(player));
    }

    public boolean eventIsAction(Action action){
        return Arrays.stream(this.skill.getActions()).anyMatch(skillActions -> skillActions == action);
    }

}
