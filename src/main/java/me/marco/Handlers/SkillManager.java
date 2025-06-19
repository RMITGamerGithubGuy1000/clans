package me.marco.Handlers;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Skills.BuilderGUI.ArmourViewer.ArmourViewerMenu;
import me.marco.Skills.BuilderGUI.ClassBuilder.ClassBuildMenu;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.BuildsContainer;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.ISkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkillData;
import me.marco.Skills.Data.Skill;
import me.marco.Skills.Skills.Guardian.Axe.Barrier;
import me.marco.Skills.Skills.Guardian.Axe.SeismicSlam;
import me.marco.Skills.Skills.Guardian.PassiveA.Ram;
import me.marco.Skills.Skills.Guardian.PassiveB.GlacialAxe;
import me.marco.Skills.Skills.Guardian.Sword.BattleTaunt;
import me.marco.Skills.Skills.Mage.Axe.Avalanche;
import me.marco.Skills.Skills.Mage.Axe.Flamethrower;
import me.marco.Skills.Skills.Mage.PassiveA.InfernoReborn;
import me.marco.Skills.Skills.Mage.PassiveB.ElementalGolem;
import me.marco.Skills.Skills.Mage.PassiveA.FrostShield;
import me.marco.Skills.Skills.Mage.PassiveB.MagmaShield;
import me.marco.Skills.Skills.Mage.Sword.Plaguespreader;
import me.marco.Skills.Skills.Mage.Sword.WaterPrison;
import me.marco.Skills.Skills.Ranger.Axe.Ricochet;
import me.marco.Skills.Skills.Ranger.Axe.SmokeBomb;
import me.marco.Skills.Skills.Ranger.Bow.MagicArrows;
import me.marco.Skills.Skills.Ranger.PassiveA.HealingArrow;
import me.marco.Skills.Skills.Ranger.PassiveA.Repair;
import me.marco.Skills.Skills.Ranger.PassiveB.AntiVenom;
import me.marco.Skills.Skills.Ranger.PassiveB.ArmourPadding;
import me.marco.Skills.Skills.Ranger.Sword.ExecutionArrow;
import me.marco.Skills.Skills.Ranger.Sword.Hookshot;
import me.marco.Skills.Skills.Rogue.Axe.Blink;
import me.marco.Skills.Skills.Rogue.Axe.Leap;
import me.marco.Skills.Skills.Rogue.PassiveA.Swift;
import me.marco.Skills.Skills.Rogue.PassiveB.Backstab;
import me.marco.Skills.Skills.Rogue.PassiveB.Moxie;
import me.marco.Skills.Skills.Rogue.Sword.Recall;
import me.marco.Skills.Skills.Rogue.Sword.VenomStrike;
import me.marco.Skills.Skills.Samurai.Axe.Stampede;
import me.marco.Skills.Skills.Samurai.PassiveA.AngelsBlessing;
import me.marco.Skills.Skills.Samurai.PassiveA.HealingSong;
import me.marco.Skills.Skills.Samurai.PassiveB.Firefox;
import me.marco.Skills.Skills.Samurai.Sword.BeastCall;
import me.marco.Skills.Skills.Samurai.Sword.SageToads;
import me.marco.Skills.Skills.Samurai.Axe.Scout;
import me.marco.Skills.Skills.Warrior.Axe.BullsCharge;
import me.marco.Skills.Skills.Warrior.Axe.HoldPosition;
import me.marco.Skills.Skills.Warrior.PassiveA.Maim;
import me.marco.Skills.Skills.Warrior.PassiveA.Swordsmanship;
import me.marco.Skills.Skills.Warrior.PassiveB.Tempo;
import me.marco.Skills.Skills.Warrior.PassiveB.Tenacity;
import me.marco.Skills.Skills.Warrior.Sword.Aerostrike;
import me.marco.Skills.Skills.Warrior.Sword.Riposte;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {

    private Core core;

    public SkillManager(Core core){
        this.core = core;
    }

    private List<ChannelSkillData> channelSkillList = new ArrayList<ChannelSkillData>();
    private List<ChannelSkillData> toRemove = new ArrayList<ChannelSkillData>();
    private ArmourViewerMenu armourViewerMenu;
    private ClassBuildMenu leatherBuildMenu;
    private ClassBuildMenu chainBuildMenu;
    private ClassBuildMenu diamondBuildMenu;
    private ClassBuildMenu netherriteBuildMenu;
    private ClassBuildMenu goldBuildMenu;
    private ClassBuildMenu ironBuildMenu;

    public MagicArrows getMagicArrows(){
        return (MagicArrows) this.getSkill("Magic Arrows");
    }

    public void addChannelSkill(ChannelSkillData channelSkillData){
        this.channelSkillList.add(channelSkillData);
    }

    public void handleChannelSkills(){
        this.channelSkillList.forEach(channelSkillData -> {
            Player player = channelSkillData.getCaster();
            Client client = getInstance().getClientManager().getClient(player);
            if(!channelSkillData.handleTick(client)){
                client.setChanneling(null);
                ChannelSkill channelSkill = channelSkillData.getToCast();
                int level = channelSkillData.getLevel();
                if(!channelSkillData.isPassive()) getInstance().getCooldownManager().channelExpireAdd(channelSkillData);
                channelSkill.cleanup(player);
                toRemove.add(channelSkillData);
            }
        });
        this.toRemove.forEach(channelSkillData -> channelSkillList.remove(channelSkillData));
        toRemove.clear();
    }

//    public boolean isChanneling(Player player){
//        return findChannel(player) != null;
//    }

//    public ChannelSkillData findChannel(Player player){
//        return this.channelSkillList.stream().filter(channelSkillData -> channelSkillData.getCaster().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
//    }

    private List<Skill> skillList = new ArrayList<Skill>();

    public List<Skill> getSkills(){
        return this.skillList;
    }

    public List<Skill> getSkillsOfClassType(eClassType eClassType){
        List<Skill> foundSkillList = new ArrayList<Skill>();
        //        return this.allianceList.stream().filter(listAlliance -> listAlliance.getAllianceWith() == clan).findFirst().orElse(null);
        this.skillList.stream().filter(skill -> skill.getClassType() == eClassType).forEach(skill -> foundSkillList.add(skill));
        return foundSkillList;
    }

//    public void checkClasses(){
//        new BukkitRunnable(){
//            public void run(){
//
//            }
//        }.runTaskTimer(core, 0, 10);
//    }

    public void openArmourViewMenu(Player player){
        this.armourViewerMenu.openMenu(player);
    }

    public void registerSkills(){
//        String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract
        this.armourViewerMenu = new ArmourViewerMenu(getInstance());
        this.leatherBuildMenu = new ClassBuildMenu(getInstance(), eClassType.ROGUE);
        this.chainBuildMenu = new ClassBuildMenu(getInstance(), eClassType.RANGER);
        this.diamondBuildMenu = new ClassBuildMenu(getInstance(), eClassType.GUARDIAN);
        this.netherriteBuildMenu = new ClassBuildMenu(getInstance(), eClassType.SAMURAI);
        this.goldBuildMenu = new ClassBuildMenu(getInstance(), eClassType.MAGE);
        this.ironBuildMenu = new ClassBuildMenu(getInstance(), eClassType.WARRIOR);
        getInstance().getMenuManager().addMenu(this.armourViewerMenu);
        getInstance().getMenuManager().addMenu(this.leatherBuildMenu);
        getInstance().getMenuManager().addMenu(this.chainBuildMenu);
        getInstance().getMenuManager().addMenu(this.diamondBuildMenu);
        getInstance().getMenuManager().addMenu(this.netherriteBuildMenu);
        getInstance().getMenuManager().addMenu(this.goldBuildMenu);
        getInstance().getMenuManager().addMenu(this.ironBuildMenu);

        registerSwordSkills();
        registerAxeSkills();
        registerBowSkills();
        registerPassiveAs();
        registerPassiveBs();
    }

    private void registerSwordSkills(){
        addSkill(new Riposte("Riposte",
                core, eClassType.WARRIOR,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new Aerostrike("Aerostrike",
                core, eClassType.WARRIOR,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new VenomStrike("Venom Strike",
                core, eClassType.ROGUE,
                ISkill.getSwords, ISkill.rightClick,
                3, true, false));

        addSkill(new Recall("Recall",
                core, eClassType.ROGUE,
                ISkill.getSwords, ISkill.rightClick,
                3, true, false));

        addSkill(new BattleTaunt("Battle Taunt",
                core, eClassType.GUARDIAN,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new Hookshot("Hookshot",
                core, eClassType.RANGER,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new ExecutionArrow("Execution Arrow",
                core, eClassType.RANGER,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new BeastCall("Beast Call",
                core, eClassType.SAMURAI,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new SageToads("Sage Toads",
                core, eClassType.SAMURAI,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new WaterPrison("Water Prison",
                core, eClassType.MAGE,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));

        addSkill(new Plaguespreader("Plaguespreader",
                core, eClassType.MAGE,
                ISkill.getSwords, ISkill.rightClick,
                3, true, true));



    }

    private void registerAxeSkills(){
        addSkill(new BullsCharge("Bulls Charge",
                core, eClassType.WARRIOR,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new HoldPosition("Hold Position",
                core, eClassType.WARRIOR,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Barrier("Barrier",
                core, eClassType.GUARDIAN,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new SeismicSlam("Seismic Slam",
                core, eClassType.GUARDIAN,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Leap("Leap",
                core, eClassType.ROGUE,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Blink("Blink",
                core, eClassType.ROGUE,
                ISkill.getAxes, ISkill.rightClick,
                4, true, true));

        addSkill(new Flamethrower("Flamethrower",
                core, eClassType.MAGE,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Avalanche("Avalance",
                core, eClassType.MAGE,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new SmokeBomb("Smoke Bomb",
                core, eClassType.RANGER,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Ricochet("Ricochet",
                core, eClassType.RANGER,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Scout("Scout",
                core, eClassType.SAMURAI,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));

        addSkill(new Stampede("Stampede",
                core, eClassType.SAMURAI,
                ISkill.getAxes, ISkill.rightClick,
                3, true, true));
    }

    private void registerBowSkills(){
        addSkill(new MagicArrows("Magic Arrows",
                core, eClassType.RANGER,
                ISkill.getBow, ISkill.leftClick,
                1, false, true));
    }

    private void registerPassiveAs(){
        addSkill(new Swift("Swift",
                core, eClassType.ROGUE,
                ISkill.noMaterials, ISkill.noActions,
                1, false, false));

        addSkill(new Swordsmanship("Swordsmanship",
                core, eClassType.WARRIOR,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Maim("Maim",
                core, eClassType.WARRIOR,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new FrostShield("Frost Shield",
                core, eClassType.MAGE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new InfernoReborn("Inferno",
                core, eClassType.MAGE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));



        addSkill(new Repair("Repair",
                core, eClassType.RANGER,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Ram("Ram",
                core, eClassType.GUARDIAN,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new HealingSong("Healing Song",
                core, eClassType.SAMURAI,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new AngelsBlessing("Angel's Blessing",
                core, eClassType.SAMURAI,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Moxie("Moxie",
                core, eClassType.ROGUE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new HealingArrow("Healing Arrow",
                core, eClassType.RANGER,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));
    }

    private void registerPassiveBs(){
        addSkill(new Backstab("Backstab",
                core, eClassType.ROGUE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Tenacity("Tenacity",
                core, eClassType.WARRIOR,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Tempo("Tempo",
                core, eClassType.WARRIOR,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new GlacialAxe("Glacial Axe",
                core, eClassType.GUARDIAN,
                ISkill.noMaterials, ISkill.noActions,
                1, false, false));

        addSkill(new MagmaShield("Magma Shield",
                core, eClassType.MAGE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new ArmourPadding("Armour Padding",
                core, eClassType.RANGER,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new Firefox("Firefox",
                core, eClassType.SAMURAI,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new AntiVenom("Antivenom",
                core, eClassType.RANGER,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

        addSkill(new ElementalGolem("Wind Golem",
                core, eClassType.MAGE,
                ISkill.noMaterials, ISkill.noActions,
                3, false, false));

    }

    private void addSkill(Skill skill){
        this.skillList.add(skill);
    }

    public void generateDefaults(Client client){
        BuildsContainer buildsContainer = new BuildsContainer();

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.WARRIOR, new ClassBuild(eClassType.WARRIOR,
                    new BuildSkill(getSkill("Riposte"), 1),
                    new BuildSkill(getSkill("Bulls Charge"), 1),
                    null, null, null, null, isActive));
        }

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.ROGUE, new ClassBuild(eClassType.ROGUE,
                    new BuildSkill(getSkill("Venom Strike"), 1),
                    new BuildSkill(getSkill("Leap"), 1),
                    null,
                    new BuildSkill(getSkill("Swift"), 1),
                    new BuildSkill(getSkill("Backstab"), 1), null, isActive));
        }

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.GUARDIAN, new ClassBuild(eClassType.GUARDIAN,
                    new BuildSkill(getSkill("Battle Taunt"), 1),
                    new BuildSkill(getSkill("Seismic Slam"), 1),
                    null,
                    new BuildSkill(getSkill("Ram"), 1),
                    new BuildSkill(getSkill("Glacial Axe"), 1)
                    , null, isActive));
        }

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.RANGER, new ClassBuild(eClassType.RANGER,
                    new BuildSkill(getSkill("Hookshot"), 1),
                    new BuildSkill(getSkill("Smoke Bomb"), 1),
                    new BuildSkill(getSkill("Magic Arrows"), 1),
                    null,
                    null, null, isActive));
        }

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.SAMURAI, new ClassBuild(eClassType.SAMURAI,
                    new BuildSkill(getSkill("Beast Call"), 1),
                    null,
                    null,
                    null,
                    null, null, isActive));
        }

        for(int i = 3; i > 0; i--){
            boolean isActive = i == 1 ? true : false;
            buildsContainer.addBuild(eClassType.MAGE, new ClassBuild(eClassType.MAGE,
                    new BuildSkill(getSkill("Water Prison"), 1),
                    new BuildSkill(getSkill("Flamethrower"), 1),
                    null,
                    new BuildSkill(getSkill("Frost Shield"), 1),
                    new BuildSkill(getSkill("Magma Shield"), 1),
                    null, isActive));
        }

        client.setBuildsContainer(buildsContainer);
    }

    public Skill getSkill(String name){
        for(Skill skill : skillList){
            if(skill.getName().equalsIgnoreCase(name)){
                return skill;
            }
        }
        return null;
    }

    private Core getInstance(){
        return this.core;
    }

}
