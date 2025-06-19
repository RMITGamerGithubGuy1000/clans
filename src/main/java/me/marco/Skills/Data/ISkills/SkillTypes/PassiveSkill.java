package me.marco.Skills.Data.ISkills.SkillTypes;

import org.bukkit.entity.Player;

public interface PassiveSkill {

    public default void onEquip(Player player){

    }

    public default void onDequip(Player player){

    }

}
