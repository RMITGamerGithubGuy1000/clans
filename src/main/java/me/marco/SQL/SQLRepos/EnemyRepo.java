package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Relations.Alliance;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Relations.Enemy;
import me.marco.Handlers.ClanManager;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.IntegerStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.Objects.Transaction;
import me.marco.SQL.SQLRepo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnemyRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE enemies (id INT NOT NULL AUTO_INCREMENT, " +
            "Clan1 VARCHAR(255), " +
            "Clan2 VARCHAR(255), " +
            "Dominance INTEGER DEFAULT 0, " +
            "PRIMARY KEY (id))";

    public EnemyRepo(Core instance) {
        super(instance);
        this.name = "enemies";
        this.createRepo();
    }

    public void loadData(){
        String query = "SELECT * FROM enemies";
        try{
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ClanManager clanManager = getInstance().getClanManager();
            while(rs.next()){
                Clan enemyOwner = clanManager.getClan(rs.getString("Clan1"));
                Clan enemyWith = clanManager.getClan(rs.getString("Clan2"));
                int dominance = rs.getInt("Dominance");
                enemyOwner.addEnemy(new Enemy(enemyOwner, enemyWith, dominance));
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

    public void addEnemy(Clan clan1, Clan clan2){
        String query = "INSERT INTO enemies (Clan1, Clan2) VALUES (?, ?)";
        List<Statement> statementList = new ArrayList<Statement>();
        statementList.add(new Statement(query,
                new StringStatementValue(clan1.getName()),
                new StringStatementValue(clan2.getName())));
        statementList.add(new Statement(query,
                new StringStatementValue(clan2.getName()),
                new StringStatementValue(clan1.getName())));
        Transaction transaction = new Transaction(statementList);
        getSQL().addQuery(transaction);
    }

    public void removeEnemy(Clan clan1, Clan clan2){
        String query = "DELETE FROM enemies WHERE clan1=? AND clan2=?";
        List<Statement> statementList = new ArrayList<Statement>();
        statementList.add(new Statement(query,
                new StringStatementValue(clan1.getName()),
                new StringStatementValue(clan2.getName())));
        statementList.add(new Statement(query,
                new StringStatementValue(clan2.getName()),
                new StringStatementValue(clan1.getName())));
        Transaction transaction = new Transaction(statementList);
        getSQL().addQuery(transaction);
    }

    public void updateDominance(Clan gaining, Clan losing) {
        String query = "UPDATE enemies SET dominance=? WHERE clan1=? AND clan2=?";
        List<Statement> statementList = new ArrayList<Statement>();
        statementList.add(new Statement(query,
                new IntegerStatementValue(gaining.getDominance(losing)),
                new StringStatementValue(gaining.getName()),
                new StringStatementValue(losing.getName())));
        statementList.add(new Statement(query,
                new IntegerStatementValue(losing.getDominance(gaining)),
                new StringStatementValue(losing.getName()),
                new StringStatementValue(gaining.getName())));
        Transaction transaction = new Transaction(statementList);
        getSQL().addQuery(transaction);
    }

    public void deleteEnemies(Clan clan){
        String query = "DELETE FROM enemies WHERE clan1=? OR clan2=?";
        StringStatementValue clanName = new StringStatementValue(clan.getName());
        Statement statement = new Statement(query,
                clanName,
                clanName);
        getSQL().addQuery(new Query(statement));
    }

}
