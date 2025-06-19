package me.marco.Skills.Skills.Samurai.PassiveB;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.NMSFox;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveB;
import me.marco.Skills.Data.Skill;
import me.marco.Tags.PvPTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Firefox extends Skill implements PassiveSkill, PassiveB {

    public Firefox(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "After hitting an enemy " + ChatColor.GREEN + "2" + ChatColor.YELLOW + " times in combat, summon ",
                ChatColor.YELLOW + "a " + ChatColor.AQUA + "Firefox " + ChatColor.YELLOW + "that pounces them and sets them on fire. ",
                ChatColor.YELLOW + "as well as temporarily giving them " + ChatColor.AQUA + "Slow I" + ChatColor.YELLOW + ". ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 30 - 4 * level;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPhysicalDamage(PhysicalDamageEvent event){
        Player damager = event.getDamager();
        Client damagerClient = getInstance().getClientManager().getClient(damager);
        if(event.isCancelled()) return;
        Player target = event.getTarget();
        if(!damagerClient.hasSkill(this)) return;
        Client targetClient = getInstance().getClientManager().getClient(target);
        if(!targetClient.hasPvPTag()) return;
        PvPTag pvpTag = targetClient.getPvPTag();
        if(pvpTag.getDamager().getUniqueId() != damager.getUniqueId()) return;
        if(getInstance().getCooldownManager().isCooling(damager, getName())) return;
        castFirefox(damager, target);
    }

    public void castFirefox(Player damager, Player target){
        NMSFox nmsFox = new NMSFox(getInstance(), damager, damager.getLocation(), target);
        Fox fox = (Fox) nmsFox.getBukkitEntity();
        fox.setCustomNameVisible(true);
        fox.setCustomName(ChatColor.AQUA + damager.getName() + " Firefox");
        fox.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 1));
        fox.setFireTicks(100);
        int level = getLevel(damager);
        getInstance().getCooldownManager().add(damager, getName(), getClassTypeName(), level, getCooldown(level), true);
    }

}
