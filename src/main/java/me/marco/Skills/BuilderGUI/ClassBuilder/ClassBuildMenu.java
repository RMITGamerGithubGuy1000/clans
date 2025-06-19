package me.marco.Skills.BuilderGUI.ClassBuilder;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.GUI.Button;
import me.marco.Quests.GUI.DynamicMenu;
import me.marco.Skills.BuilderGUI.BackButton;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.WeaponTypes.*;
import me.marco.Skills.Data.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassBuildMenu extends DynamicMenu {

    private eClassType classType;

    private final int cherriesPos = 4;
    private final int swordPos = 9;
    private final int axePos = 18;
    private final int bowPos = 27;
    private final int passiveAPos = 36;
    private final int passiveBPos = 45;

    public ClassBuildMenu(Core core, eClassType classType) {
        super(core, classType.getColour() + ChatColor.BOLD.toString() + classType.getName() + ChatColor.YELLOW + " Class Builder", 54);
        this.classType = classType;
    }

    @Override
    public Inventory generateUniqueMenu(Player player) {
        //    public ClassBuildButton(Skill skill, ClassBuild classBuild, int level, String displayName, Core core, int slot, Material material) {
        Client client = getInstance().getClientManager().getClient(player);
        ClassBuild classBuild = client.getBuildsContainer().getClassBuilds(this.classType);

        BackButton backButton = new BackButton(getInstance().getMenuManager().getMenu("Class Builder"), getInstance(), 0);

        //    public Button(Core core, int slot, String name, List<String> description, Material material)
        ClassBuildButton cherries = new ClassBuildButton(getInstance(), cherriesPos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Skill Points",
                Arrays.asList(ChatColor.YELLOW + "Remaining: " + ChatColor.GREEN + classBuild.getPointsRemaining() + ""), Material.GLOW_BERRIES);

        ClassBuildButton sword = new ClassBuildButton(getInstance(), swordPos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Sword Skill",
                Arrays.asList(ChatColor.YELLOW + "Select a sword skill"), Material.IRON_SWORD);

        ClassBuildButton axe = new ClassBuildButton(getInstance(), axePos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Axe Skill",
                Arrays.asList(ChatColor.YELLOW + "Select an axe skill"), Material.IRON_AXE);

        ClassBuildButton bow = new ClassBuildButton(getInstance(), bowPos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Bow Skill",
                Arrays.asList(ChatColor.YELLOW + "Select a bow skill"), Material.BOW);

        ClassBuildButton classPassiveA  = new ClassBuildButton(getInstance(), passiveAPos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Passive A",
                Arrays.asList(ChatColor.YELLOW + "Select an A Passive"), Material.RED_DYE);

        ClassBuildButton classPassiveB  = new ClassBuildButton(getInstance(), passiveBPos,
                ChatColor.GOLD + ChatColor.BOLD.toString() + "Passive B",
                Arrays.asList(ChatColor.YELLOW + "Select a B Passive"), Material.LIME_DYE);

        List<Skill> classSkills = getInstance().getSkillManager().getSkillsOfClassType(this.classType);

        List<Button> uniqueButtons = new ArrayList<Button>();

        int swordCounter = 0;
        int axeCounter = 0;
        int bowCounter = 0;
        int passiveACounter = 0;
        int passiveBCounter = 0;

        // public ClassBuildSkillButton(Skill skill, ClassBuild classBuild, int level, String displayName, Core core, int slot, Material material) {
        for(Skill skill : classSkills){
            String displayName = "";
            int pos = 1;
            if(skill instanceof SwordSkill) {
                swordCounter++;
                pos = swordPos + swordCounter;
            }else if(skill instanceof AxeSkill){
                axeCounter++;
                pos = axePos + axeCounter;
            }else if(skill instanceof BowSkill){
                bowCounter++;
                pos = bowPos + bowCounter;
            }else if(skill instanceof PassiveA){
                passiveACounter++;
                pos = passiveAPos + passiveACounter;
            }else if(skill instanceof PassiveB){
                passiveBCounter++;
                pos = passiveBPos + passiveBCounter;
            }
            ChatColor prefix = classBuild.hasSkill(skill) ? ChatColor.GREEN : ChatColor.RED;
            displayName = prefix + ChatColor.BOLD.toString() + skill.getName();
            int level = classBuild.getSkillLevel(skill);
            if(level == 0) level = 1;
            uniqueButtons.add(new ClassBuildSkillButton(skill, classBuild,
                    level, displayName, getInstance(), pos, Material.ENCHANTED_BOOK));
        }

        cherries.getItem().setAmount(remainingNotZero(classBuild.getPointsRemaining()));

        uniqueButtons.add(backButton);
        uniqueButtons.add(cherries);
        uniqueButtons.add(sword);
        uniqueButtons.add(axe);
        uniqueButtons.add(bow);
        uniqueButtons.add(classPassiveA);
        uniqueButtons.add(classPassiveB);

        return generateInventory(client, uniqueButtons);
    }

    public int remainingNotZero(int val){
        if (val == 0) return 1;
        return val;
    }

}
