package me.marco.Skills.BuilderGUI.ClassBuilder;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.Skills.ClassPassiveAddEvent;
import me.marco.GUI.Button;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ClassBuildSkillButton extends Button {

    private Skill skill;
    private ClassBuild classBuild;

    public ClassBuildSkillButton(Skill skill, ClassBuild classBuild, int level, String displayName, Core core, int slot, Material material) {
        super(core, slot, displayName, Arrays.asList(skill.getAppendedDescription(level)), material, 64);
        this.skill = skill;
        this.classBuild = classBuild;
        this.getItem().setAmount(getItemStackAmount());
    }

    private int getItemStackAmount(){
        return classBuild.getSkillLevel(skill) > 0 ? classBuild.getSkillLevel(skill) : 1;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        if(this.skill == null) return;
        if(clickType == ClickType.LEFT){
            if(this.getCurrentSkillOfType() == this.skill){
                addLevelToBuildSkill(player, clickedItem);
            }else if(this.getCurrentSkillOfType() == null){
                this.equipSkill(player, clickedItem);
            }else{
                getInstance().getChat().sendModule(player, "You need to un-level your other skill of this type", "Class Builder");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 5);
            }
        }
        if(clickType == ClickType.RIGHT){
            if(this.getCurrentSkillOfType() == this.skill){
                removeLevelFromBuildSkill(player, clickedItem);
                clickedItem.setAmount(getItemStackAmount());
            }
        }
    }

    private Skill getCurrentSkillOfType(){
        BuildSkill buildSkill = this.classBuild.getBuildSkill(this.skill);
        if(buildSkill == null) return null;
        return this.classBuild.getBuildSkill(this.skill).getSkill();
    }

    private void removeLevelFromBuildSkill(Player player, ItemStack clickedItem){
        if(classBuild.hasSkill(this.skill)){
            BuildSkill buildSkill = classBuild.getBuildSkill(skill);
            buildSkill.setLevel(buildSkill.getLevel() - 1);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 4, 4);

            ItemStack cherries = player.getOpenInventory().getItem(4).clone();
            ItemMeta im = cherries.getItemMeta();
            im.setLore(Arrays.asList(ChatColor.YELLOW + "Remaining: " + ChatColor.GREEN + classBuild.getPointsRemaining()));
            cherries.setItemMeta(im);
            cherries.setAmount(getRemainingPointsStack());
            player.getOpenInventory().setItem(4, cherries);
            ItemStack skillItem = player.getOpenInventory().getItem(this.getSlot()).clone();
            ItemMeta skillMeta = skillItem.getItemMeta();
            if(buildSkill.getLevel() > 0) skillMeta.setLore(Arrays.asList(buildSkill.getSkill().getAppendedDescription(buildSkill.getLevel())));
            if(buildSkill.getLevel() <= 0){
                if(skill instanceof PassiveSkill){
                    ((PassiveSkill) skill).onDequip(player);
                }
                classBuild.removeSkill(skill);
                skillMeta.setLore(Arrays.asList(buildSkill.getSkill().getAppendedDescription(1)));
                skillMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + skill.getName());
            }
            skillItem.setItemMeta(skillMeta);
            skillItem.setAmount(getItemStackAmount());
            this.setItem(skillItem);
            player.getOpenInventory().setItem(this.getSlot(), this.getItem());

            Client client = getInstance().getClientManager().getClient(player);
            getInstance().getSqlRepoManager().getClassBuildRepo().updateClass(this.classBuild, client);
        }
    }

    private void addLevelToBuildSkill(Player player, ItemStack clickedItem){
        if(!classBuild.canLevelMore()){
            getInstance().getChat().sendModule(player, "You do not have any more " +
                    getInstance().getChat().highlightText + "skill points" + getInstance().getChat().textColour + " to use.", "Class Builder");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 5);
            return;
        }
        if(classBuild.getSkillLevel(skill) + 1 > skill.getMaxLevel()){
            getInstance().getChat().sendModule(player, getInstance().getChat().highlightText + "Max level" + getInstance().getChat().textColour + " reached", "Class Builder");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 5);
            return;
        }

        BuildSkill buildSkill = classBuild.getBuildSkill(skill);
        buildSkill.setLevel(buildSkill.getLevel() + 1);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 4);

        ItemStack cherries = player.getOpenInventory().getItem(4).clone();
        ItemMeta im = cherries.getItemMeta();
        im.setLore(Arrays.asList(ChatColor.YELLOW + "Remaining: " + ChatColor.GREEN + classBuild.getPointsRemaining()));
        cherries.setItemMeta(im);
        cherries.setAmount(getRemainingPointsStack());
        player.getOpenInventory().setItem(4, cherries);

        ItemStack skillItem = player.getOpenInventory().getItem(this.getSlot()).clone();
        ItemMeta skillMeta = skillItem.getItemMeta();
        skillMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + skill.getName());
        skillMeta.setLore(Arrays.asList(buildSkill.getSkill().getAppendedDescription(buildSkill.getLevel())));
        skillItem.setItemMeta(skillMeta);
        skillItem.setAmount(getItemStackAmount());
        this.setItem(skillItem);
        player.getOpenInventory().setItem(this.getSlot(), this.getItem());

        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getSqlRepoManager().getClassBuildRepo().updateClass(this.classBuild, client);
    }

    private void equipSkill(Player player, ItemStack clickedItem){

        BuildSkill buildSkill = new BuildSkill(skill, 1);
        classBuild.addSkill(buildSkill);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 5);

        ItemStack cherries = player.getOpenInventory().getItem(4).clone();
        ItemMeta im = cherries.getItemMeta();
        im.setLore(Arrays.asList(ChatColor.YELLOW + "Remaining: " + ChatColor.GREEN + classBuild.getPointsRemaining()));
        cherries.setItemMeta(im);
        cherries.setAmount(getRemainingPointsStack());
        player.getOpenInventory().setItem(4, cherries);

        ItemStack skillItem = player.getOpenInventory().getItem(this.getSlot()).clone();
        ItemMeta skillMeta = skillItem.getItemMeta();
        skillMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + skill.getName());
        skillMeta.setLore(Arrays.asList(buildSkill.getSkill().getAppendedDescription(buildSkill.getLevel())));
        skillItem.setItemMeta(skillMeta);
        this.setItem(skillItem);

        player.getOpenInventory().setItem(this.getSlot(), this.getItem());

        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getSqlRepoManager().getClassBuildRepo().updateClass(this.classBuild, client);
        if(skill instanceof PassiveSkill){
            ((PassiveSkill) skill).onEquip(player);
        }

    }

    private int getRemainingPointsStack(){
        return classBuild.getPointsRemaining() > 0 && classBuild.getPointsRemaining() != 1 ? classBuild.getPointsRemaining() : 1;
    }


//    private int getLevelOfSkill(ClassBuild classBuild, Skill skill){
//        return classBuild.getBuildSkill(skill).equals(skill) ? classBuild.getBuildSkill(skill).getLevel() : 1;
//    }
}
