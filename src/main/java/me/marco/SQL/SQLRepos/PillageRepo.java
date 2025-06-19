package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Relations.Enemy;
import me.marco.Clans.Objects.Relations.Pillage;
import me.marco.Handlers.ClanManager;
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

public class PillageRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE pillages (id INT NOT NULL AUTO_INCREMENT, " +
            "PillagingClan VARCHAR(255), " +
            "ToPillage VARCHAR(255), " +
            "StartTime DOUBLE, " +
            "PRIMARY KEY (id))";

    public PillageRepo(Core instance) {
        super(instance);
        this.name = "pillages";
        this.createRepo();
    }

    public void loadData(){
        String query = "SELECT * FROM pillages";
        try{
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ClanManager clanManager = getInstance().getClanManager();
            while(rs.next()){
                Clan pillagingClan = clanManager.getClan(rs.getString("PillagingClan"));
                Clan toPillage = clanManager.getClan(rs.getString("ToPillage"));
                double startTime = rs.getDouble("StartTime");
                Pillage pillage = new Pillage(pillagingClan, toPillage, startTime);
                pillagingClan.addPillage(pillage);
                toPillage.addPillage(pillage);
                getInstance().getPillageManager().loadPillage(pillage);
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

    public void addPillage(Clan pillagingClan, Clan toPillage, long startTime){
        String query = "INSERT INTO pillages (PillagingClan, ToPillage, StartTime) VALUES (?, ?, ?)";
        Statement statement = new Statement(query,
                new StringStatementValue(pillagingClan.getName()),
                new StringStatementValue(toPillage.getName()),
                new DoubleStatementValue(startTime));
        getSQL().addQuery(new Query(statement));
    }

    public void deletePillage(Pillage pillage){
        String query = "DELETE FROM pillages WHERE PillagingClan=? AND ToPillage=?";
        Statement statement = new Statement(query,
                new StringStatementValue(pillage.getPillaging().getName()),
                new StringStatementValue(pillage.getToPillage().getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void wipePillages(Clan clan){
        String query = "DELETE FROM pillages WHERE PillagingClan=? OR ToPillage=?";
        StringStatementValue value = new StringStatementValue(clan.getName());
        Statement statement = new Statement(query, value, value);
        getSQL().addQuery(new Query(statement));
    }

}
