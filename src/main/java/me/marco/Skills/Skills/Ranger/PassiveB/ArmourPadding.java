package me.marco.Skills.Skills.Ranger.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

public class ArmourPadding extends Skill implements PassiveSkill, PassiveB {

    public ArmourPadding(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return false;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Gain " + ChatColor.GREEN + this.formatNumber(calcReduction(level), 1) + ChatColor.LIGHT_PURPLE + " Physical Damage " + ChatColor.YELLOW + "reduction. "
        };
    }

    @Override
    public double getCooldown(int level) {
        return 0;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    public double calcReduction(int level){
        return .1 * level;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        if(event.isCancelled()) return;
        Player player = event.getTarget();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        double reduction = calcReduction(client.getActiveBuild().getSkillLevel(this));
        double eDmg = event.getDamage();
        event.setDamage(eDmg - (eDmg * reduction));
    }

}
