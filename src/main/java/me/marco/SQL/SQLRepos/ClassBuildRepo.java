package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Clan.ClanRank;
import me.marco.Client.Client;
import me.marco.Client.ClientRank;
import me.marco.Quests.QuestProgression;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.DoubleStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.SQLRepo;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.BuildsContainer;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.Skill;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClassBuildRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE classbuilds (id INT NOT NULL AUTO_INCREMENT, " +
            "UUID VARCHAR(255), " +
            "ROGUE_SKILLS VARCHAR(255), " +
            "RANGER_SKILLS VARCHAR(255), " +
            "GUARDIAN_SKILLS VARCHAR(255), " +
            "NETHERRITE_SKILLS VARCHAR(255), " +
            "MAGE_SKILLS VARCHAR(255), " +
            "WARRIOR_SKILLS VARCHAR(255), " +
            "PRIMARY KEY (id))";

    public ClassBuildRepo(Core instance) {
        super(instance);
        this.name = "classbuilds";
        this.createRepo();
    }

    public String getName() {
        return this.name;
    }

    public void loadData() {
        String query = "SELECT * FROM classbuilds";
        try {
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("UUID");
                String rogueSkills = rs.getString("ROGUE_SKILLS");
                String rangerSkills = rs.getString("RANGER_SKILLS");
                String guardianSkills = rs.getString("GUARDIAN_SKILLS");
                String netherriteSkills = rs.getString("NETHERRITE_SKILLS");
                String mageSkills = rs.getString("MAGE_SKILLS");
                String warriorSkills = rs.getString("WARRIOR_SKILLS");
                BuildsContainer buildsContainer = new BuildsContainer();
                Client client = getInstance().getClientManager().getClient(UUID.fromString(uuid));
                buildsContainer.addBuild(eClassType.ROGUE, getBuildFromString(eClassType.ROGUE, rogueSkills));
                buildsContainer.addBuild(eClassType.RANGER, getBuildFromString(eClassType.RANGER, rangerSkills));
                buildsContainer.addBuild(eClassType.GUARDIAN, getBuildFromString(eClassType.GUARDIAN, guardianSkills));
                buildsContainer.addBuild(eClassType.SAMURAI, getBuildFromString(eClassType.SAMURAI, netherriteSkills));
                buildsContainer.addBuild(eClassType.MAGE, getBuildFromString(eClassType.MAGE, mageSkills));
                buildsContainer.addBuild(eClassType.WARRIOR, getBuildFromString(eClassType.WARRIOR, warriorSkills));
                client.setBuildsContainer(buildsContainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void createDataIfNotExists(Client client){
//        String query = "SELECT * FROM classbuilds WHERE uuid=?";
//        try {
//            PreparedStatement ps = this.getSQL().prepareStatement(query);
//            ps.setString(1, client.getUUID().toString());
//            ResultSet rs = ps.executeQuery();
//            if(!rs.next()){
//                createClientBuild(client);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public ClassBuild getBuildFromString(eClassType classType, String buildString){
        ClassBuild classBuildObj = new ClassBuild(classType);
        String[] commaSplit = buildString.split(",");
        for(int i = 0; i < commaSplit.length; i++){
            String[] skillSplit = commaSplit[i].split("\\|");
            Skill skill = getInstance().getSkillManager().getSkill(skillSplit[0]);
            if(skill == null) continue;
            int level = Integer.parseInt(skillSplit[1]);
            BuildSkill buildSkill = new BuildSkill(skill, level);
            if(i == 0) classBuildObj.setSwordSkill(buildSkill);
            if(i == 1) classBuildObj.setAxeSkill(buildSkill);
            if(i == 2) classBuildObj.setBowSkill(buildSkill);
            if(i == 3) classBuildObj.setPassiveA(buildSkill);
            if(i == 4) classBuildObj.setPassiveB(buildSkill);
        }
        return classBuildObj;
    }

    public String classBuildToString(ClassBuild classBuild){
        String toReturn = "";
        toReturn+= buildSkillToString(classBuild.getSwordSkill()) + ",";
        toReturn+= buildSkillToString(classBuild.getAxeSkill()) + ",";
        toReturn+= buildSkillToString(classBuild.getBowSkill()) + ",";
        toReturn+= buildSkillToString(classBuild.getPassiveA()) + ",";
        toReturn+= buildSkillToString(classBuild.getPassiveB()) + "";
        return toReturn;
    }

    public String buildSkillToString(BuildSkill buildSkill){
        if(buildSkill == null) return "null";
        return buildSkill.getSkill().getName() + "|" + buildSkill.getLevel();
    }

    public void createRepo() {
        try {
            if (!this.getSQL().tableExists(this.name)) {
                System.out.println("Table " + this.name + " doesn't exist... Creating now!");
                System.out.println(createString);
                this.getSQL().executePreparedStatement(createString);
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createClientBuild(Client client) {
        String query = "INSERT INTO classbuilds (UUID, ROGUE_SKILLS, RANGER_SKILLS, GUARDIAN_SKILLS, NETHERRITE_SKILLS, MAGE_SKILLS, WARRIOR_SKILLS) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(client.getUUID().toString()),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.ROGUE))),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.RANGER))),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.GUARDIAN))),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.SAMURAI))),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.MAGE))),
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.WARRIOR))));
        getSQL().addQuery(new Query(statement));
    }

    public void updateRogue(Client client){
        String query = "UPDATE classbuilds SET ROGUE_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.ROGUE))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateRanger(Client client){
        String query = "UPDATE classbuilds SET RANGER_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.RANGER))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateGuardian(Client client){
        String query = "UPDATE classbuilds SET GUARDIAN_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.GUARDIAN))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateNetherrite(Client client){
        String query = "UPDATE classbuilds SET NETHERRITE_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.SAMURAI))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateMage(Client client){
        String query = "UPDATE classbuilds SET MAGE_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.MAGE))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateWarrior(Client client){
        String query = "UPDATE classbuilds SET WARRIOR_SKILLS=? WHERE uuid=?";
        BuildsContainer buildsContainer = client.getBuildsContainer();
        Statement statement = new Statement(query,
                new StringStatementValue(classBuildToString(buildsContainer.getClassBuilds(eClassType.WARRIOR))),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateClass(ClassBuild classBuild, Client client){
        eClassType classType = classBuild.getClassType();
        if(classType == eClassType.ROGUE){
            updateRogue(client);
            return;
        }
        if(classType == eClassType.RANGER){
            updateRanger(client);
            return;
        }
        if(classType == eClassType.GUARDIAN){
            updateGuardian(client);
            return;
        }
        if(classType == eClassType.SAMURAI){
            updateNetherrite(client);
            return;
        }
        if(classType == eClassType.MAGE){
            updateMage(client);
            return;
        }
        if(classType == eClassType.WARRIOR){
            updateWarrior(client);
            return;
        }
    }

}
