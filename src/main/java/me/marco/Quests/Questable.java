package me.marco.Quests;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import org.bukkit.Material;

public abstract class Questable extends CListener<Core> {

    private String name;
    private Material icon;

    public Questable(String name, Material icon, Core instance) {
        super(instance);
        this.name = name;
        this.icon = icon;
    }

    public int getRequired(QuestWrapper wrapper){
        return wrapper.getQuestDifficulty().getDifficultyMultiplier() * getBaseProgressRequired();
    }

    public abstract int getBaseProgressRequired();

    public String getName() {
        return name;
    }

    public abstract String[] getDescription(QuestWrapper questWrapper);

    public Material getIcon() {
        return icon;
    }
}
