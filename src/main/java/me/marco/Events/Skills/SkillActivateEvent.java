package me.marco.Events.Skills;

import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillActivateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player activator;
    private final BuildSkill buildSkill;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public SkillActivateEvent(Player activator, BuildSkill buildSkill) {
        this.activator = activator;
        this.buildSkill = buildSkill;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getActivator() {
        return activator;
    }

    public BuildSkill getBuildSkill() {
        return buildSkill;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
