package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Relations.Enemy;
import me.marco.Clans.Objects.Relations.Pillage;
import me.marco.Client.Client;
import me.marco.Handlers.ClanManager;
import me.marco.Quests.QuestManager;
import me.marco.Quests.QuestProgression;
import me.marco.Quests.QuestWrapper;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.DoubleStatementValue;
import me.marco.SQL.Objects.StatementValues.IntegerStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.Objects.Transaction;
import me.marco.SQL.SQLRepo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE quests (id INT NOT NULL AUTO_INCREMENT, " +
            "UUID VARCHAR(255), " +
            "QUESTPROFILE VARCHAR(255), " +
            "PRIMARY KEY (id))";

    public QuestRepo(Core instance) {
        super(instance);
        this.name = "quests";
        this.createRepo();
    }

    public void loadData(){
        String query = "SELECT * FROM quests";
        try{
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            QuestManager questManager = getInstance().getQuestManager();
            while(rs.next()){
                QuestProgression questProgression = questManager.questProfileFromString(rs.getString("QUESTPROFILE"));
                Client client = getInstance().getClientManager().getClient(rs.getString("UUID"));
                client.setQuestProgression(questProgression);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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

    public void createProfile(Client client){
        String query = "INSERT INTO quests (UUID) VALUES (?)";
        Statement statement = new Statement(query,
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void setProfile(Client client){
            String query = "UPDATE quests SET QUESTPROFILE=? WHERE UUID=?";
        Statement statement = new Statement(query,
                new StringStatementValue(getInstance().getQuestManager().questProfileToString(client.getQuestProgression())),
                new StringStatementValue(client.getUUID().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void setProfile(String UUID, QuestProgression questProgression){
        String query = "UPDATE quests SET QUESTPROFILE=? WHERE UUID=?";
        Statement statement = new Statement(query,
                new StringStatementValue(getInstance().getQuestManager().questProfileToString(questProgression)),
                new StringStatementValue(UUID));
        getSQL().addQuery(new Query(statement));
    }


}
