package me.marco.Skills.Data.ISkills.SkillTypes;

import org.bukkit.entity.Player;

public interface ToggleSkill {

    void activateToggle(Player player, int level);

    float getRequiredEnergy(int level);

    boolean hasEnergy(Player player);

}
