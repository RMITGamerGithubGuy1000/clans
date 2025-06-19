package me.marco.Events;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.Skills.*;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.ISkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SkillListener extends CListener<Core> {

    public SkillListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onAbilityActivate(SkillActivateEvent event){
        Player player = event.getActivator();
        BuildSkill buildSkill = event.getBuildSkill();
        if(!buildSkill.getSkill().isSafezoneSafe() &&
                !getInstance().getClanManager().canCastInSafeZone(player, buildSkill.getSkill().getName())){
            event.setCancelled(true);
            return;
        }
        Skill skill = buildSkill.getSkill();
        if(skill instanceof ChannelSkill){
            ChannelSkill channelSkill = (ChannelSkill) skill;
            if(this.getInstance().getCooldownManager().isCooling(player, skill.getName())){
                this.getInstance().getCooldownManager().sendRemaining(player, skill.getName());
                return;
            }
            if(!channelSkill.checkChannelingForToggle(player)) if(!channelSkill.useageCheck(player)){
                event.setCancelled(true);
                return;
            }
            if(channelSkill.isChanneling(player)){
                channelSkill.toggleChannel(player);
                event.setCancelled(true);
                return;
            }
            buildSkill.activateInteractSkill(player);
            return;
        }
        if(!skill.useageCheck(player)){
            event.setCancelled(true);
            return;
        }
        int level = skill.getLevel(player);
        if(!this.getInstance().getCooldownManager().add(player, skill, skill.getClassTypeName(), level, skill.getCooldown(level), skill.isInform())){
            event.setCancelled(true);
            return;
        }
        buildSkill.activateInteractSkill(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!client.hasActiveBuild()) return;
        ClassBuild classBuild = client.getActiveBuild();
        Action action = event.getAction();
        if (Arrays.stream(ISkill.getSwords).anyMatch(
                material -> getItemInHand(player, EquipmentSlot.HAND).getType() == material)) {
            if (!classBuild.hasSwordSkill()) return;
            BuildSkill bs = classBuild.getSwordSkill();
            if(!bs.eventIsAction(event.getAction())) return;
            getInstance().getServer().getPluginManager().callEvent(new SkillActivateEvent(player, bs));
            return;
        }
        if (Arrays.stream(ISkill.getAxes).anyMatch(
                material -> getItemInHand(player, EquipmentSlot.HAND).getType() == material)) {
            if (!classBuild.hasAxeSkill()) return;
            BuildSkill bs = classBuild.getAxeSkill();
            if(!bs.eventIsAction(event.getAction())) return;
            getInstance().getServer().getPluginManager().callEvent(new SkillActivateEvent(player, bs));
            return;
        }
        if (Arrays.stream(ISkill.getBow).anyMatch(
                material -> getItemInHand(player, EquipmentSlot.HAND).getType() == material)) {
            if (!classBuild.hasBowSkill()) return;
            BuildSkill bs = classBuild.getBowSkill();
            if(!bs.eventIsAction(event.getAction())) return;
            getInstance().getServer().getPluginManager().callEvent(new SkillActivateEvent(player, bs));
            return;
        }
    }

    @EventHandler
    public void onClassCheck(ClassCheckEvent event) {
        for (Player player : getInstance().getServer().getOnlinePlayers()) {
            eClassType classType = eClassType.getWearing(player);
            Client client = getInstance().getClientManager().getClient(player);
            if (classType != null) {
                if (client.getActiveBuild() != null && client.getActiveBuild().getClassType() == classType) continue;
                getInstance().getServer().getPluginManager().callEvent(new ClassEquipEvent(player, classType));
                continue;
            }
            if (client.getActiveBuild() != null) {
                getInstance().getServer().getPluginManager().callEvent(new ClassDequipEvent(player));
            }
        }
    }

    @EventHandler
    public void onEquip(ClassPassiveAddEvent event){
        Player player = event.getEquipping();
        Client client = getInstance().getClientManager().getClient(player);
    }

    @EventHandler
    public void onClassEquip(ClassEquipEvent event) {
        Player player = event.getEquipping();
        Client client = getInstance().getClientManager().getClient(player);
        eClassType classType = event.getClassType();
        ClassBuild classBuild = client.getActiveClassBuild(classType);
        client.setActiveBuild(classBuild);
        classBuild.sendSummary(player);
        client.getScoreboard().updateClass(classType);
        getInstance().getServer().getPluginManager().callEvent(new ClassPassiveAddEvent(player, classType));
        if(classBuild.getSwordSkill() != null &&
                classBuild.getSwordSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getSwordSkill().getSkill()).onEquip(player);
        if(classBuild.getAxeSkill() != null &&
                classBuild.getAxeSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getAxeSkill().getSkill()).onEquip(player);
        if(classBuild.getBowSkill() != null &&
                classBuild.getBowSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getBowSkill().getSkill()).onEquip(player);
        if(classBuild.getPassiveA() != null &&
                classBuild.getPassiveA().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getPassiveA().getSkill()).onEquip(player);
        if(classBuild.getPassiveB() != null &&
                classBuild.getPassiveB().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getPassiveB().getSkill()).onEquip(player);
    }

    @EventHandler
    public void onClassDequip(ClassDequipEvent event) {
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        ClassBuild classBuild = client.getActiveBuild();
        if(classBuild.getSwordSkill() != null &&
                classBuild.getSwordSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getSwordSkill().getSkill()).onDequip(player);
        if(classBuild.getAxeSkill() != null &&
                classBuild.getAxeSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getAxeSkill().getSkill()).onDequip(player);
        if(classBuild.getBowSkill() != null &&
                classBuild.getBowSkill().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getBowSkill().getSkill()).onDequip(player);
        if(classBuild.getPassiveA() != null &&
                classBuild.getPassiveA().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getPassiveA().getSkill()).onDequip(player);
        if(classBuild.getPassiveB() != null &&
                classBuild.getPassiveB().getSkill() instanceof PassiveSkill) ((PassiveSkill) classBuild.getPassiveB().getSkill()).onDequip(player);
        client.setActiveBuild(null);
        client.getScoreboard().updateClass(null);
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Dequipped");
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 2, 1);
    }

    public ItemStack getItemInHand(Player player, EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.HAND) {
            return player.getInventory().getItemInMainHand();
        }
        if (equipmentSlot == EquipmentSlot.OFF_HAND) {
            return player.getInventory().getItemInOffHand();
        }
        return null;
    }

    public boolean isArmour(ItemStack itemStack) {
        Material mat = itemStack.getType();
        return mat == Material.NETHERITE_HELMET || mat == Material.NETHERITE_CHESTPLATE ||
                mat == Material.NETHERITE_LEGGINGS || mat == Material.NETHERITE_BOOTS ||
                mat == Material.DIAMOND_HELMET || mat == Material.DIAMOND_CHESTPLATE ||
                mat == Material.DIAMOND_LEGGINGS || mat == Material.DIAMOND_BOOTS ||
                mat == Material.CHAINMAIL_HELMET || mat == Material.CHAINMAIL_CHESTPLATE ||
                mat == Material.CHAINMAIL_LEGGINGS || mat == Material.CHAINMAIL_BOOTS ||
                mat == Material.IRON_HELMET || mat == Material.IRON_HELMET ||
                mat == Material.IRON_HELMET || mat == Material.IRON_HELMET ||
                mat == Material.GOLDEN_HELMET || mat == Material.GOLDEN_HELMET ||
                mat == Material.GOLDEN_HELMET || mat == Material.GOLDEN_HELMET ||
                mat == Material.LEATHER_HELMET || mat == Material.LEATHER_HELMET ||
                mat == Material.LEATHER_HELMET || mat == Material.LEATHER_HELMET;
    }

}
